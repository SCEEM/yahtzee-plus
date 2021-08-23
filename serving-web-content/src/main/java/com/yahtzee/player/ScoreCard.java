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
    this.editMode = false;
    this.numYahtzeeBonuses = 0;

    this.scores = new int[20];
    this.possibleScores = new int[20];
    for (int i = 0; i < 13; i++) {
      this.scores[i] = -1;
      this.possibleScores[i] = -1;
    }
    for (int i = 13; i < 20; i++) {
      this.scores[i] = 0;
      this.possibleScores[i] = 0;
    }
  }

  /**
   * @param rowNumber
   */
  public void setScore(int rowNumber) {
    if (this.editMode) {
      this.scores[rowNumber] = this.possibleScores[rowNumber];
      this.scores[17] = getUpperScore();
      this.scores[15] = this.scores[16] == 0 ? this.scores[17] : this.scores[17] - 35;
      this.scores[18] = getLowerScore();
      this.scores[19] = this.scores[17] + this.scores[18];
    }
  }

  /**
   *
   */
  public boolean getEditMode() {
    return this.editMode;
  }

  /**
   * @param ind
   * @return sectionAvailable
   */
  public boolean getSectionAvailabilty(int ind) {
    boolean sectionAvailable = true;
    if (this.scores[ind] != -1) {
      sectionAvailable = false;
      this.possibleScores[ind] = this.scores[ind]; 
    } 
    return sectionAvailable;
  }

  /**
   * @return
   */
  public boolean boardFilled() {
    boolean filled = true;
    for (int i = 0; i < 13; i++) {
      if (this.scores[i] == -1) { 
        filled = false;
      }
    }
    return filled;
  }  

  public int[] getScores() {
    return this.scores;
  }

  private int getUpperScore() {
    int upperScore = 0;
    for (int i = 0; i < 6; i++) {
      if (this.scores[i] != -1) {
        upperScore = upperScore + this.scores[i];
      }
    }
    // Bonus Score
    if (upperScore >= 63) {
      this.scores[16] = 35;
      upperScore = upperScore + 35;
    } else {
      this.scores[16] = 0;
    }
    return upperScore;
  }

  public int getNumYahtzeeBonuses(){ 
    return this.scores[13];
  }

  private int getLowerScore() {
    int lowerScore = 0;
    for (int i = 6; i < 13; i++) {
      if (this.scores[i] != -1) {
        lowerScore = lowerScore + this.scores[i];
      }
    }
    //Add Yahtzee bonus score in
    lowerScore = lowerScore + this.scores[14];
    return lowerScore;
  }

  /**
   * @return
   */
  public int getTotalScore() {
    this.scores[19] = getUpperScore() + getLowerScore();
    return this.scores[19];
  }


  /**
   * @param diceValues
   * @return
  */
  public int[] getPossibleScores(ArrayList<Integer> diceValues) {
    int numOnes = Collections.frequency(diceValues, 1);
    int numTwos = Collections.frequency(diceValues, 2);
    int numThrees = Collections.frequency(diceValues, 3);
    int numFours = Collections.frequency(diceValues, 4);
    int numFives = Collections.frequency(diceValues, 5);
    int numSixes = Collections.frequency(diceValues, 6);
    this.editMode = true;

    //Aces
    if (getSectionAvailabilty(0)){
      this.possibleScores[0] = numOnes;
    } 

    //Twos
    if (getSectionAvailabilty(1)){
      this.possibleScores[1] = numTwos * 2; 
    } 

    //Threes
    if (getSectionAvailabilty(2)){
      this.possibleScores[2] = numThrees * 3; 
    } 

    //Fours
    if (getSectionAvailabilty(3)){
      this.possibleScores[3] = numFours * 4; 
    }

    //Fives
    if (getSectionAvailabilty(4)){
      this.possibleScores[4] = numFives * 5; 
    } 

    //Sixes
    if (getSectionAvailabilty(5)){
      this.possibleScores[5] = numSixes * 6; 
    } 
    
    
    //Three of a Kind
    if ( (numOnes >= 3 || numTwos >= 3 || numThrees >= 3 || numFours >= 3 || numFives >= 3 || numSixes >= 3) &&
            getSectionAvailabilty(6) ) {
      int sum = getSumDice(diceValues);
      this.possibleScores[6] = sum; 
    } else if (getSectionAvailabilty(6)) {
      this.possibleScores[6] = 0; 
    }
        
    //Four of a Kind
    if ( (numOnes >= 4 || numTwos >= 4 || numThrees >= 4 || numFours >= 4 || numFives >= 4 || numSixes >= 4) &&
            getSectionAvailabilty(7) ) {
      int sum = getSumDice(diceValues);
      this.possibleScores[7] = sum; 
    } else if (getSectionAvailabilty(7)) {
      this.possibleScores[7] = 0; 
    } 
    
    //Full House
    if ( (numOnes == 3 || numTwos == 3 || numThrees == 3 || numFours == 3 || numFives == 3 || numSixes == 3) &&
            (numOnes == 2 || numTwos == 2 || numThrees == 2 || numFours == 2 || numFives == 2 || numSixes == 2) &&
            getSectionAvailabilty(8) ) {
        this.possibleScores[8] = 25; 
      } else if (getSectionAvailabilty(8)) {
        this.possibleScores[8] = 0; 
      } 
      

    //Small Straight -- combinations are 1,2,3,4 ... 2,3,4,5 ... 3,4,5,6
    if ( ( (numOnes >= 1 && numTwos >= 1 && numThrees >= 1 && numFours >= 1) ||
            (numTwos >= 1 && numThrees >= 1 && numFours >= 1 && numFives >= 1) ||
            (numThrees >= 1 && numFours >= 1 && numFives >= 1 && numSixes >= 1) ) &&
          getSectionAvailabilty(9) ) {
        this.possibleScores[9] = 30; 
      } else if (getSectionAvailabilty(9)) {
        this.possibleScores[9] = 0; 
      } 

    //Large Straight -- combinations are 1,2,3,4,5 ... 2,3,4,5,6
    if ( ( (numOnes == 1 && numTwos == 1 && numThrees == 1 && numFours == 1 && numFives == 1) ||
            (numTwos == 1 && numThrees == 1 && numFours == 1 && numFives == 1 && numSixes == 1) ) &&
          getSectionAvailabilty(10) ) {
      this.possibleScores[10] = 40; 
    } else if (getSectionAvailabilty(10)) {
      this.possibleScores[10] = 0; 
    } 

    
    //Yahtzee
    if ( (numOnes == 5 || numTwos == 5 || numThrees == 5 || numFours == 5 || numFives == 5 || numSixes == 5) &&
          (getSectionAvailabilty(11)) &&
          (this.numYahtzeeBonuses == 0)) {
      this.possibleScores[11] = 50; 
    } else if ( (numOnes == 5 || numTwos == 5 || numThrees == 5 || numFours == 5 || numFives == 5 || numSixes == 5) &&
            (getSectionAvailabilty(11) == false) && (this.numYahtzeeBonuses < 3) ) {
      this.numYahtzeeBonuses = this.numYahtzeeBonuses + 1; 
      this.scores[13] = this.numYahtzeeBonuses;
      this.scores[14] = this.scores[13] * 100; 
    } else if (getSectionAvailabilty(11)) {
      this.possibleScores[11] = 0;
    } 

    //Chance
    if (getSectionAvailabilty(12)) {
      this.possibleScores[12] = getSumDice(diceValues); 
    }

    // Upper scorecard update
    this.possibleScores[15] = this.scores[15];
    this.possibleScores[17] = this.scores[17];
    // Bonus Score
    this.possibleScores[16] = this.scores[16];

    // lower scorecard update
    this.possibleScores[18] = this.scores[18];

    return this.possibleScores;
  }

  private int getSumDice(ArrayList<Integer> diceValues) { 
    int sum = 0;
    for(int i = 0; i < diceValues.size(); i++){
        sum += diceValues.get(i);
    } 
    return sum;  
  }
}
