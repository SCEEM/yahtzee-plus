package com.yahtzee.turn;

import java.util.ArrayList;

/**
 * This class represents a turn taken by a {@link com.yahtzee.player.Player}
 */
public class Turn {

  static final int MAX_ROLLS = 3;
  ArrayList<Die> dice;  // a reference to the latest dice
  ArrayList<Roll> rolls;
  Roll currentRoll;

  /**
   * Constructor.
   */
  public Turn() {
    dice = createDice(); // initialize dice
    rolls = new ArrayList<>();
  }

  /**
   * Initialize the 5 {@link Die} objects for use in the Turn.
   *
   * @return dice as an ArrayList<Die>
   */
  private ArrayList<Die> createDice() {
    ArrayList<Die> dice = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      dice.add(new Die(i));
    }

    return dice;
  }

  /**
   * Get the dice used for this Turn.
   *
   * @return the dice as an ArrayList<Die>
   */
  public ArrayList<Die> getCurrentDice() {
    if (rolls.isEmpty()) {
      // create new dice
      return this.dice;
    } else {
      // get dice from latest roll
      return currentRoll.getDice();
    }
  }

  /**
   * Get the dice used for this Turn.
   *
   * @return the dice as an ArrayList<Integer>
   */
  public ArrayList<Integer> getDiceValues() {
    ArrayList<Integer> diceValues = new ArrayList<>();
    (this.dice).forEach(die -> diceValues.add(die.value));
    return diceValues;
  }

  /**
   * Get the dice used for this Turn.
   *
   * @return the dice as an ArrayList<Integer>
   */
  public ArrayList<Die> rollKeepers(ArrayList<Die> keepers) {
    ArrayList<Die> rolledKeepers = new ArrayList<>();
    if (!keepers.isEmpty()) {
      this.dice.forEach((die) -> {
        System.out.println(die);
        if (keepers.stream().anyMatch(keeper -> keeper.id.equals(die.id))) {
          die.roll();
          rolledKeepers.add(die);
        };
      });
    }
    return rolledKeepers;
  }

  /**
   * Hold onto a reference to the latest Roll.
   *
   * @param currentRoll the current roll
   */
  public void setCurrentRoll(Roll currentRoll) {
    this.currentRoll = currentRoll;
  }

  /**
   * Get the current {@link Roll} as part of this Turn.
   *
   * @return the current Roll
   */
  public Roll getCurrentRoll() {
    return this.currentRoll;
  }

  /**
   * Save the latest dice status to the Roll.
   *
   * @param dice the final dice
   */
  public void finishRoll(ArrayList<Die> dice) {
    this.currentRoll.setDice(dice);
  }

  /**
   * Check if a roll can be taken.
   *
   * @return true if allowed, false if not
   */
  public boolean canRoll() {
    return rolls.size() < MAX_ROLLS;
  }


  /**
   * Create a new {@link Roll} for this Turn.
   *
   * @return the new {@link Roll}
   */
  public Roll newRoll() {
    Roll roll = new Roll();
    rolls.add(roll);  // add to list of rolls
    this.setCurrentRoll(roll); // this is now the currentRoll
    return roll;
  }

}