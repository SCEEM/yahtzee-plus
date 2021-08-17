package com.yahtzee.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import java.util.ArrayList;

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
  public JSONObject rollDice(@RequestBody boolean rollKeepers) {
    System.out.println("rollKeepers? " + rollKeepers);
    JSONObject rollObject = new JSONObject();
    Player currentPlayer = game.getCurrentActivePlayer();
    rollObject.put("dice", currentPlayer.rollDice());
    rollObject.put("canRoll", currentPlayer.canRollDice());
    return rollObject;
  }


  /**
   * Change the state of the specified dice to KEPT, and
   * return the updated dice.
   *
   * @return the dice as an ArrayList<{@link Die}>
   */
  @MessageMapping("/roll/keep")
  @SendTo("/topic/keepers")
  public JSONObject setKeepers(@RequestBody ArrayList<Die> keepers) {
    JSONObject rollObject = new JSONObject();
    Player currentPlayer = game.getCurrentActivePlayer();
    rollObject.put("dice", currentPlayer.keepDice(keepers));
    rollObject.put("canRoll", currentPlayer.canRollDice());
    // get current dice from current Turn
    return rollObject;
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
