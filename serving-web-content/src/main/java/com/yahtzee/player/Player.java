package com.yahtzee.player;

import com.yahtzee.turn.Turn;

import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


/**
 * This class represents a player in a {@link com.yahtzee.game.Game}
 */
public class Player {

  private boolean currentTurn;
  private boolean isHost;
  private int playerId;
  private int score;
  private String playerName;
  private ScoreCard scoreCard;
  public Turn myTurn;

  /**
   * Constructor
   */
  public Player(int playerId) {
    this.currentTurn = false; //active player not assigned yet
    this.playerId = playerId;
    this.playerName = playerName;
    this.isHost = isHost;
    this.score = 0;
    this.scoreCard = new ScoreCard();
    this.myTurn = null;
  }

  public void startTurn() {
    this.currentTurn = true;
    this.myTurn = new Turn();
  }

  public void endTurn() {
    this.currentTurn = false;
    this.myTurn = null;
  }

  public Turn getMyTurn() {
    return this.myTurn;
  }

  public boolean isCurrentlyTakingTurn() {
    return this.currentTurn;
  }

  public boolean canRollDice() {
    return this.currentTurn && this.myTurn.canRoll();
  }
  
  public JSONArray getScorecard() {
    System.out.println("GETTING SCORECARD");
    ArrayList<Integer> diceValues = (this.myTurn).getDiceValues();

    JSONArray retVal = new JSONArray();
      for(int i = 0; i < 20; i++) {
          JSONObject scorecardRow = new JSONObject();
          scorecardRow.put("score", this.scoreCard.getPossibleScores(diceValues)[i]);
          scorecardRow.put("availability", this.scoreCard.getSectionAvailabilty(i));
          retVal.add(scorecardRow);
      }

    this.score = this.scoreCard.getTotalScore();
    System.out.println(this.score);
    return retVal;
  }

  public void rollDice() {
    if (canRollDice()) {
      //TODO: NEED to create a method in turn to model dice role action
    }
  }

  public void keepDice() {
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

  @Override
  public String toString() {
    return "Player: id = " + playerId + ", name = " + playerName + ", currentTurn = " + currentTurn;
  }

}