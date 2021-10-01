function DebugService(editorUI)
{
    this.editorUI = editorUI;
}

DebugService.prototype.sendDebugRequest = function (debugRequest)
{
    const xmlHTTP = new XMLHttpRequest();
    xmlHTTP.open('POST', 'http://127.0.0.1:10002/debug', true);
    xmlHTTP.onreadystatechange = function() { // Call a function when the state changes.
        if (this.readyState === XMLHttpRequest.DONE) {

        }
    }
    const debug = JSON.stringify(debugRequest);
    const base64Debug = btoa(debug)
    xmlHTTP.send(base64Debug);
}

DebugService.prototype.sendSourceCodeToUnity = function()
{
    // attempt a connection to the Unity HTTP Server
    // if this succeeds, add the Unity refresh button to toolbar
    // if this fails, ensure the Unity refresh button is removed from the toolbar
    const xmlHTTP = new XMLHttpRequest();
    xmlHTTP.open('POST', 'http://127.0.0.1:10002/source', true);
    xmlHTTP.onreadystatechange = function() { // Call a function when the state changes.
        if (this.readyState === XMLHttpRequest.DONE) {
            // Request finished. Do processing here.
        }
    }
    const source = this.editorUI.blockEditor.getContents();
    const base64Source = btoa(source)
    xmlHTTP.send(base64Source);
}

DebugService.prototype.setBreakpoint = function(row)
{
    const source = this.editorUI.blockEditor.getContents();
    const ast = luaparse.parse(source);
    const tokens = ast.tokens;

    const sourceLine = (row + 1);

    if (!(sourceLine in tokens))
    {
        // not a valid breakpoint
        return false;
    }

    const req = {
        cmd: "breakpoint",
        arg: "set",
        sourceId: 1,
        sourceLine: sourceLine,
        sourceCol: 0
    }

    this.sendDebugRequest(req);

    return true;
}

DebugService.prototype.clearBreakpoint = function(row)
{
    const source = this.editorUI.blockEditor.getContents();
    const ast = luaparse.parse(source);
    const tokens = ast.tokens;

    const sourceLine = (row + 1);

    if (!(sourceLine in tokens))
    {
        // not a valid breakpoint
        return false;
    }

    const req = {
        cmd: "breakpoint",
        arg: "clear",
        sourceId: 1,
        sourceLine: sourceLine,
        sourceCol: 0
    }

    this.sendDebugRequest(req);

    return true;
}