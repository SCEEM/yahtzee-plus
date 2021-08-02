package com.yahtzee.web;

import com.yahtzee.game.Game;
import com.yahtzee.player.Player;
import com.yahtzee.turn.Die;
import com.yahtzee.turn.Roll;
import com.yahtzee.turn.Turn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    // TODO: get Turn from Player or Game?
    Turn turn = new Turn();

    Roll roll = turn.newRoll();
    ArrayList<Die> dice = roll.rollDice(turn.getDice());

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
    // TODO: get current dice from current Turn

    // change selected die status to KEPT
    for (Die d : keepers) {
      d.setStatus(Die.Status.KEPT);
      System.out.println(d.getValue()); // TODO: remove
    }

    // TODO: update the Turn's current dice, and save the dice to the Roll

    return keepers;
  }


  /**
   * Complete the given {@link Player}'s turn.
   *
   */
  @MessageMapping("/finish")
  public String finishTurn(Model model) {
    Game game = ctx.getBean(Game.class);
    model.addAttribute("playerList", game.getPlayerList());
    model.addAttribute("scoreList", game.getScoreList());
    game.assignNextActivePlayer();
    return "index";
  }

}
