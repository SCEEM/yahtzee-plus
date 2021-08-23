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
  private Turn myTurn;

  /**
   * Constructor.
   *
   * @param playerId an integer id
   */
  public Player(int playerId) {
    this.currentTurn = false; // active player not assigned yet
    this.playerId = playerId;
    this.score = 0;
    this.name = "test player"; // TODO
    this.scoreCard = new ScoreCard();
    this.myTurn = null;
  }

  /**
   * Begin the Player's turn.
   */
  public void startTurn() {
    this.currentTurn = true;
    this.myTurn = new Turn();
  }

  /**
   * End the Player's turn.
   */
  public void endTurn() {
    this.currentTurn = false;
    this.myTurn = null;
  }

  public Turn getMyTurn() {
    return this.myTurn;
  }

  /**
   * Check if it's currently the Player's turn.
   *
   * @return true if it is their turn; false if not
   */
  public boolean isCurrentlyTakingTurn() {
    return this.currentTurn;
  }

  /**
   * Calculate all possible scores for the given Turn.
   *
   * @return all possible scores
   */
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

  /**
   * Roll the dice during the Player's Turn.
   *
   * @return the dice as an ArrayList<Die>
   */
  public ArrayList<Die> rollDice() {
    if (canRollDice()) {
      // get the last known dice for the Roll
      ArrayList<Die> dice = myTurn.getCurrentDice();

      // create a new Roll
      Roll roll = myTurn.newRoll();
      return roll.rollDice(dice);
    } else {
      return null; //TODO: return error
    }
  }
  /**
   * Roll the dice during the Player's Turn.
   *
   * @return the dice as an ArrayList<Die>
   */
  public ArrayList<Die> rollKeepers(ArrayList<Die> keepers) {
      return this.myTurn.rollKeepers(keepers);
  }

  /**
   * Check if the Player can roll the dice.
   *
   * @return true if able to roll; false if not
   */
  public boolean canRollDice() {
    return this.currentTurn && this.myTurn.canRoll();
  }

  /**
   * Set the given dice as "keepers".
   *
   * @param keepers the dice to keep
   * @return the keepers
   */
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
//    System.out.println("Dice status after setting keepers:" + latestRoll.getDice());
    return latestDice;
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
  
  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
  
  @Override
  public String toString() {
    return "Player: id = " + playerId + ", name = " + name + ", currentTurn = " + currentTurn;
  }
}