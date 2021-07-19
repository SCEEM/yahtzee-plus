package com.example.web;

import com.example.game.Game;
import com.example.player.Player;
import com.example.turn.Roll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
   *
   * @param model the Model
   * @return an array of dice values TODO: update
   */
  @PostMapping("/roll")
  public String rollDice(Model model) {
    Roll roll = new Roll();
    ArrayList<Integer> rollReturned = roll.rollDice();
    model.addAttribute("dice_0", rollReturned.get(0).toString());
    model.addAttribute("dice_1", rollReturned.get(1).toString());
    model.addAttribute("dice_2", rollReturned.get(2).toString());
    model.addAttribute("dice_3", rollReturned.get(3).toString());
    model.addAttribute("dice_4", rollReturned.get(4).toString());
    return "index";
  }

  /**
   * Keep the specified dice.
   *
   * @param values a list of the values to keep
   */
  @PostMapping("/roll/keep")
  public String setKeepers(@RequestParam(name = "values", required = false) List<Integer> values,
                         Model model) {
    model.addAttribute("keepers_msg", "Successfully kept: " + values.toString());
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
    model.addAttribute("lastPlayerScore", game.getCurrentActivePlayer().getTotalScore());
    game.assignActivePlayer();
    return "index";
  }

}
