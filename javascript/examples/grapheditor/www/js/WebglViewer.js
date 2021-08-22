/**
 * Copyright (c) CrayonScript
 */
/**
 * Constructs a new code editor for the given editor
 */
function WebglViewer(editorUi, container)
{
    this.editorUi = editorUi;
    this.container = container;
    this.init();
};

WebglViewer.prototype.init = function()
{
    this.createViewer();
}

WebglViewer.prototype.destroy = function()
{

}

WebglViewer.prototype.showViewer = function(contents)
{
    this.webglViewerContainer.style.visibility = 'visible';
}

WebglViewer.prototype.hideViewer = function()
{
    this.webglViewerContainer.style.visibility = 'hidden';
}

/**
 * Creates a new webgl viewer.
 */
WebglViewer.prototype.createViewer = function()
{
    //
    // webgl viewer
    //
    this.webglViewerContainer = document.createElement('div');
    this.webglViewerContainer.className = 'webgl-content';
    this.container.appendChild(this.webglViewerContainer);
    this.webglViewer = document.createElement('div');
    this.webglViewer.id = 'unityContainer';
    this.webglViewer.className = 'geWebglUnityContainer';
    this.webglViewerContainer.appendChild(this.webglViewer);

    this.webglInstance = UnityLoader.instantiate(this.webglViewer, "webgl/Build/webgl-crayonscript.json", {onProgress: UnityProgress});
};





