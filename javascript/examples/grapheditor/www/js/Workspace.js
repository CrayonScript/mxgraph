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
    const self = this;
    self.sidebar.setCurrentSearchEntryLibrary('workspace', 'workspace');

    self.getFiles(function(files) {
        const fns = [];
        for (const filesKey in files) {
            const fileName = files[filesKey];
            const fn = self.createEntry('rounded=0;whiteSpace=wrap;html=1;', 120, 60, '', `${fileName}`, null, null, 'rect rectangle box');
            fns.push(fn);
        }
        self.sidebar.addPaletteFunctions('workspace', mxResources.get('workspace'), true, fns);
        self.sidebar.setCurrentSearchEntryLibrary();
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

Workspace.prototype.getFile = function(name, fn)
{
    const listRequest = new XMLHttpRequest();
    listRequest.open('POST', 'http://127.0.0.1:10002/workspace/get', true);
    listRequest.onreadystatechange = function() { // Call a function when the state changes.
        if (this.readyState === XMLHttpRequest.DONE && this.status == 200) {
            // Request finished. Do processing here.
            const responseBase64Str = this.responseText;
            if (responseBase64Str)
            {
                const responseStr = atob(responseBase64Str);
                const responseJSON = JSON.parse(responseStr);
                const contentsBase64 = responseJSON.contents;
                const contentsStr = atob(contentsBase64);
                fn(contentsStr);
            }
            else
            {
                fn("");
            }
        }
    }
    const nameBase64 = btoa(name);
    listRequest.send(nameBase64);
}

Workspace.prototype.createEntry = function(style, width, height, value, title, showLabel, showTitle, tags)
{
    const self = this;

    tags = (tags != null && tags.length > 0) ? tags : ((title != null) ? title.toLowerCase() : '');

    return this.sidebar.addEntry(tags, mxUtils.bind(this, function()
    {
        return self.createEntryElement(style, width, height, value, title, showLabel, showTitle);
    }));
}

Workspace.prototype.createEntryElement = function(style, width, height, value, title, showLabel, showTitle, allowCellsInserted)
{
    const elt = document.createElement('a');
    elt.className = 'geItem';
    elt.style.overflow = 'hidden';
    elt.style.width = width * 5 + 'px';
    elt.style.height = height + 'px';
    elt.style.padding = '2px';
    elt.style.width = width * 5 + 'px';

    const titleDiv = document.createElement('div');
    titleDiv.innerHTML = title;
    //titleDiv.appendChild(node);
    elt.appendChild(titleDiv);

    const self = this;

    // Blocks default click action
    mxEvent.addListener(elt, 'click', function(evt)
    {
        mxEvent.consume(evt);
        const blockEditor = self.editorUi.blockEditor;
        self.getFile(title, function(contents) {
            blockEditor.setCodeContents(contents);
        })
    });

    return elt;
};




