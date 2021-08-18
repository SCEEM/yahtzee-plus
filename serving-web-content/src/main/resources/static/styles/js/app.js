'use strict';

var stompClient = null,
    playerName = null,
    currentKeepers = [],
    currentDice = [],
    activePlayerId,
    playerId,
    scoreList,
    playerList,
    isActivePlayer = false;


//______________________Game Connect/Disconnect Functions_____________

function joinGameAndCreateBoard(){
    $('#welcome-page').hide();
    $('#contentPage').show();
    connect();
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    playerName = $("#playerName").val();
    console.log("player name is " + name);

    var bannerImg = new Image();
    bannerImg.id = 'bannerImg';
    $('#banner').append(bannerImg);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/activePlayer', function (player) {
            setActivePlayer(JSON.parse(player.body));
        });
        stompClient.subscribe('/topic/roll', function (dice) {
            showDice(JSON.parse(dice.body));
        });
        stompClient.subscribe('/topic/keepers', function (keepers) {
            showKeepers(JSON.parse(keepers.body));
        });
        stompClient.subscribe('/topic/chat', function (msg){
            onMessageReceived(msg)
        });
        stompClient.subscribe('/topic/loadScorecard', function (scorecard) {
            showScoreOptions(JSON.parse(scorecard.body));
        });
        stompClient.subscribe('/topic/updateScorecard', function (scorecard) {
            console.log("SCORECARD: " + JSON.parse(scorecard.body));
            updateScorecard(JSON.parse(scorecard.body));
        });
        stompClient.subscribe('/topic/updatePlayerList', function (playerList) {
            console.log("HERE: " + playerList);
            updatePlayerList(JSON.parse(playerList.body));
        });
        stompClient.send("/app/getPlayerList");
        stompClient.send("/app/turn/getActivePlayer");
        newUserMessage();
        // sendSystemMessage("It is " + playerName + "'s turn.");
    });
    console.log("Connected");
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

//___________________________Receivers_________________________________

function setActivePlayer (activePlayer) {
    isActivePlayer = (activePlayer.playerId).toString() === $('#playerId').text();
    activePlayerId = activePlayer.playerId;
    currentKeepers = [];
    currentDice = [];

    $( "#rollDice" ).hide();
    $('[type=checkbox]').hide();
    $( "#setKeepers" ).hide();
    $( "#submitScore" ).hide();
    $( "#finishTurn" ).hide();
    $( '#stopRolling' ).hide();
    $('[class=scoreCheckboxes]').hide();
    $('#keeperSet img[id^=diceImg]').remove();
    $('#rollSet img[id^=diceImg]').remove();

    if (isActivePlayer) {
        $( "#rollDice" ).prop('disabled', false ).show();
        $( "#stopRolling" ).prop('disabled', false ).show();
    }

    (activePlayer.scorecard).forEach((value, index) => {
        let scoreLabel = "#scoreVal" + index;
        if (value !== -1) {
            $(scoreLabel+"> .scoreValue").text(value);
        } else {
            $(scoreLabel+"> .scoreValue").text("");
        }
        $(scoreLabel).removeClass('scoreValueEnabled').addClass('scoreValueDisabled');
    });
}

function showDice (dice) {
    var dieDiv = document.querySelectorAll('div[id^=die]');
    currentDice = dice;

    $('#rollSet img[id^=diceImg]').remove();
    $(dieDiv).hide();

    if (isActivePlayer) {
        $( "#setKeepers" ).show().prop('disabled', false );
        $( "#stopRolling" ).show();
        $('[type=checkbox]').prop("checked", false).show();
    }

    dice.forEach(function (die, index) {
        var diceImg = new Image(50, 50);
        diceImg.id = 'diceImg' + die.value;
        $('#' + die.id).append(diceImg).show();
    });
}

function showKeepers (keepers) {
    let keeperDiv = document.querySelectorAll('div[id^=keeper]'),
        remainingDice = currentDice;
    currentKeepers = keepers;
    $('#keeperSet img[id^=diceImg]').remove();
    $(keeperDiv).hide();

    keepers.forEach(function (keeper, index) {
        var diceImg = new Image(50, 50);
        diceImg.id = 'diceImg' + keeper.value;
        remainingDice.forEach(function( die, index ) {
            if (die.id === keeper.id) {
                remainingDice.splice(index, 1);
            }
        });
        $('#keeper' + index).append(diceImg).show();
    });

    showDice(remainingDice);
}

function showScoreOptions (possibleScores) {
    $( '#rollDice' ).prop('disabled', true );
    $( '#setKeepers' ).prop('disabled', true );
    $( '#stopRolling' ).prop('disabled', true );

    if (isActivePlayer) {
        $('#submitScore').removeAttr('disabled').show();
    } else {
        $('[class=scoreCheckboxes]').hide();
    }
    possibleScores.forEach((row, index) => {
        let scoreLabel = "#scoreVal" + index;
        if (row.availability) {
            if (isActivePlayer) {
                $(scoreLabel + "> .scoreCheckboxes").show();
                $(scoreLabel).removeClass('scoreValueDisabled').addClass('scoreValueEnabled');
            }
            $(scoreLabel+"> .scoreValue").text(row.score)
        }

    });
    console.log("RETURNED: " + scorecard);
}

function updateScorecard (scorecard) {
    if (isActivePlayer) {
        $( '#submitScore' ).prop('disabled', true );
        $( '#finishTurn' ).removeAttr('disabled').show();
    }

    (scorecard).forEach((value, index) => {
        let scoreLabel = "#scoreVal" + index;
        if (value !== -1) {
            $(scoreLabel+"> .scoreValue").text(value);
        } else {
            $(scoreLabel+"> .scoreValue").text("");
        }
        $(scoreLabel).removeClass('scoreValueEnabled').addClass('scoreValueDisabled');
    });
    $("#playerRow" + activePlayerId + "> .score").text(scorecard[scorecard.length-1])
}

function updatePlayerList (playerList) {
    console.log(playerList);
    let tableBody = $("table tbody");
    playerList.forEach(player => {
        let playerId = player.playerId,
            score = player.score,
            playerRow = document.getElementById("playerRow" + playerId),
            markup;
        if (playerRow !== null) {
            $("#playerRow" + playerId + "> .score").text(score)
        } else {
            markup = "<tr id='playerRow"+ playerId +"'><td>" + playerId + "</td><td class='score'>"
                + score +"</td></tr>";
            tableBody.append(markup);
        }
    });
}

function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);
    console.log(message);
    var messageElement = $('<li>').addClass('event-data');

	if (message.type === 'NEW_USER') {

        message.content = message.sender + ' has joined the game';
	// } else if (message.type === 'SYSTEM') {

        // var element = document.createElement('i');
        // var usernameElement = document.createElement('span');

        // var usernameText = document.createTextNode("Game"); // The user name is 'Game'
        
		// usernameElement.append(usernameText); // adds message name to 'span' element
        // element.append(usernameElement); // adds 'span' element to 'i' element
		// messageElement.append(element); // add 'i' element to the mainMessage
    } else if (message.type === 'CHAT') { // the message type is CHAT
        
		var element = document.createElement('i');
        var usernameElement = document.createElement('span');

        var usernameText = document.createTextNode(message.sender); // The user name is retreived from the message
        
		usernameElement.append(usernameText); // adds message name to 'span' element
        element.append(usernameElement); // adds 'span' element to 'i' element
		messageElement.append(element); // add 'i' element to the mainMessage
	}

	var textElement = document.createElement('p');
	var messageText = document.createTextNode(message.content);
	textElement.append(messageText);

	messageElement.append(textElement);

	$("#messageList").append(messageElement);
	$("#messageList").scrollTop($("#messageList")[0].scrollHeight);
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
    $( '#setKeepers' ).prop('disabled', true );
    $('.dice:checkbox:checked').each(function (index, checkElement) {
        var keeperId = $(this).parent().attr('id');
        remainingDice.forEach(function( die, index ) {
            if (die.id === keeperId) {
                currentKeepers.push(die);
            }
        });
    });
    stompClient.send("/app/turn/roll/keep", {}, JSON.stringify(currentKeepers));
    var str = ""
    currentKeepers.forEach(function(die, index){
        if (index == currentKeepers/length - 1){
            str += die.value + "";
        } else {
            str += die.value + ", ";
        }
    });
    sendSystemMessage("These are " + playerName + "'s current keepers: " + str);
}

function stopRolling () {
    stompClient.send("/app/turn/stopRoll");
    sendSystemMessage(playerName + " has finished rolling.");
}

function submitScore () {
    let ele = document.getElementsByName('scoreCheckboxes'),
        selectedVal = -1,
        i;
    for(i = 0; i < ele.length; i++) {
        if(ele[i].checked)
            selectedVal = ele[i].value;
    }
    stompClient.send("/app/turn/submitScore", {}, JSON.stringify(selectedVal));
    sendSystemMessage(playerName + " scored " + selectedVal + " this round.");
}

function finishTurn () {
    $( "#rollDice" ).hide();
    $( "#setKeepers" ).hide();
    $( "#stopRolling" ).hide();
    $( "#submitScore" ).hide();
    $( "#finishTurn" ).hide();
    var dieDiv = document.querySelectorAll('div[id^=die]');
    $(dieDiv).hide();

    stompClient.send("/app/turn/finish");
    sendSystemMessage(playerName + " finished their turn.");
}

function sendMessage() {
    var messageContent = $("#chatMessage").val();
    console.log(messageContent);
	if (messageContent && stompClient) {
		var chatMessage = {
            sender : playerName,
			content : messageContent,
			type : 'CHAT'
		};
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
		$('#chatMessage').val(''); //reset user input
	}
}

// This function will alert the players that a new user has joined the game
function newUserMessage(){
    var msg = {
        sender : playerName,
        content : "new user",
        type : 'NEW_USER'
    };
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(msg));
    console.log("Sent new user message");
}

// THis function will alert the players of a message from the game system
function sendSystemMessage(msg) {
    var sysMessage = {
        sender : "Game",
        content : msg,
        type : 'SYSTEM'
    };
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(sysMessage));
    console.log("sent system message: " + msg);
}

// click activity functions that are initialized once the document is loaded
$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#rollDice" ).click(function() { rollDice(); });
    $( "#setKeepers" ).click(function() { setKeepers(); });
    $( "#sendChat" ).click(function() { sendMessage(); });
    $( "#stopRolling" ).click(function() { stopRolling(); });
    $( "#submitScore" ).click(function() { submitScore(); });
    $( "#finishTurn" ).click(function() { finishTurn(); });
    $( "#joinGame" ).click(function() { joinGameAndCreateBoard(); });
});