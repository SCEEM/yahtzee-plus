package com.example.turn;

import java.util.ArrayList;
import java.util.Random;

/**
 * A roll is taken as part of a {@link Turn}.
 */
public class Roll {

  /**
   * Constructor
   */
  public Roll() {

  }

  /**
   * Get random values for the given number of dice.
   *
   * @param numToRoll number [1,5] of dice to roll
   * @return a list of dice values
   */
  public ArrayList<Die> rollDice(int numToRoll) {
    Random random = new Random();
    int min = 1; // min is inclusive
    int max = 7; // max is exclusive

    ArrayList<Die> dice = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      dice.add(new Die(i));
    }

    return dice; // TODO: use Dice
  }

  /**
   * Generate new values for the dice that are not kept.
   *
   * @param dice
   * @return
   */
  public ArrayList<Die> roll(ArrayList<Die> dice) {
    for (Die die : dice) {
      if (die.status != Die.Status.KEPT) {
        die.roll();
      }
    }

    return dice;
  }

  public Dice rollDice(Dice dice) {
    for (Die die : dice.getDice()) {
      if (die.status != Die.Status.KEPT) {
        die.roll();
      }
    }

    return dice;
  }


  /**
   * Set the selected dice as keepers
   */
  public void setKeepers() {

  }

}
