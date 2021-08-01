package com.example.player;

import com.example.turn.Turn;

/**
 * This class represents a player in a {@link com.example.game.Game}
 */
public class Player {

  private boolean currentTurn;
  private boolean isHost;
  private int playerId;
  private String playerName;
  private ScoreCard scoreCard;
  private Turn myTurn;

  /**
   * Constructor
   */
  public Player(int playerId) {
    this.currentTurn = false; //active player not assigned yet
    this.playerId = playerId;
    this.playerName = playerName;
    this.isHost = isHost;
    this.scoreCard = new ScoreCard();
    myTurn = null;
  }

 
    public void startTurn(){
      this.currentTurn = true;
        this.myTurn = new Turn();
    } 
    public void endTurn(){
      this.currentTurn = false;
      this.myTurn = null;
    }
    public boolean isCurrentlyTakingTurn(){
      return this.currentTurn;
    }
    public boolean canRollDice(){
      return this.currentTurn && this.myTurn.canRoll();
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