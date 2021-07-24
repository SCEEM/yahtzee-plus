let stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/roll', function (dice) {
            showDice(JSON.parse(dice.body));
        });
    });
    console.log("Connected");
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showDice (dice) {
    var dieElements = document.getElementsByName("dice"),
        dieDiv = document.querySelectorAll('[id^=dice]');
    dice.forEach(function (dieVal, index) {
        var diceImg = new Image(100, 200);
        diceImg.src = '/images/dice' + dieVal.toString() + '.png';
        dieElements[index].value = dieVal;
        dieDiv[index].appendChild(diceImg);
        dieDiv[index].removeAttribute("hidden")
    })
}

function rollDice() {
    stompClient.send("/app/turn/roll");
}

function joinGame (player) {
    playerId = player.playerId;
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#rollDice" ).click(function() { rollDice(); });
});