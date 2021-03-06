package com.yahtzee.turn;

import java.util.Random;

/**
 * Represents a single die in the dice.
 */
public class Die {

  public String id;
  public int value;
  public Status status;

  public enum Status {
    ACTIVE,
    KEEPER
  }

  /**
   * Empty constructor.
   */
  public Die() {
    super();
  }

  /**
   * Create a new Die.
   */
  public Die(int id) {
    this.id = "die" + id;
    this.status = Status.ACTIVE;
  }

  /**
   * Roll a single die.
   */
  public void roll() {
    Random random = new Random();
    int min = 1; // min is inclusive
    int max = 7; // max is exclusive

    this.value = random.nextInt(max - min) + min;
  }

  /**
   * Get the value of this Die.
   *
   * @return the value as an integer
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Set this die's status.
   *
   * @param status a Status
   */
  public void setStatus(Status status) {
    this.status = status;
  }

  /**
   * Get this die's status.
   *
   * @return the Status
   */
  public Status getStatus() {
    return this.status;
  }

  /**
   * Get this die's id.
   *
   * @return the id
   */
  public String getId() {
    return this.id;
  }

  @Override
  public String toString() {
    return "id: " + this.getId() + ", value: " + this.getValue() + ", status: " + this.getStatus();
  }

}
