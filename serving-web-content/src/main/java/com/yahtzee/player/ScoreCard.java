package com.yahtzee.player;

import java.util.ArrayList;
import java.util.*;

/**
 * This class represents a Yahtzee scorecard.
 */
public class ScoreCard {
  private boolean editMode;
  private int[] scores;
  private int[] possibleScores; 
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

    int[] scores = new int[15];
    for (int i = 0; i < 15; i++) {
      scores[i] = -1; 
    }
  }

  /**
   * @param ind
   * @param points
   */
  public void setScore(int ind, int possibleScores) {
    if (this.editMode == true) {
      scores[ind] = possibleScores[ind];
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
    return sectionAvailable;
  }

  /**
   * @return
   */
  public boolean boardFilled() {
    boolean filled = true;
    for (int i = 0; i < 13; i++) {
      if (scores[i] == -1) { 
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

  public int getNumYahtzeeBonuses(){ 
    return scores[13];
  }

  public int getLowerScore() {
    for (int i = 7; i < 13; i++) {
      if (scores[i] != -1) {
        lowerScore = lowerScore + scores[i];
      }
    }

    lowerScore = lowerScore + scores[14];
    return lowerScore;
  }

  /**
   * @return
   */
  public int getTotalScore() {
    return upperScore + lowerScore;
  }


  /**  
   * @param ArrayList<Integer> diceValues
   * @param int[] possibleScores
   * @return
  */
  public boolean getPossibleScores(ArrayList<Integer> diceValues, int[] possibleScores, int numYahtzeeBonuses) { 

    int numOnes = Collections.frequency(diceValues, 1);
    int numTwos = Collections.frequency(diceValues, 2);
    int numThrees = Collections.frequency(diceValues, 3);
    int numFours = Collections.frequency(diceValues, 4);
    int numFives = Collections.frequency(diceValues, 5);
    int numSixes = Collections.frequency(diceValues, 6);

    //Aces
    if (getSectionAvailabilty(0) == true){
      possibleScores[0] = numOnes * 1; 
    } else { 
      possibleScores[0] = -1;
    }

    //Twos
    if (getSectionAvailabilty(1) == true){
      possibleScores[1] = numTwos * 2; 
    } else { 
      possibleScores[1] = -1;
    }

    //Threes
    if (getSectionAvailabilty(2) == true){
      possibleScores[2] = numThrees * 3; 
    } else { 
      possibleScores[2] = -1;
    }

    //Fours
    if (getSectionAvailabilty(3) == true){
      possibleScores[3] = numFours * 4; 
    } else { 
      possibleScores[3] = -1;
    }

    //Fives
    if (getSectionAvailabilty(4) == true){
      possibleScores[4] = numFives * 5; 
    } else { 
      possibleScores[4] = -1;
    }

    //Sixes
    if (getSectionAvailabilty(5) == true){
      possibleScores[5] = numSixes * 6; 
    } else { 
      possibleScores[5] = -1;
    }
    
    //Three of a Kind
    if (numOnes == 3 || numTwos == 3 || numThrees == 3 || numFours == 3 || numFives == 3 || numSixes == 3) && 
      (getSectionAvailabilty(6) == true){ 
      int sum = getSumDice(diceValues);
      possibleScores[6] = sum; 
    } else if (getSectionAvailabilty(6) == true) {
      possibleScores[6] = 0; 
    } else { 
      possibleScores[6] = -1; 
    }
        
    //Four of a Kind
    if (numOnes == 4 || numTwos == 4 || numThrees == 4 || numFours == 4 || numFives == 4 || numSixes == 4) && 
      (getSectionAvailabilty(7) == true) { 
      int sum = getSumDice(diceValues);
      possibleScores[7] = sum; 
    } else if (getSectionAvailabilty(7) == true) {
      possibleScores[7] = 0; 
    } else { 
      possibleScores[7] = -1; 
    }
    
    //Full House
    if (numOnes == 3 || numTwos == 3 || numThrees == 3 || numFours == 3 || numFives == 3 || numSixes == 3) && 
      (numOnes == 2 || numTwos == 2 || numThrees == 2 || numFours == 2 || numFives == 2 || numSixes == 2) && 
      (getSectionAvailabilty(8) == true) { 
        possibleScores[8] = 25; 
      } else if (getSectionAvailabilty(8) == true) {
        possibleScores[8] = 0; 
      } else { 
        possibleScores[8] = -1; 
      }

    //Small Straight -- combinations are 1,2,3,4 ... 2,3,4,5 ... 3,4,5,6 
    if (numOnes >= 1 && numTwos >= 1 && numThrees >= 1 && numFours >= 1) || 
      (numTwos >= 1 && numThrees >= 1 && numFours >= 1 && numFives >= 1) || 
      (numThrees >= 1 && numFours >= 1 && numFives >= 1 && numSixes >= 1) && 
      (getSectionAvailabilty(9) == true) { 
        possibleScores[9] = 30; 
      } else if (getSectionAvailabilty(9) == true) {
        possibleScores[9] = 0; 
      } else { 
        possibleScores[9] = -1; 
      }

    //Large Straight -- combinations are 1,2,3,4,5 ... 2,3,4,5,6
    if (numOnes == 1 && numTwos == 1 && numThrees == 1 && numFours == 1 && numFives == 1) || 
      (numTwos == 1 && numThrees == 1 && numFours == 1 && numFives == 1 && numSixes == 1) && 
      (getSectionAvailabilty(10) == true) { 
      possibleScores[10] = 40; 
    } else if (getSectionAvailabilty(10) == true) {
      possibleScores[10] = 0; 
    } else { 
      possibleScores[10] = -1; 
    }

    
    //Yahtzee
    if (numOnes == 5 || numTwos == 5 || numThrees == 5 || numFours == 5 || numFives == 5 || numSixes == 5) && 
      (getSectionAvailabilty(11) == true) && (numYahtzeeBonuses == 0) {
      possibleScores[11] = 50; 
    } else if (numOnes == 5 || numTwos == 5 || numThrees == 5 || numFours == 5 || numFives == 5 || numSixes == 5) &&
     (getSectionAvailabilty(11) == false) && (numYahtzeeBonuses < 3) {
      numYahtzeeBonuses = numYahtzeeBonuses + 1; 
      possibleScores[13] = numYahtzeeBonuses;
      possibleScores[14] = numYahtzeeBonuses * 100; 
    } else if (getSectionAvailabilty(11) == true) {
      possibleScores[11] = 0; 
    } else { 
      possibleScores[11] = -1; 
    }

    //Chance
    if (getSectionAvailabilty(12) == true) { 
      possibleScores[12] = getSumDice(diceValues); 
    } else { 
      possibleScores[12] = -1; 
    } 
  } 

  private int getSumDice(ArrayList<Integer> diceValues) { 
    int sum = 0;
    for(int i = 0; i < diceValues.size(); i++){
        sum += diceValues.get(i);
    } 
    return sum;  
  }

 
  } 
