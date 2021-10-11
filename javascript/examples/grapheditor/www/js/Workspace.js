/**
 * Copyright (c) CrayonScript
 *
 * Constructs a new workspace for the given editor sidebar
 */
function Workspace(sidebar)
{
    this.sidebar = sidebar;
    this.editorUi = sidebar.editorUi;
    this.init();
};

Workspace.prototype.init = function()
{
    this.createUnityWorkspace();
}

Workspace.prototype.destroy = function()
{

}

Workspace.prototype.showViewer = function(contents)
{
    this.unityWorkspace.style.visibility = 'visible';
}

Workspace.prototype.hideViewer = function()
{
    this.unityWorkspace.style.visibility = 'hidden';
}

/**
 * Creates a new workspace.
 */
Workspace.prototype.createUnityWorkspace = function()
{
    const sidebar = this.sidebar;
    const lineTags = 'line lines connector connectors connection connections arrow arrows ';
    sidebar.setCurrentSearchEntryLibrary('workspace', 'workspace');

    var fns = [
        sidebar.createVertexTemplateEntry('rounded=0;whiteSpace=wrap;html=1;', 120, 60, '', 'Rectangle', null, null, 'rect rectangle box'),
        sidebar.createVertexTemplateEntry('rounded=1;whiteSpace=wrap;html=1;', 120, 60, '', 'Rounded Rectangle', null, null, 'rounded rect rectangle box'),
    ];

    sidebar.addPaletteFunctions('workspace', mxResources.get('workspace'), true, fns);
    sidebar.setCurrentSearchEntryLibrary();
};




