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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.ArrayList;
import java.util.List;

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

//    dice.get(0).setStatus(Die.Status.KEPT); // TODO remove
//    model.addAttribute("dice", dice);
//    model.addAttribute("playerList", game.getPlayerList());
//    model.addAttribute("scoreList", game.getScoreList());
//    return "index";

//    ArrayList<Integer> rollReturned = roll.rollDice();
//    return rollReturned;
  }

  /**
   * Keep the specified dice.
   * TODO
   */
  @MessageMapping("/roll/keep")
  @SendTo("/topic/game")
  public Model setKeepers(@RequestParam(name = "values", required = false) List<Integer> values,
                         Model model) {
    model.addAttribute("keepers_msg", "Successfully kept: " + values.toString());
    return model;
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
    game.assignActivePlayer();
    return "index";
  }

}
