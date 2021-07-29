let stompClient = null,
    currentKeepers = [],
    currentDice = [];

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
        stompClient.subscribe('/topic/keepers', function (keepers) {
            showKeepers(JSON.parse(keepers.body));
        });
        stompClient.subscribe('/topic/chat', function (chat) {
            console.log(chat);
            showChat(JSON.parse(chat.body));
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
        dieDiv = document.querySelectorAll('div[id^=dice]');
    currentDice = dice;

    $('#rollSet img[id^=diceImg]').remove();
    $(dieDiv).hide();
    $('[type=checkbox]').prop("checked", false);

    dice.forEach(function (dieVal, index) {
        var diceImg = new Image(50, 50);
        diceImg.id = 'diceImg' + dieVal;
        dieElements[index].value = dieVal;
        dieDiv[index].appendChild(diceImg);
        $(dieDiv[index]).show();
    })
}

function showKeepers (keepers) {
    var keeperDiv = document.querySelectorAll('div[id^=keeper]');
    currentKeepers = keepers;
    $('#keeperSet img[id^=diceImg]').remove();
    keepers.forEach(function (keeperVal, index) {
        var diceImg = new Image(50, 50);
        diceImg.id = 'diceImg' + keeperVal;
        keeperDiv[index].appendChild(diceImg);
        keeperDiv[index].removeAttribute("hidden")
    })
}

function showChat (chat) {
    console.log("RETURNED: " + chat);
}

function rollDice() {
    $(document.querySelectorAll('[id^=diceImg]')).remove();
    stompClient.send("/app/turn/roll");
}

function joinGame (player) {
    var playerId = player.playerId;
}

function setKeepers () {
    let remainingDice = currentDice;
    $('.dice:checkbox:checked').each(function (index, checkElement) {
        currentKeepers.push(checkElement.value);
        remainingDice.splice(remainingDice.indexOf(parseInt(checkElement.value)), 1)
    });
    showDice(remainingDice);
    stompClient.send("/app/turn/roll/keep", {}, JSON.stringify(currentKeepers));

}

function sendChat (chatMsg) {
    console.log(chatMsg);
    stompClient.send("/app/chat", {}, JSON.stringify(chatMsg));

}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#rollDice" ).click(function() { rollDice(); });
    $( "#setKeepers" ).click(function() { setKeepers(); });
    $( "#sendChat" ).click(function() { sendChat($('#chatMsg').val()); });
});