package com.example.turn;

import java.util.ArrayList;

/**
 * Dice contains 5 {@link Die}s used as part of a turn/roll.
 */
public class Dice {

  ArrayList<Die> dice = new ArrayList<>();

  public Dice() {
    // create 5 new dies
    for (int i = 0; i < 5; i++) {
      dice.add(new Die(i));
    }
  }

  public ArrayList<Die> getDice() {
    return dice;
  }

  public ArrayList<Die> getKeptDice() {
    ArrayList<Die> kept = new ArrayList<>();
    for (Die die : dice) {
      if (die.getStatus() == Die.Status.KEPT) {
        kept.add(die);
      }
    }
    return kept;
  }

}
