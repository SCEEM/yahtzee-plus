package com.yahtzee.web;

import com.yahtzee.game.Game;
import com.yahtzee.player.Player;
import com.yahtzee.turn.Die;
import com.yahtzee.turn.Roll;
import com.yahtzee.turn.Turn;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains the endpoints needed to perform
 * actions as part of a {@link com.yahtzee.turn.Turn}
 */
@Controller
@MessageMapping("/turn")
public class TurnController {

  @Autowired
  ApplicationContext ctx;

  @Autowired
  Game game;

  /**
   * Generate a new {@link Roll} as part of the current {@link Turn}
   */
  @MessageMapping("/roll")
  @SendTo("/topic/roll")
  public ArrayList<Die> rollDice() {
    Player currentPlayer = game.getCurrentActivePlayer();
    ArrayList<Die> dice;

    // TODO: get Turn from Player or Game
    //if (currentPlayer.canRollDice()) {
    currentPlayer.startTurn(); // TODO: shouldn't have to do this here
    Turn currentTurn = currentPlayer.getMyTurn();

    Roll roll = currentTurn.newRoll();
    dice = roll.rollDice(currentTurn.getDice());

//    Turn turn = new Turn();
//    Roll roll = turn.newRoll();
//    ArrayList<Die> dice = roll.rollDice(turn.getDice());

    return dice;
  }


  /**
   * Change the state of the specified dice to KEPT, and
   * return the updated dice.
   *
   * @return the dice as an ArrayList<{@link Die}>
   */
  @MessageMapping("/roll/keep")
  @SendTo("/topic/keepers")
  public ArrayList<Die> setKeepers(@RequestBody ArrayList<Die> keepers) {
    List<String> dieIdsToKeep = keepers.stream().map(k -> k.getId()).collect(Collectors.toList());

    // get current dice from current Turn
    Turn currentTurn = game.getCurrentActivePlayer().getMyTurn();

    // get latest roll as part of the Turn
    Roll latestRoll = currentTurn.getCurrentRoll();
    ArrayList<Die> latestDice = latestRoll.getDice();

    // update die status
    for (Die die : latestDice) {
      if (dieIdsToKeep.contains(die.getId())) {
        die.setStatus(Die.Status.KEEPER);
      }
    }

    // save dice status to the Roll
    currentTurn.finishRoll(latestDice);

    // TODO: remove
    System.out.println("Final status of dice in roll:");
    for (Die die : latestRoll.getDice()) {
      System.out.println(die.toString());
    }

    return keepers;
  }


  /**
   * Finish the current {@link Player}'s turn.
   *
   */
  @MessageMapping("/stopRoll")
  @SendTo("/topic/loadScorecard")
  public JSONArray stopRoll() {
    Player activePlayer = game.getCurrentActivePlayer();
    return activePlayer.getPossibleScores();
  }

  /**
   *
   *
   */
  @MessageMapping("/submitScore")
  @SendTo("/topic/updateScorecard")
  public int[] submitScore(int rowNumber) {
    System.out.println("ROW SELECTED: " + rowNumber);
    game.getCurrentActivePlayer().setScore(rowNumber);
    return game.getCurrentActivePlayer().getScorecard();
  }

  /**
   * Complete the current {@link Player}'s turn, and assign
   * the next active player in the {@link Game}.
   *
   * @return
   */
  @MessageMapping("/finish")
  @SendTo("/topic/activePlayer")
  public JSONObject finishTurn() {
    game.currentActivePlayerEndsTurn();
    game.assignNextActivePlayer();
    return getActivePlayerJSON();
  }

  /**
   * Complete the given {@link Player}'s turn.
   *
   */
  @MessageMapping("/getActivePlayer")
  @SendTo("/topic/activePlayer")
  public JSONObject getActivePlayer() {
    return getActivePlayerJSON();
  }

  private JSONObject getActivePlayerJSON() {
    JSONObject playerJson = new JSONObject();
    Player currentActivePlayer = game.getCurrentActivePlayer();
    playerJson.put("playerId", currentActivePlayer.getPlayerId());
    playerJson.put("scorecard", currentActivePlayer.getScorecard());
    return playerJson;
  }

}
