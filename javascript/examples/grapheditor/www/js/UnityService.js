
function UnityService(editorUI)
{
    this.editorUI = editorUI;
}

UnityService.prototype.postRequest = function(action, jsonReq)
{
    const xmlHTTP = new XMLHttpRequest();
    xmlHTTP.open('POST', `http://127.0.0.1:10002/${action}`, true);
    xmlHTTP.onreadystatechange = function() { // Call a function when the state changes.
        if (this.readyState === XMLHttpRequest.DONE) {

        }
    }
    const jsonStr = JSON.stringify(jsonReq);
    const base64Str = btoa(jsonStr)
    xmlHTTP.send(base64Str);
}