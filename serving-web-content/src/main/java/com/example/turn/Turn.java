package com.example.turn;

/**
 * This class represents a turn taken by a {@link com.example.player.Player}
 */
public class Turn {

  int numRollsTaken;
  Dice dice;


  /**
   * Constructor
   */
  public Turn() {
    numRollsTaken = 0; // no rolls taken yet for a new Turn
    dice = new Dice(); // initialize dice
  }

  public void createTurn() {

  }

  public Dice getDice() {
    return this.dice;
  }

}