/**
 * Copyright (c) CrayonScript
 */
/**
 * Constructs a new code editor for the given editor
 */
function BlockEditor(editorUi, container)
{
    this.editorUi = editorUi;
    this.container = container;
    this.init();
};

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

    const editor = ace.edit(editorId);
    editor.setTheme("ace/theme/vibrant_ink");
    editor.session.setMode("ace/mode/lua");
    editor.setShowPrintMargin(false);

    this.codeEditor = editor;
};

/**
 * Creates a new data editor
 */
BlockEditor.prototype.createDataEditor = function()
{

}

BlockEditor.prototype.init = function()
{
    this.createCodeEditor();
    this.createDataEditor();
}

BlockEditor.prototype.destroy = function()
{

}






