/**
 * Copyright (c) CrayonScript
 */
/**
 * Constructs a new code editor for the given editor
 */
function BlockEditor(editorUi, container, debugService)
{
    this.editorUi = editorUi;
    this.container = container;
    this.debugService = debugService;
    this.init();
};

BlockEditor.prototype.init = function()
{
    this.createCodeEditor();
    this.createDataEditor();
    this.hideCodeView();
    this.hideDataView();
}

BlockEditor.prototype.destroy = function()
{

}

BlockEditor.prototype.showDataView = function()
{
    this.dataEditorContainer.style.visibility = 'visible';
}

BlockEditor.prototype.hideDataView = function()
{
    this.dataEditorContainer.style.visibility = 'hidden';
}

BlockEditor.prototype.showCodeView = function(contents)
{
    this.codeEditorContainer.style.visibility = 'visible';
    this.codeEditor.setValue(contents, 1);
}

BlockEditor.prototype.hideCodeView = function()
{
    this.codeEditorContainer.style.visibility = 'hidden';
}

BlockEditor.prototype.getContents = function()
{
    return this.codeEditor.getValue();
}

/**
 * Creates a new code editor.
 */
BlockEditor.prototype.createCodeEditor = function()
{
    //
    // ace editor
    //

    var editorId = 'geCodeEditorContainer';
    var elt = document.createElement('div');
    elt.className = 'geCodeEditorContainer';
    elt.id = editorId;
    this.codeEditorContainer = elt;
    this.container.appendChild(this.codeEditorContainer);

    var debugService = this.debugService;

    const editor = ace.edit(editorId);
    editor.setTheme("ace/theme/vibrant_ink");
    editor.session.setMode("ace/mode/lua");
    editor.setOptions({
        enableBasicAutocompletion: true,
        enableLiveAutocompletion: true,
        showPrintMargin: false
    });
    editor.on("guttermousedown", function(e) {
        const target = e.domEvent.target;
        if (target.className.indexOf("ace_gutter-cell") == -1) return;
        if (!editor.isFocused()) return;
        if (e.clientX > 50 + target.getBoundingClientRect().left) return;
        const row = e.getDocumentPosition().row;
        const breakpoints = editor.session.getBreakpoints(row, 0);
        // If there's a breakpoint already defined, it should be removed, offering the toggle feature
        if(typeof breakpoints[row] === typeof undefined){
            if (debugService.setBreakpoint(row)) {
                editor.session.setBreakpoint(row);
            }
        }else{
            if (debugService.clearBreakpoint(row)) {
                editor.session.clearBreakpoint(row);
            }
        }
        e.stop();
    });

    this.codeEditor = editor;
};

/**
 * Creates a new data editor
 */
BlockEditor.prototype.createDataEditor = function()
{
    //
    // canvas-datagrid
    //

    var editorId = 'geDataEditorContainer';
    var elt = document.createElement('div');
    elt.className = 'geDataEditorContainer';
    elt.id = editorId;
    this.dataEditorContainer = elt;
    this.container.appendChild(this.dataEditorContainer);

    const editor = canvasDatagrid({
        showNewRow : true
    });
    elt.appendChild(editor);

    this.dataEditor = editor;
}






