/**
 * Copyright (c) CrayonScript
 *
 * Constructs a new workspace for the given editor
 */
function Workspace(editorUi, container)
{
    this.editorUi = editorUi;
    this.container = container;
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
    //
    // unity workspace
    //
    this.unityWorkspace = document.createElement('div');
    this.unityWorkspace.id = "unityWorkspace";
    this.unityWorkspace.className = 'geUnityWorkspaceContainer';
    this.container.appendChild(this.unityWorkspace);
    this.workspace = null;

    mxUtils.post(WORKSPACE_URL, '', mxUtils.bind(this, function(req)
    {
        const encodedText = req.getText();
        const plainText = decodeURIComponent(encodedText);
        if (plainText != null)
        {
            const workspace = JSON.parse(plainText);
        }
    }));
};




