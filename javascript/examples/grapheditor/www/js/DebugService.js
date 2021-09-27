function DebugService(editorUI)
{
    this.editorUI = editorUI;
    setTimeout(sendSourceCodeToUnityEditor, 1000, this.editorUI, 1);
    //setTimeout(pingUnityEditor, 1000, this.editorUI, 1);
}

function pingUnityEditor(editorUI, counter)
{
    const xmlHTTP = new XMLHttpRequest();
    xmlHTTP.open('POST', 'http://127.0.0.1:10002/ping', true);
    xmlHTTP.onreadystatechange = function() { // Call a function when the state changes.
        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            // Request finished. Do processing here.
            // show the unity toolbar
        }
        else
        {
            // hide the unity toolbar
        }
        setTimeout(pingUnityEditor, 3000, this.editorUI, ++counter);
    }
    xmlHTTP.send();
}

function sendSourceCodeToUnityEditor(editorUI, counter)
{
    // attempt a connection to the Unity HTTP Server
    // if this succeeds, add the Unity refresh button to toolbar
    // if this fails, ensure the Unity refresh button is removed from the toolbar
    const xmlHTTP = new XMLHttpRequest();
    xmlHTTP.open('POST', 'http://127.0.0.1:10002/source', true);
    xmlHTTP.onreadystatechange = function() { // Call a function when the state changes.
        if (this.readyState === XMLHttpRequest.DONE) {
            // Request finished. Do processing here.
            setTimeout(sendSourceCodeToUnityEditor, 5000, editorUI, ++counter);
        }
    }
    const source = this.editorUI.blockEditor.getContents();
    const base64Source = btoa(source)
    xmlHTTP.send(base64Source);
}

DebugService.prototype.setBreakpoint = function(row)
{
    const req = {
        "cmd": "breakpoint",
        "arg": "set",
        "sourceId": -1,
        "sourceLine": row,
        "sourceCol": 0
    }
    this.setRequest(req);
}

DebugService.prototype.clearBreakpoint = function(row)
{
    const req = {
        "cmd": "breakpoint",
        "arg": "clear",
        "sourceId": -1,
        "sourceLine": row,
        "sourceCol": 0
    }
    this.setRequest(req);
}

DebugService.prototype.setRequest = function(req)
{
    //this.editorUI.SendMessage('Controller', 'SetDebugRequest', req);
}