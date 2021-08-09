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
    this.upperScore = 0;
    this.lowerScore = 0;
    this.editMode = false;
    this.numYahtzeeBonuses = 0;

    this.scores = new int[20];
    this.possibleScores = new int[20];
    for (int i = 0; i < 20; i++) {
      this.scores[i] = -1;
      this.possibleScores[i] = -1;
    }
  }

  /**
   * @param ind
   */
  public void setScore(int ind) {
    if (this.editMode) {
      this.scores[ind] = this.possibleScores[ind];
    }
  }

  /**
   * @param ind
   * @return sectionAvailable
   */
  private boolean getSectionAvailabilty(int ind) {
    boolean sectionAvailable = true;
    if (this.scores[ind] != -1) {
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
      if (this.scores[i] == -1) { 
        filled = false;
      }
    }
    return filled;
  }  

  public int getScore(int ind) {
    return this.scores[ind];
  }

  private int getUpperScore() {
    for (int i = 0; i < 6; i++) {
      if (this.scores[i] != -1) {
        this.upperScore = this.upperScore + this.scores[i];
      }
    }
    this.scores[15] = this.upperScore;
    this.possibleScores[15] = this.upperScore;
    // Bonus Score
    if (this.upperScore >= 63) {
      this.scores[16] = 1;
      this.possibleScores[16] = 1;
      this.upperScore = this.upperScore + 35;
    } else {
      this.scores[16] = 0;
      this.possibleScores[16] = 0;
    }
    this.scores[17] = this.upperScore;
    this.possibleScores[17] = this.upperScore;
    return this.upperScore;
  }

  public int getNumYahtzeeBonuses(){ 
    return this.scores[13];
  }

  private int getLowerScore() {
    for (int i = 7; i < 13; i++) {
      if (this.scores[i] != -1) {
        this.lowerScore = this.lowerScore + this.scores[i];
      }
    }
    this.scores[18] = this.lowerScore;
    this.possibleScores[18] = this.lowerScore;
    return this.lowerScore;
  }

  /**
   * @return
   */
  public int getTotalScore() {
    this.scores[19] = getUpperScore() + getLowerScore();
    this.possibleScores[19] = getUpperScore() + getLowerScore();
    return this.scores[19];
  }


  /**
   * @param diceValues
   * @return
  */
  public int[] getPossibleScores(ArrayList<Integer> diceValues) {

    System.out.println("GETTING this.scores");

    int numOnes = Collections.frequency(diceValues, 1);
    int numTwos = Collections.frequency(diceValues, 2);
    int numThrees = Collections.frequency(diceValues, 3);
    int numFours = Collections.frequency(diceValues, 4);
    int numFives = Collections.frequency(diceValues, 5);
    int numSixes = Collections.frequency(diceValues, 6);

    //Aces
    if (getSectionAvailabilty(0)){
      this.possibleScores[0] = numOnes;
    } else { 
      this.possibleScores[0] = -1;
    }

    //Twos
    if (getSectionAvailabilty(1)){
      this.possibleScores[1] = numTwos * 2; 
    } else { 
      this.possibleScores[1] = -1;
    }

    //Threes
    if (getSectionAvailabilty(2)){
      this.possibleScores[2] = numThrees * 3; 
    } else { 
      this.possibleScores[2] = -1;
    }

    //Fours
    if (getSectionAvailabilty(3)){
      this.possibleScores[3] = numFours * 4; 
    } else { 
      this.possibleScores[3] = -1;
    }

    //Fives
    if (getSectionAvailabilty(4)){
      this.possibleScores[4] = numFives * 5; 
    } else { 
      this.possibleScores[4] = -1;
    }

    //Sixes
    if (getSectionAvailabilty(5)){
      this.possibleScores[5] = numSixes * 6; 
    } else { 
      this.possibleScores[5] = -1;
    }
    
    //Three of a Kind
    if ( (numOnes == 3 || numTwos == 3 || numThrees == 3 || numFours == 3 || numFives == 3 || numSixes == 3) &&
            getSectionAvailabilty(6) ) {
      int sum = getSumDice(diceValues);
      this.possibleScores[6] = sum; 
    } else if (getSectionAvailabilty(6)) {
      this.possibleScores[6] = 0; 
    } else {
      this.possibleScores[6] = -1; 
    }
        
    //Four of a Kind
    if ( (numOnes == 4 || numTwos == 4 || numThrees == 4 || numFours == 4 || numFives == 4 || numSixes == 4) &&
            getSectionAvailabilty(7) ) {
      int sum = getSumDice(diceValues);
      this.possibleScores[7] = sum; 
    } else if (getSectionAvailabilty(7)) {
      this.possibleScores[7] = 0; 
    } else { 
      this.possibleScores[7] = -1; 
    }
    
    //Full House
    if ( (numOnes == 3 || numTwos == 3 || numThrees == 3 || numFours == 3 || numFives == 3 || numSixes == 3) &&
            (numOnes == 2 || numTwos == 2 || numThrees == 2 || numFours == 2 || numFives == 2 || numSixes == 2) &&
            getSectionAvailabilty(8) ) {
        this.possibleScores[8] = 25; 
      } else if (getSectionAvailabilty(8)) {
        this.possibleScores[8] = 0; 
      } else { 
        this.possibleScores[8] = -1; 
      }

    //Small Straight -- combinations are 1,2,3,4 ... 2,3,4,5 ... 3,4,5,6
    if ( ( (numOnes >= 1 && numTwos >= 1 && numThrees >= 1 && numFours >= 1) ||
            (numTwos >= 1 && numThrees >= 1 && numFours >= 1 && numFives >= 1) ||
            (numThrees >= 1 && numFours >= 1 && numFives >= 1 && numSixes >= 1) ) &&
          getSectionAvailabilty(9) ) {
        this.possibleScores[9] = 30; 
      } else if (getSectionAvailabilty(9)) {
        this.possibleScores[9] = 0; 
      } else { 
        this.possibleScores[9] = -1; 
      }

    //Large Straight -- combinations are 1,2,3,4,5 ... 2,3,4,5,6
    if ( ( (numOnes == 1 && numTwos == 1 && numThrees == 1 && numFours == 1 && numFives == 1) ||
            (numTwos == 1 && numThrees == 1 && numFours == 1 && numFives == 1 && numSixes == 1) ) &&
          getSectionAvailabilty(10) ) {
      this.possibleScores[10] = 40; 
    } else if (getSectionAvailabilty(10)) {
      this.possibleScores[10] = 0; 
    } else { 
      this.possibleScores[10] = -1; 
    }

    
    //Yahtzee
    if ( (numOnes == 5 || numTwos == 5 || numThrees == 5 || numFours == 5 || numFives == 5 || numSixes == 5) &&
          (getSectionAvailabilty(11)) &&
          (this.numYahtzeeBonuses == 0)) {
      this.possibleScores[11] = 50; 
    } else if ( (numOnes == 5 || numTwos == 5 || numThrees == 5 || numFours == 5 || numFives == 5 || numSixes == 5) &&
            (!getSectionAvailabilty(11)) &&
            (this.numYahtzeeBonuses < 3) ) {
      this.numYahtzeeBonuses = this.numYahtzeeBonuses + 1; 
      this.possibleScores[13] = this.numYahtzeeBonuses;
      this.possibleScores[14] = this.numYahtzeeBonuses * 100; 
    } else if (getSectionAvailabilty(11)) {
      this.possibleScores[11] = 0;
    } else { 
      this.possibleScores[11] = -1; 
    }

    //Chance
    if (getSectionAvailabilty(12)) {
      this.possibleScores[12] = getSumDice(diceValues); 
    } else { 
      this.possibleScores[12] = -1; 
    }
    getTotalScore();
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
