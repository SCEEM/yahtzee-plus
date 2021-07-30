package com.example.web;

import com.example.game.Game;
import com.example.player.Player;
import com.example.turn.Dice;
import com.example.turn.Die;
import com.example.turn.Roll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

/**
 * This class contains the endpoints needed to perform
 * actions as part of a {@link com.example.turn.Turn}
 */
@Controller
@MessageMapping("/turn")
public class TurnController {

  @Autowired
  ApplicationContext ctx;


  /**
   * Generate a new {@link Roll}
   */
  @MessageMapping("/roll")
  @SendTo("/topic/roll")
  public ArrayList<Die> rollDice() {
    Game game = ctx.getBean(Game.class);
    Roll roll = new Roll();
    Dice newDice = new Dice(); // TODO: get dice from Turn
    ArrayList<Die> dice = roll.roll(newDice.getDice()); // TODO: use Dice class or ArrayList?
    return dice;
  }

  /**
   * Keep the specified dice.
   * TODO
   */
  @MessageMapping("/roll/keep")
  @SendTo("/topic/keepers")
  public ArrayList<Die> setKeepers(@RequestBody ArrayList<Die> keepers) {
    System.out.println("SET KEEPERS: " + keepers);
    return keepers;
  }

  /**
   * Complete the given {@link Player}'s turn.
   *
   */
  @MessageMapping("/stopRoll")
  @SendTo("/topic/loadScorecard")
  public String stopRoll() {
    System.out.println("GET SCORECARD: ");
    return "SCORECARD";
  }

  /**
   * Complete the given {@link Player}'s turn.
   *
   */
  @MessageMapping("/submitScore")
  @SendTo("/topic/updateScorecard")
  public String submitScore() {
    System.out.println("GET SCORECARD: ");
    return "SCORE SUBMITTED";
  }

  /**
   * Complete the given {@link Player}'s turn.
   *
   */
  @MessageMapping("/finish")
  public String finishTurn() {
    Game game = ctx.getBean(Game.class);
    game.assignActivePlayer();
    return "index";
  }

}
