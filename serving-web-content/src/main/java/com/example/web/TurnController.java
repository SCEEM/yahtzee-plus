package com.example.web;

import com.example.turn.Roll;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * This class contains the endpoints needed to perform
 * actions as part of a {@link com.example.turn.Turn}
 */
@RestController
@RequestMapping("/turn")
public class TurnController {

  @PostMapping("/turn/roll")
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
}
