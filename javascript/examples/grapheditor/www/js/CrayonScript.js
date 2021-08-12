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
                blockEditor.showCodeView();
            }
        }
    }));
}

CrayonScript.prototype.importCode = function(node)
{

}

CrayonScript.prototype.importData = function(node)
{

}

//
// Utility functions
//
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


