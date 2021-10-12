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
    sidebar.setCurrentSearchEntryLibrary('workspace', 'workspace');

    this.getFiles(function(files) {
        const fns = [];
        for (const filesKey in files) {
            var fileName = files[filesKey];
            var fn = sidebar.createVertexTemplateEntry('rounded=0;whiteSpace=wrap;html=1;', 120, 60, '', `${fileName}`, null, null, 'rect rectangle box');
            fns.push(fn);
        }
        sidebar.addPaletteFunctions('workspace', mxResources.get('workspace'), true, fns);
        sidebar.setCurrentSearchEntryLibrary();
    });

};

Workspace.prototype.getFiles = function(fn)
{
    const listRequest = new XMLHttpRequest();
    listRequest.open('POST', 'http://127.0.0.1:10002/workspace/list', true);
    listRequest.onreadystatechange = function() { // Call a function when the state changes.
        if (this.readyState === XMLHttpRequest.DONE && this.status == 200) {
            // Request finished. Do processing here.
            const responseBase64Str = this.responseText;
            const responseStr = atob(responseBase64Str);
            const responseJSON = JSON.parse(responseStr);
            const names = [];
            for (const itemKey in responseJSON.Payload) {
                const itemJSON = responseJSON.Payload[itemKey];
                const nameBase64 = itemJSON.name;
                const nameStr = atob(nameBase64);
                names.push(nameStr);
            }
            fn(names);
        }
    }
    listRequest.send();
}




