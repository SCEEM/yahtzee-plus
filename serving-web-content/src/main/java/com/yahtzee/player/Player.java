package com.yahtzee.player;

import com.yahtzee.turn.Die;
import com.yahtzee.turn.Roll;
import com.yahtzee.turn.Turn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


/**
 * This class represents a player in a {@link com.yahtzee.game.Game}
 */
public class Player {

  private boolean currentTurn;
  private int playerId;
  private int score;
  private String name;
  private ScoreCard scoreCard;
  public Turn myTurn;

  /**
   * Constructor
   */
  public Player(int playerId) {
    this.currentTurn = false; //active player not assigned yet
    this.playerId = playerId;
    this.score = 0;
    this.name = "test player"; //TODO
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
  
  public JSONArray getPossibleScores() {
    ArrayList<Integer> diceValues = (this.myTurn).getDiceValues();
    int[] scorecard =  this.scoreCard.getPossibleScores(diceValues);

    JSONArray retVal = new JSONArray();
      for(int i = 0; i < 20; i++) {
          JSONObject scorecardRow = new JSONObject();
          scorecardRow.put("score",scorecard[i]);
          scorecardRow.put("availability", this.scoreCard.getSectionAvailabilty(i));
          retVal.add(scorecardRow);
      }
    this.score = this.scoreCard.getTotalScore();
    return retVal;
  }

  public int[] getScorecard() {
    return this.scoreCard.getScores();
  };

  public ArrayList<Die> rollDice() {
    if (canRollDice()) {
      Roll roll = myTurn.newRoll();
      return roll.rollDice(myTurn.getDice());
    } else {
      return null; //TODO: return error
    }
  };

  public ArrayList<Die> keepDice(ArrayList<Die> keepers) {
    List<String> dieIdsToKeep = keepers.stream().map(k -> k.getId()).collect(Collectors.toList());
    // get latest roll as part of the Turn
    Roll latestRoll = myTurn.getCurrentRoll();
    ArrayList<Die> latestDice = latestRoll.getDice();
    // update die status
    for (Die die : latestDice) {
      if (dieIdsToKeep.contains(die.getId())) {
        die.setStatus(Die.Status.KEEPER);
      }
    }
    // save dice status to the Roll
    myTurn.finishRoll(latestDice);
    System.out.println("Final status of dice in roll:");
    for (Die die : latestRoll.getDice()) {
      System.out.println(die.toString());
    }
    return keepers;
  }

  public int getPlayerId() {
    return this.playerId;
  }

  public int getTotalScore() {
    return scoreCard.getTotalScore();
  }

  /* May be moved to inside the takeTurn() method */
  public void setScore(int rowNumber) {
    if (this.scoreCard.getEditMode()) {
      this.scoreCard.setScore(rowNumber);
    }
  }
  
  public void setName(String name){
    this.name = name;
  }
  public String getName(){
    return this.name;
  }
  
  @Override
  public String toString() {
    return "Player: id = " + playerId + ", name = " + name + ", currentTurn = " + currentTurn;
  }
}