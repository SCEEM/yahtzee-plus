package com.yahtzee.player;

import com.yahtzee.turn.Turn;

import java.util.ArrayList;

/**
 * This class represents a player in a {@link com.yahtzee.game.Game}
 */
public class Player {

  private boolean isCurrentTurn;
  private boolean isHost;
  private int playerId;
  private int score;
  private String playerName;
  private ScoreCard scoreCard;
  private Turn currentTurn;

  /**
   * Constructor
   */
  public Player(int playerId) {
    this.isCurrentTurn = false; //active player not assigned yet
    this.playerId = playerId;
    this.playerName = playerName;
    this.isHost = isHost;
    this.score = 0;
    this.scoreCard = new ScoreCard();
    this.currentTurn = null;
  }

 
  public void startTurn(){
    this.isCurrentTurn = true;
    this.currentTurn = new Turn();
  }

  public void endTurn(){
    this.isCurrentTurn = false;
    this.currentTurn = null;
  }

  public boolean isCurrentlyTakingTurn(){
    return this.isCurrentTurn;
  }

  public int[] getScorecard() {
    System.out.println("GETTING SCORECARD");
    ArrayList<Integer> diceValues = (this.currentTurn).getDiceValues();
    int[] retVal = this.scoreCard.getPossibleScores(diceValues);
    this.score = this.scoreCard.getTotalScore();
    System.out.println(this.score);
    return retVal;
  }

  public boolean canRollDice(){
    return this.isCurrentTurn && this.currentTurn.canRoll();
  }

  public void rollDice(){
    if (canRollDice()){
      //TODO: NEED to create a method in turn to model dice role action
    }
  }
  public void keepDice(){
    //TODO: Need to create a method to select keeper Dice in turn object
  }

  public int getPlayerId() {
    return this.playerId;
  }

  public int getTotalScore() {
      return scoreCard.getTotalScore();
  }

    /* May be moved to inside the takeTurn() method */
  public void fillInScoreCard() {
    //ScoreCard.editMode = true;
  }


}