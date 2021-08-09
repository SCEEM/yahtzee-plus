let stompClient = null,
    currentKeepers = [],
    currentDice = [],
    activePlayerId,
    playerId,
    isActivePlayer = false;

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

    var bannerImg = new Image();
    bannerImg.id = 'bannerImg';
    $('#banner').append(bannerImg);

    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/activePlayerId', function (playerId) {
            setActivePlayerId(JSON.parse(playerId.body));
        });
        stompClient.subscribe('/topic/roll', function (dice) {
            showDice(JSON.parse(dice.body));
        });
        stompClient.subscribe('/topic/keepers', function (keepers) {
            showKeepers(JSON.parse(keepers.body));
        });
        stompClient.subscribe('/topic/chat', function (chat) {
            showChat(JSON.parse(chat.body));
        });
        stompClient.subscribe('/topic/loadScorecard', function (scorecard) {
            showScoreOptions(scorecard.body);
        });
        stompClient.subscribe('/topic/updateScorecard', function (scorecard) {
            updateScorecard(scorecard.body);
        });
    });
    setActivePlayerId($('#activePlayerId').text());
    console.log("Connected");
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

//___________________________Receivers_________________________________

function setActivePlayerId (activePlayerId) {
    isActivePlayer = activePlayerId.toString() === $('#playerId').text();

    $( "#rollDice" ).hide();
    $('[type=checkbox]').hide();
    $( "#setKeepers" ).hide();
    $( "#submitScore" ).hide();
    $( "#finishTurn" ).hide();
    $( '#stopRolling' ).hide();

    if (isActivePlayer) {
        $( "#rollDice" ).prop('disabled', false ).show();
        $( "[type=checkbox]" ).show();
    }
}
function showDice (dice) {
    var dieDiv = document.querySelectorAll('div[id^=die]');
    currentDice = dice;

    $('#rollSet img[id^=diceImg]').remove();
    $(dieDiv).hide();
    $('[type=checkbox]').prop("checked", false);

    if (isActivePlayer) {
        $( "#setKeepers" ).show();
        $( "#stopRolling" ).show();
    }

    dice.forEach(function (die, index) {
        var diceImg = new Image(50, 50);
        diceImg.id = 'diceImg' + die.value;
        $('#' + die.id).append(diceImg).show();
    });
}

function showKeepers (keepers) {
    var keeperDiv = document.querySelectorAll('div[id^=keeper]');
    currentKeepers = keepers;
    $('#keeperSet img[id^=diceImg]').remove();
    $(keeperDiv).hide();
    keepers.forEach(function (keeper, index) {
        var diceImg = new Image(50, 50);
        diceImg.id = 'diceImg' + keeper.value;
        $('#keeper' + index).append(diceImg).show();
    });
}

function showChat (chat) {
    console.log("RETURNED: " + chat);
}

function showScoreOptions (scorecard) {
    $( '#rollDice' ).prop('disabled', true );
    $( '#setKeepers' ).prop('disabled', true );
    $( '#stopRolling' ).prop('disabled', true );

    if (isActivePlayer) {
        $('#submitScore').removeAttr('disabled').show();
    }
    console.log("RETURNED: " + scorecard);
}

function updateScorecard (scorecard) {
    if (isActivePlayer) {
        $( '#submitScore' ).prop('disabled', true );
        $( '#finishTurn' ).removeAttr('disabled').show();
    }
    console.log("FINISH TURN");
}

//___________________________Senders_________________________________

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
        remainingDice.forEach(function( die, index ) {
            if (die.id === keeperId) {
                remainingDice.splice(index, 1);
                currentKeepers.push(die);
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

function stopRolling () {
    stompClient.send("/app/turn/stopRoll");
}

function submitScore () {
    stompClient.send("/app/turn/submitScore", {}, JSON.stringify());
}

function finishTurn () {
    $( "#rollDice" ).hide();
    $( "#setKeepers" ).hide();
    $( "#stopRolling" ).hide();
    $( "#submitScore" ).hide();
    $( "#finishTurn" ).hide();

    stompClient.send("/app/turn/finish");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#rollDice" ).click(function() { rollDice(); });
    $( "#setKeepers" ).click(function() { setKeepers(); });
    $( "#stopRolling" ).click(function() { stopRolling(); });
    $( "#submitScore" ).click(function() { submitScore(); });
    $( "#finishTurn" ).click(function() { finishTurn(); });
    $( "#sendChat" ).click(function() { sendChat($('#chatMsg').val()); });
});