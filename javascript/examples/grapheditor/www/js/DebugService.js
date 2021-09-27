function DebugService(editorUI)
{
    this.editorUI = editorUI;
    this.debugRequests = [];
    setTimeout(sendDebugRequests, 1000, this, 1);
}

function sendDebugRequests(self, counter)
{
    const xmlHTTP = new XMLHttpRequest();
    xmlHTTP.open('POST', 'http://127.0.0.1:10002/debug', true);
    xmlHTTP.onreadystatechange = function() { // Call a function when the state changes.
        if (this.readyState === XMLHttpRequest.DONE) {
            if (this.status == 204)
            {
                // Request finished. Do processing here.
                // Clear the existing
                self.debugRequests.length = 0;
                if (counter == 1)
                {
                    setTimeout(sendSourceCodeToUnityEditor, 5000, this.editorUI, 1);
                }
                setTimeout(sendDebugRequests, 3000, self, ++counter);
            }
            else
            {
                setTimeout(sendDebugRequests, 3000, self, 1);
            }
        }
    }
    const debugJSON = {
        Payload : self.debugRequests
    }
    const debug = JSON.stringify(debugJSON);
    const base64Debug = btoa(debug)
    xmlHTTP.send(base64Debug);
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
        cmd: "breakpoint",
        arg: "set",
        sourceId: -1,
        sourceLine: row,
        sourceCol: 0
    }
    this.debugRequests.push(req);
}

DebugService.prototype.clearBreakpoint = function(row)
{
    const req = {
        cmd: "breakpoint",
        arg: "clear",
        sourceId: -1,
        sourceLine: row,
        sourceCol: 0
    }
    this.debugRequests.push(req);
}