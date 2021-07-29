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
            console.log(dice);
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
    var dieDiv = document.querySelectorAll('div[id^=die]');
    currentDice = dice;

    $('#rollSet img[id^=diceImg]').remove();
    $(dieDiv).hide();
    $('[type=checkbox]').prop("checked", false);

    dice.forEach(function (die, index) {
        var diceImg = new Image(50, 50);
        diceImg.id = 'diceImg' + die.value;
        $('#' + die.id).append(diceImg);
        $('#' + die.id).show();
    })
}

function showKeepers (keepers) {
    var keeperDiv = document.querySelectorAll('div[id^=keeper]');
    currentKeepers = keepers;
    $('#keeperSet img[id^=diceImg]').remove();
    $(keeperDiv).hide();
    keepers.forEach(function (keeper, index) {
        var diceImg = new Image(50, 50);
        diceImg.id = 'diceImg' + keeperVal;
        $('#' + keeper.id).append(diceImg);
        $('#' + keeper.id).show();
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
        var keeperId = $(this).parent().attr('id');
        currentKeepers = currentDice.filter(function( die, index ) {
            if (die.id === keeperId) {
                remainingDice.splice(index, 1);
                currentKeepers.push(die)
            }
        });
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