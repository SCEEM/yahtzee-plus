package com.example.web;

import com.example.turn.Roll;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping("/roll")
  public ArrayList<Integer> rollDice(Model model) {
    Roll roll = new Roll();
    return roll.rollDice();
  }
}
