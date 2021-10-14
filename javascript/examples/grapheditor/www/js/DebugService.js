function DebugService(editorUI)
{
    this.editorUI = editorUI;
    this.debugRequests = [];
}

DebugService.prototype.importDebugData = function()
{
    const debugDataArr = {
        Payload: this.debugRequests
    }
    const debugDataStr = JSON.stringify(debugDataArr);
    const debugDataBase64 = btoa(debugDataStr);
    return debugDataBase64;
}

DebugService.prototype.sendDebugRequest = function (debugRequest)
{
    // add to the debug requests
    this.debugRequests.push(debugRequest);

    const unityService = this.editorUI.unityService;
    unityService.postRequest('debug', debugRequest);

    // const webglService = this.editorUI.webglViewer;
    // webglService.sendRequest('debug', debugRequest);
}

DebugService.prototype.refreshUnity = function()
{
    const xmlHTTP = new XMLHttpRequest();
    xmlHTTP.open('POST', 'http://127.0.0.1:10002/unity/refresh', true);
    xmlHTTP.onreadystatechange = function() { // Call a function when the state changes.
        if (this.readyState === XMLHttpRequest.DONE) {
            // Request finished. Do processing here.
        }
    }
    xmlHTTP.send();
}

DebugService.prototype.setBreakpoint = function(sourceName, row)
{
    const tokens = getSourceCodeTokens();
    const sourceLine = (row + 1);
    if (!isValidSourceCodeAtLine(tokens, sourceLine)) return false;

    const sourceCol = getSourceCodeCol(tokens, sourceLine);

    const req = {
        cmd: "breakpoint",
        arg: "set",
        sourceName: sourceName,
        sourceId: 0,
        sourceLine: sourceLine,
        sourceCol: sourceCol
    }

    this.sendDebugRequest(req);

    return true;
}

DebugService.prototype.clearBreakpoint = function(sourceName, row)
{
    const tokens = getSourceCodeTokens();
    const sourceLine = (row + 1);
    if (!isValidSourceCodeAtLine(tokens, sourceLine)) return false;

    const sourceCol = getSourceCodeCol(tokens, sourceLine);

    const req = {
        cmd: "breakpoint",
        arg: "clear",
        sourceName: sourceName,
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