'use strict';

let stompClient = null,
    playerName = null,
    currentDice = [],
    activePlayerId,
    rollCount = 0,
    isActivePlayer = false,
    scorecardMap = {};

//______________________Game Connect/Disconnect Functions_____________

function joinGameAndCreateBoard(){
    $('#welcomePage').hide();
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
        stompClient.subscribe('/topic/roll', function (rollInformation) {
            showDice(JSON.parse(rollInformation.body));
        });
        stompClient.subscribe('/topic/keepers', function (rollInformation) {
            showDiceAndKeepers(JSON.parse(rollInformation.body));
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
        newUserMessage();
        stompClient.send("/app/getPlayerList");
        stompClient.send("/app/turn/getActivePlayer");
    });
    console.log("Connected");
}

function disconnect() {
    // remove player from game
    var id = $('#playerId').text();
    console.log("Disconnecting player " + id);
    stompClient.send("/app/disconnect", {}, id);
    if (isActivePlayer) {
        finishTurn();
    }

    sendSystemMessage(playerName + " has left the game");

    // disconnect this player
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

function endGameAndShowWinner(winner){
    console.log("The Game is Over");
    console.log("We have a winner");
    console.log(winner);
    $("#contentPage").hide();
    var winnerIdElement =  document.createElement('span');
    var userIdText = document.createTextNode("Player number " + (winner.playerId).toString() + " is the winner");
    // var userScoreText = document.createTextNode((winner.score));
    winnerIdElement.append(userIdText); // adds message name to 'span' element
    $("#winnerDiv").append(winnerIdElement);
    $("#winnerPage").show();
}

//___________________________Receivers_________________________________

function setActivePlayer (activePlayer) {
    console.log(activePlayer);
    if ((activePlayer.winner).toString() === "yes"){
        // the current player is the winner, game is over
        console.log("got a winner");
        endGameAndShowWinner(activePlayer);
        return;
    }
    console.log("continuing to active player");
    isActivePlayer = (activePlayer.playerId).toString() === $('#playerId').text();
    activePlayerId = activePlayer.playerId;
    currentDice = [];
    rollCount = 0;

    $( "#rollDice" ).hide();
    $('.dice:checkbox').hide();
    $( "#setKeepers" ).hide();
    $( "#submitScore" ).attr('disabled', 'disabled').hide();
    $( "#finishTurnDiv" ).hide();
    $( '#stopRolling' ).hide();
    $('[class=scoreCheckboxes]').hide();
    $('#rollKeepersDiv').hide();
    $('.rollLabel').hide();
    $('#keeperSet img[id^=diceImg]').remove();
    $('#rollSet img[id^=diceImg]').remove();
    $('img[id^=diceSmallImg]').remove();
    $('#activePlayer').text('Player #' + activePlayerId);

    if (isActivePlayer) {
        $( "#rollDice" ).prop('disabled', false ).show();
        $( "#stopRolling" ).prop('disabled', false ).show();
        $( ".currentScore" ).hide();
        $( "#activePlayer" ).hide();
        $('#you').css('float', 'right');
        $('#isActivePlayer').show();
        // system message
        // current bug: this message will be re-sent every time a new player connects
        if (isActivePlayer) {
            sendSystemMessage(playerName + " is the active player");
        }
    } else {
        $('#you').css('float', 'left');
        $( ".currentScore" ).show();
        $( "#activePlayer" ).show();
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



function showDice(rollInformation) {
    let dieDiv = document.querySelectorAll('div[id^=die]'),
        canRoll = rollInformation["canRoll"],
        dice = rollInformation["dice"];
    currentDice = dice;

    $('#rollSet img[id^=diceImg]').remove();
    $(dieDiv).hide();
    $('#rollLabel' + rollCount).show();

    if (isActivePlayer) {
        $('#rollKeepersDiv').show();
        $( "#setKeepers" ).show().prop('disabled', false );
        $( "#stopRolling" ).show();
        $('.dice:checkbox').prop("checked", false).show();
    }

    if (!canRoll) {
        $( "#rollDice" ).prop('disabled', true );
        $('#rollKeepersDiv').hide();
    }
    // creating string for system message
    let str = ""

    dice.forEach(function (die, index){
        if (die.status !== "KEEPER") {
            showDie(die);
            showSmallDie(die);
            // creating string for system message
            if (index == dice.length - 1){
                str += die.value + "";
            } else {
                str += die.value + ", ";
            }
        }
    });
    rollCount++;

    // system message
    if (isActivePlayer) {
        sendSystemMessage(playerName + " rolled: " + str);
    }
}

function showDiceAndKeepers(rollInformation) {
    let dieDiv = document.querySelectorAll('div[id^=die]'),
        keeperDiv = document.querySelectorAll('div[id^=keeper]'),
        dice = rollInformation["dice"];
    currentDice = dice;

    $('.dice:checkbox').hide();
    $('#rollSet img[id^=diceImg]').remove();
    $('#keeperSet img[id^=diceImg]').remove();
    $(dieDiv).hide();
    $(keeperDiv).hide();

    dice.forEach(function (die, index) {
        if (die.status === "KEEPER") {
            showKeeper(die, index);
        } else {
            showDie(die);
        }
    });
}

function showDie (die) {
    let diceImg = new Image(50,50);
    diceImg.id = 'diceImg' + die.value;
    $('#' + die.id).append(diceImg).show();
}

function showSmallDie (die) {
    let diceImg = new Image(50,50);
    diceImg.id = 'diceSmallImg' + die.value;
    $('#rollSet'+ rollCount +' > .'+die.id).append(diceImg).show();
}

function showKeeper (keeper, index) {
    var diceImg = new Image(50, 50);
    diceImg.id = 'diceImg' + keeper.value;
    $('#keeper' + index).append(diceImg).show();
}

function showScoreOptions (possibleScores) {
    $( '#rollDice' ).prop('disabled', true );
    $( '#setKeepers' ).prop('disabled', true );
    $( '#stopRolling' ).hide();

    if (isActivePlayer) {
        $('#submitScore').show();
        // handle button click
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
        $( '#finishTurnDiv' ).removeAttr('disabled').show();
    }

    scorecardMap[activePlayerId] = scorecard;

    (scorecard).forEach((value, index) => {
        let scoreLabel = "#scoreVal" + index,
            currentScore = "#currentScoreVal" + index;
        if (value !== -1) {
            $(scoreLabel+"> .scoreValue").text(value);
            if (isActivePlayer) {
                $(currentScore).text(value);
            }
        } else {
            $(scoreLabel+"> .scoreValue").text("");
        }
        $(scoreLabel).removeClass('scoreValueEnabled').addClass('scoreValueDisabled');
    });
    $("#playerRow" + activePlayerId + "> .score").text(scorecard[scorecard.length-1])

    // system message
    if (isActivePlayer) {
        sendSystemMessage(playerName + " scored " + scorecard[scorecard.length-1] + " point(s)");
    }
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

//___________________________Senders_________________________________
function rollDice() {
    let rollKeepers = $('#rollKeepers:checkbox:checked').length;
    stompClient.send("/app/turn/roll", {}, JSON.stringify(rollKeepers > 0));
}

function joinGame (player) {
    var playerId = player.playerId;
}

function setKeepers () {
    let remainingDice = currentDice,
        currentKeepers = [];
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
    // creating string for system message
    let str = ""
    currentKeepers.forEach(function(die, index){
        if (index == currentKeepers.length - 1){
            str += die.value + "";
        } else {
            str += die.value + ", ";
        }
    });
    // system message
    if (isActivePlayer) {
        sendSystemMessage("These are " + playerName + "'s current keepers: " + str);
    }
}

function stopRolling () {
    stompClient.send("/app/turn/stopRoll");
    // system message
    if (isActivePlayer){
        sendSystemMessage(playerName + " has finished rolling.");
    }
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
}

function finishTurn () {
    $( "#rollDice" ).hide();
    $( "#setKeepers" ).hide();
    $( "#stopRolling" ).hide();
    $( "#submitScore" ).hide();
    $( "#finishTurnDiv" ).hide();
    var dieDiv = document.querySelectorAll('div[id^=die]');
    $(dieDiv).hide();
    $('#isActivePlayer').hide();

    stompClient.send("/app/turn/finish");
    // system message
    if (isActivePlayer) {
        sendSystemMessage(playerName + " finished their turn.");
    }
}

function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);
    console.log(message);
    var messageElement = $('<li>').addClass('event-data');

	if (message.type === 'NEW_USER') {

        message.content = message.sender + ' has joined the game';
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

function sendMessage() {
    var messageContent = $("#chatMessage").val();
    console.log(messageContent);
	if (messageContent && stompClient) {
		var chatMessage = {
			content : messageContent,
            type : 'CHAT',
            sender : playerName
		};

		stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
		$('#chatMessage').val(''); //reset user input
	}
}

// This function will alert the players that a new user has joined the game
function newUserMessage() {
    var msg = {
        sender : playerName,
        content : "new user",
        type : 'NEW_USER'
    };
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(msg));
    console.log("Sent new user message");
}

// This function will alert the players of a message from the game system
function sendSystemMessage(msg) {
    var sysMessage = {
        sender : "Game",
        content : msg,
        type : 'SYSTEM'
    };
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(sysMessage));
    console.log("sent system message: " + msg);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#joinGame" ).click(function() { joinGameAndCreateBoard(); });
    $( "#connect" ).click(function() { connect(); }); 
    $( "#rollDice" ).click(function() { rollDice(); });
    $( "#setKeepers" ).click(function() { setKeepers(); });
    $( "#sendChat" ).click(function() { sendMessage(); });
    $( "#stopRolling" ).click(function() { stopRolling(); });
    $( "#submitScore" ).click(function() { submitScore(); });
    $( "#finishTurn" ).click(function() { finishTurn(); });
    $( "#leaveGame" ).click(function() { disconnect(); });
    $('.scoreCheckboxes').click(function () { $('#submitScore').removeAttr('disabled');});
});