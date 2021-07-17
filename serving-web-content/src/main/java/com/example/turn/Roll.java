package com.example.turn;

import java.util.ArrayList;
import java.util.Random;

/**
 * A roll is taken as part of a {@link Turn}
 */
public class Roll {

  public int numDice;

  /**
   * Constructor
   */
  public Roll() {

  }

  /**
   * Get random values for 5 dice.
   * TODO: update to roll 1-5 dice
   *
   * @return a list of dice values
   */
  public ArrayList<Integer> rollDice() {
    Random random = new Random();
    int min = 1;
    int max = 6;

    ArrayList<Integer> dice = new ArrayList<Integer>();
    for (int i = 0; i < 5; i++) {
      dice.add(random.nextInt(max - min) + min);
    }

    return dice; // TODO: use Dice
  }

  private void selectKeepers() {

  }


  /**
   * Inner class representing the 1-5 dice as part of a Roll.
   */
  class Dice {

    /*
     * call dice methods from here
     */
    private void roll() {
      // Dice.rollDice();
    }

    /*
     * get random roll results from here
     */
    private int rollDice() {
      return 0;
    }
  }
}
