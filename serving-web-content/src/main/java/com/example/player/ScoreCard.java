package com.example.player;

import java.util.ArrayList;
import java.util.*;

/**
 * This class represents a Yahtzee scorecard.
 */
public class ScoreCard {
  private boolean editMode;
  private int[] scores;
  private ArrayList<Integer> diceValues; 
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

  /**  
   * @param ind
   * @return
  */
  public boolean isScoreValid(ArrayList diceValues, int ind) { 
    boolean validScore = false; 

    //place holders... will need inputs from UI 
    boolean scoreOnes = false; 
    boolean scoreTwos = false; 
    boolean scoreThrees = false; 
    boolean scoreFours = false; 
    boolean scoreFives = false; 
    boolean scoreSixes = false; 
    boolean score3OfAKind = false; 
    boolean score4OfAKind = false; 
    boolean scoreFullHouse = false; 
    boolean scoreSmStraight = false; 
    boolean scoreLgStraight = false; 
    boolean scoreYahtzee = false; 
    boolean scoreChance = false; 

    int numOnes = Collections.frequency(diceValues, 1);
    int numTwos = Collections.frequency(diceValues, 2);
    int numThrees = Collections.frequency(diceValues, 3);
    int numFours = Collections.frequency(diceValues, 4);
    int numFives = Collections.frequency(diceValues, 5);
    int numSixes = Collections.frequency(diceValues, 6);

    if (scoreOnes == true) { 
      if (numOnes > 0){
        return validScore = true;
      } 
    }

    if (scoreTwos == true) { 
      if (numTwos > 0){
        return validScore = true;
      } 
    }

    if (scoreThrees == true) { 
      if (numThrees > 0){
        return validScore = true;
      } 
    }

    if (scoreFours == true) { 
      if (numFours > 0){
        return validScore = true;
      } 
    }

    if (scoreFives == true) { 
      if (numFives > 0){
        return validScore = true;
      } 
    }

    if (scoreSixes == true) { 
      if (numSixes > 0){
        return validScore = true;
      } 
    }

    if (score3OfAKind == true){ 
      if (numOnes == 3 || numTwos == 3 || numThrees == 3 || numFours == 3 || numFives == 3 || numSixes == 3) { 
        return validScore = true; 
      }
    } 

    if (score4OfAKind == true){ 
      if (numOnes == 4 || numTwos == 4 || numThrees == 4 || numFours == 4 || numFives == 4 || numSixes == 4) { 
        return validScore = true; 
      }
    } 

    if (scoreYahtzee == true){ 
      if (numOnes == 5 || numTwos == 5 || numThrees == 5 || numFours == 5 || numFives == 5 || numSixes == 5) { 
        return validScore = true; 
      }
    } 

    if (scoreFullHouse == true){ 
      if (numOnes == 3 || numTwos == 3 || numThrees == 3 || numFours == 3 || numFives == 3 || numSixes == 3) && 
        (numOnes == 2 || numTwos == 2 || numThrees == 2 || numFours == 2 || numFives == 2 || numSixes == 2){
          return validScore = true; 
        } 
    }

    if (scoreSmStraight == true){ 
      // combinations are 1,2,3,4 ... 2,3,4,5 ... 3,4,5,6 
      if (numOnes >= 1 && numTwos >= 1 && numThrees >= 1 && numFours >= 1) || 
        (numTwos >= 1 && numThrees >= 1 && numFours >= 1 && numFives >= 1) || 
        (numThrees >= 1 && numFours >= 1 && numFives >= 1 && numSixes >= 1){
        return validScore = true; 
        } 
    }

    if (scoreLgStraight == true){ 
      // combinations are 1,2,3,4,5 ... 2,3,4,5,6
      if (numOnes == 1 && numTwos == 1 && numThrees == 1 && numFours == 1 && numFives == 1) || 
      (numTwos == 1 && numThrees == 1 && numFours == 1 && numFives == 1 && numSixes == 1){
      return validScore = true; 
      } 
    }
        
    }

    if (scoreChance == true){ 
      // combinations are 1,2,3,4,5 ... 2,3,4,5,6
      return validScore = true;
    }





    }
    
  }
}