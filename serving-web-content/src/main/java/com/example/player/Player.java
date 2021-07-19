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

  /**
   * Constructor
   */
  public Player(int playerId) {
    this.currentTurn = false; //active player not assigned yet
    this.playerId = playerId;
    this.playerName = playerName;
    this.isHost = isHost;
    this.scoreCard = new ScoreCard();
  }

    public void takeTurn() {
        this.currentTurn = true;
        Turn newTurn = new Turn();
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