function DebugService(editorUI)
{
    this.editorUI = editorUI;
}

DebugService.prototype.sendDebugRequest = function (debugRequest)
{
   const unityService = this.editorUI.unityService;
   unityService.postRequest('debug', debugRequest);
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
    const tokens = getSourceCodeTokens();
    const sourceLine = (row + 1);
    if (!isValidSourceCodeAtLine(tokens, sourceLine)) return false;

    const sourceCol = getSourceCodeCol(tokens, sourceLine);

    const req = {
        cmd: "breakpoint",
        arg: "set",
        sourceId: 1,
        sourceLine: sourceLine,
        sourceCol: sourceCol
    }

    this.sendDebugRequest(req);

    return true;
}

DebugService.prototype.clearBreakpoint = function(row)
{
    const tokens = getSourceCodeTokens();
    const sourceLine = (row + 1);
    if (!isValidSourceCodeAtLine(tokens, sourceLine)) return false;

    const sourceCol = getSourceCodeCol(tokens, sourceLine);

    const req = {
        cmd: "breakpoint",
        arg: "clear",
        sourceId: 1,
        sourceLine: sourceLine,
        sourceCol: sourceCol
    }

    this.sendDebugRequest(req);

    return true;
}

function getSourceCodeTokens()
{
    const source = getSourceCode();
    const ast = luaparse.parse(source);
    const tokens = ast.tokens;

    return tokens;
}

function getSourceCode()
{
    const source = this.editorUI.blockEditor.getContents();
    return source;
}

function isValidSourceCodeAtLine(tokens, sourceLine)
{
    return (sourceLine in tokens);
}

function getSourceCodeCol(tokens, sourceLine)
{
    let sourceCol = 0;
    const token = tokens[sourceLine];
    const tokenObj = token.at(-1);
    if (tokenObj.type == 32)
    {
        sourceCol = tokenObj.range[1] - tokenObj.lineStart;
    }
    return sourceCol;
}