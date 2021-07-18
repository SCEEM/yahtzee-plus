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
    private int scoreCardId; 

    /**
     * Constructor
     */
    public Player() {
        this.currentTurn = false; //active player not assigned yet
        this.playerId = playerId;
        this.playerName = playerName;
        this.isHost = isHost; 
        this.scoreCardId = scoreCardId;
    }

    public void takeTurn() {
        this.currentTurn = true;
        Turn newTurn = new Turn();
    }

    /* May be moved to inside the takeTurn() method */
    public void fillInScoreCard() {
        ScoreCard.editMode = true; 
    }

 

}