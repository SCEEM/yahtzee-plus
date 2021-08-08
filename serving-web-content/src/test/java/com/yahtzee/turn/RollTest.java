package com.yahtzee.turn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RollTest {

  ArrayList<Die> dice;

  @BeforeEach
  public void setup() {
    dice = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      dice.add(new Die(i));
    }
  }

  @Test
  public void testNewRoll() {
    Roll roll = new Roll();
    roll.rollDice(dice);
    for (Die die : dice) {
      assertEquals(die.getStatus(), Die.Status.ACTIVE);
    }
  }

  @Test
  public void testRollWithKeepers() {
    // roll dice once
    Roll roll = new Roll();
    roll.rollDice(dice);

    // set two keepers
    dice.get(0).setStatus(Die.Status.KEEPER);
    dice.get(3).setStatus(Die.Status.KEEPER);

    // hold onto keeper values
    int die0value = dice.get(0).getValue();
    int die3value = dice.get(3).getValue();

    // roll dice again
    roll.rollDice(dice);

    // test that keepers weren't re-rolled
    assertEquals(dice.get(0).getStatus(), Die.Status.KEEPER);
    assertEquals(dice.get(0).getValue(), die0value);
    assertEquals(dice.get(1).getStatus(), Die.Status.ACTIVE);
    assertEquals(dice.get(2).getStatus(), Die.Status.ACTIVE);
    assertEquals(dice.get(3).getStatus(), Die.Status.KEEPER);
    assertEquals(dice.get(3).getValue(), die3value);
    assertEquals(dice.get(4).getStatus(), Die.Status.ACTIVE);
  }
}
