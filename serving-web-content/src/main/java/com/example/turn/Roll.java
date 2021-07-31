package com.example.turn;

import java.util.ArrayList;

/**
 * A roll is taken as part of a {@link Turn}.
 */
public class Roll {

  ArrayList<Die> dice;  // the dice for this Roll

  /**
   * Constructor.
   */
  public Roll() {
    dice = new ArrayList<>();
  }

  /**
   * Generate new values for the dice that are not kept.
   *
   * @param dice an ArrayList<Die>
   * @return the roll as an ArrayList<Die>
   */
  public ArrayList<Die> rollDice(ArrayList<Die> dice) {
    for (Die die : dice) {
      if (die.status != Die.Status.KEPT) {
        die.roll();
      }
    }

    setDice(dice);

    return dice;
  }

  /**
   * Hold onto the current dice for the Roll.
   *
   * @param dice an ArrayList<Die>
   */
  public void setDice(ArrayList<Die> dice) {
    this.dice = dice;
  }

}
