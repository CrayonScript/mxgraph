/**
 * CrayonScript
 *
 * Manages all crayonscript specific functions
 */

CrayonScript = function(editorUi)
{
    this.editor = editorUi.editor;
    this.blockEditor = editorUi.blockEditor;
    this.graph = this.editor.graph;
    this.graphView = this.graph.view;
}

CrayonScript.prototype.init = function() {

    this.graph.addListener(mxEvent.CLICK, mxUtils.bind(this, function (sender, evt) {
        if (evt != null)
        {
            // get the block editor
            var blockEditor = this.blockEditor;
            // get the cell
            var cell = evt.properties.cell;
            if (this.isDataCell(cell))
            {
                blockEditor.hideCodeView();
                blockEditor.showDataView();
            }
            else if (this.isCodeCell(cell))
            {
                blockEditor.hideDataView();
                let code = this.getCode(cell);
                code = code.trim();
                blockEditor.showCodeView(code);
            }
        }
    }));
}

CrayonScript.prototype.buildCells = function()
{

}

CrayonScript.prototype.buildCell = function(cellId)
{
    const cell = this.findCell(cellId);
    let code = this.getCode(cell);
    var ast = luaparse.parse(code);
    console.log(JSON.stringify(ast));
}

CrayonScript.prototype.selectCell = function(cellId)
{
    const cell = this.findCell(cellId);
    this.clickCell(cell);
}

CrayonScript.prototype.findCell = function(cellId)
{
    const graphModel = this.graph.model;
    // these are template cells
    for (let key in graphModel.cells) {
        let cell = graphModel.cells[key];
        if (cell.getId() == cellId) {
            return cell;
        }
    }
    return null;
}

CrayonScript.prototype.importCode = function(node)
{
    this.codeObjects = this.readCellContents(node, "codeCell")
}

CrayonScript.prototype.importData = function(node)
{
    this.dataObjects = this.readCellContents(node, "dataCell");
}

CrayonScript.prototype.readCellContents = function(node, contentTag)
{
    const cellContents = [];
    const codeStack = [];
    codeStack.push(node);
    while (codeStack.length > 0) {
        let nodeToProcess = codeStack.pop();
        if (nodeToProcess.tagName == contentTag) {
            let cellId = nodeToProcess.attributes["cellId"].value;
            let cellContent = this.readCDATA(nodeToProcess);
            cellContents.push({ cellId: cellId, cellContent: cellContent });
        }
        for (let i = 0; i < nodeToProcess.childElementCount; i++) {
            codeStack.unshift(nodeToProcess.children[i]);
        }
    }
    return cellContents;
}

CrayonScript.prototype.clickCell = function(cell)
{
    const cellState = this.graphView.getState(cell);
    if (cellState != null) {
        this.graph.click(new mxMouseEvent(new PointerEvent("mouse"), cellState));
    }
}

CrayonScript.prototype.getData = function(cell)
{
    const data = this.dataObjects[cell.getId()];
    return data;
}

CrayonScript.prototype.importScript = function()
{
    const cell = this.findCell("c0");
    const code = this.getCode(cell);
    const base64Code = btoa(code);
    return base64Code;
}

CrayonScript.prototype.getCode = function(cell)
{
    for (let codeKey in this.codeObjects) {
        let codeObject = this.codeObjects[codeKey];
        if (codeObject.cellId == cell.getId()) {
            let code = codeObject.cellContent;
            code = code.trim();
            return code;
        }
    }
    return "";
}

CrayonScript.prototype.save = function(filename)
{
    this.saveCode(filename)
    this.saveData(filename)
}

CrayonScript.prototype.saveCode = function(filename)
{
    // remove the .graph substring from the filename
    filename = filename.substr(0, filename.length - ".graph".length);
    filename = filename + ".code";
    const codeDocument = mxUtils.createXmlDocument();
    const crayonScriptNode = codeDocument.createElement("crayonscript");
    const dataNode = codeDocument.createElement("data");
    const rootNode = codeDocument.createElement("root");
    codeDocument.appendChild(crayonScriptNode);
    crayonScriptNode.appendChild(dataNode);
    dataNode.appendChild(rootNode);
    for (let codeKey in this.codeObjects)
    {
        const codeObject = this.codeObjects[codeKey];
        const code = codeObject.cellContent;
        const codeCellNode = codeDocument.createElement("codeCell");
        codeCellNode.setAttribute("cellId", codeObject.getId());
        const cDataSection = codeDocument.createCDATASection(code);
        codeCellNode.appendChild(cDataSection);
        rootNode.appendChild(codeCellNode);
    }
    var codeXml = mxUtils.getXml(codeDocument, '\n');
    if (codeXml.length < MAX_REQUEST_SIZE)
    {
        new mxXmlRequest(SAVE_URL, 'filename=' + encodeURIComponent(filename) +
            '&codeXml=' + encodeURIComponent(codeXml)).send();
    }

}

CrayonScript.prototype.saveData = function(filename)
{
    // remove the .graph substring from the filename
    filename = filename.substr(0, filename.length - ".graph".length);
    filename = filename + ".data";
    const dataDocument = mxUtils.createXmlDocument();
    const crayonScriptNode = dataDocument.createElement("crayonscript");
    const dataNode = dataDocument.createElement("data");
    const rootNode = dataDocument.createElement("root");
    dataDocument.appendChild(crayonScriptNode);
    crayonScriptNode.appendChild(dataNode);
    dataNode.appendChild(rootNode);
    for (let dataKey in this.dataObjects)
    {
        const dataObject = this.dataObjects[dataKey];
        const code = dataObject.cellContent;
        const codeCellNode = dataDocument.createElement("dataCell");
        codeCellNode.setAttribute("cellId", dataObject.getId());
        const cDataSection = dataDocument.createCDATASection(code);
        codeCellNode.appendChild(cDataSection);
        rootNode.appendChild(codeCellNode);
    }
    var xml = mxUtils.getXml(dataDocument, '\n');
    if (xml.length < MAX_REQUEST_SIZE)
    {
        new mxXmlRequest(SAVE_URL, 'filename=' + encodeURIComponent(filename) +
            '&xml=' + encodeURIComponent(xml)).send();
    }
}

//
// Utility functions
//

CrayonScript.prototype.readCDATA = function(node)
{
    let count = node.childNodes.length;
    for (let i = 0; i < count; i++) {
        if (node.childNodes[i].nodeType == 4) { // CDATA-SECTION = 4
            let text = node.childNodes[i].nodeValue;
            return text;
        }
    }
    return null;
}

CrayonScript.prototype.isTemplateCell = function(cell)
{
    return this.getShapeName(cell) == mxConstants.SHAPE_CRAYONSCRIPT_TEMPLATE;
}

CrayonScript.prototype.isCodeCell = function(cell)
{
    return this.getShapeName(cell) == mxConstants.SHAPE_CRAYONSCRIPT_CODE;
}

CrayonScript.prototype.isDataCell = function(cell)
{
    return this.getShapeName(cell) == mxConstants.SHAPE_CRAYONSCRIPT_DATA;
}

CrayonScript.prototype.getShapeName = function(cell)
{
    var cellState = this.graphView.getState(cell);
    var shapeName = mxUtils.getValue(cellState.style, mxConstants.STYLE_SHAPE);
    return shapeName;
}


