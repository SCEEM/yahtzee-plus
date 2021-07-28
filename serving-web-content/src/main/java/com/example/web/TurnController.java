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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the endpoints needed to perform
 * actions as part of a {@link com.example.turn.Turn}
 */
@Controller
@RequestMapping("/turn")
public class TurnController {

  @Autowired
  ApplicationContext ctx;

  /**
   * Generate a new {@link Roll}
   */
  @PostMapping("/roll")
  public String rollDice(Model model) {
    Game game = ctx.getBean(Game.class);
    Roll roll = new Roll();
    Dice newDice = new Dice(); // TODO: get dice from Turn
    ArrayList<Die> dice = roll.roll(newDice.getDice()); // TODO: use Dice class or ArrayList?

    dice.get(0).setStatus(Die.Status.KEPT); // TODO remove
    model.addAttribute("dice", dice);
    model.addAttribute("playerList", game.getPlayerList());
    model.addAttribute("scoreList", game.getScoreList());
    return "index";
  }

  /**
   * Keep the specified dice.
   * TODO
   */
  @PostMapping("/roll/keep")
  public String setKeepers(//@RequestParam String dice,
      @ModelAttribute(value="dice") ArrayList<Die> dice,
                           Model model) {

    System.out.println(dice);
    // TODO: change state of selected dice to KEPT

    model.addAttribute("dice", dice);
    return "index";
  }

  /**
   * Complete the given {@link Player}'s turn.
   *
   *
   */
  @PostMapping("/finish")
  public String finishTurn(Model model) {
    Game game = ctx.getBean(Game.class);
    model.addAttribute("playerList", game.getPlayerList());
    model.addAttribute("scoreList", game.getScoreList());
    game.assignActivePlayer();
    return "index";
  }

}
