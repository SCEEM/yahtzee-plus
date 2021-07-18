package com.example.player;

/**
 * This class represents a Yahtzee scorecard.
 */
public class ScoreCard {
  private boolean editMode;
  private int[] scores;
  int upperScore;
  int lowerScore;
  private int numYahtzeeBonuses;

  /**
   * Constructor
   */
  public ScoreCard() {
    upperScore = 0;
    lowerScore = 0;
    this.editMode = false;
    this.numYahtzeeBonuses = 0;

    int[] scores = new int[13];
    for (int i = 0; i < 13; i++) {
      scores[i] = -1; // TODO update
    }
  }

  /**
   * @param ind
   * @param points
   */
  public void saveScore(int ind, int points) {
    if (this.editMode == true) {
      scores[ind] = points;
    }
  }

  /**
   * @param ind
   * @return
   */
  public boolean getSectionAvailabilty(int ind) {
    boolean sectionAvailable = true;
    if (scores[ind] == -1) {
      sectionAvailable = false;
    }
    return sectionAvailable; // TODO update
  }

  /**
   * @return
   */
  public boolean boardFilled() {
    boolean filled = true;
    for (int i = 0; i < 13; i++) {
      if (scores[i] == -1) { // TODO update
        filled = false;
      }
    }
    return filled;
  }

  public int getScore(int ind) {
    return scores[ind];
  }

  public int getUpperScore() {

    for (int i = 0; i < 6; i++) {
      if (scores[i] != -1) {
        upperScore = upperScore + scores[i];
      }
    }

    // Bonus Score
    if (upperScore >= 63) {
      upperScore = upperScore + 35;
    }
    return upperScore;
  }

  public void yahtzeeBonusScore() {
    // probably need to check if valid yahtzee first
    if (numYahtzeeBonuses < 3) {
      numYahtzeeBonuses += 1;
    }
  }

  public int getLowerScore() {
    for (int i = 7; i < 13; i++) {
      if (scores[i] != -1) {
        lowerScore = lowerScore + scores[i];
      }
    }

    int yahtzeeBonus = numYahtzeeBonuses * 100;
    lowerScore = lowerScore + yahtzeeBonus;
    return lowerScore;
  }

  /**
   * @return
   */
  public int getTotalScore() {
    return upperScore + lowerScore;
  }

  public void checkScoreValidity() { //TODO, might need one for each section of scorecard

  }
}