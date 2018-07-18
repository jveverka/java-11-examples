
console.log("started ...");

var  websocket = new WebSocket("ws://127.0.0.1:8080/websocket", "ws");

function onMessage(message) {
    console.log("onMessage: " + message)
}

websocket.onopen = function() { console.log("ws opened"); }
websocket.onerror = function() { console.log("ws error"); }
websocket.onmessage = function(e) { onMessage(e.data); }
websocket.onclose = function() { console.log("ws closed"); }

function sendWsMessage(message) {
    websocket.send(message);
}