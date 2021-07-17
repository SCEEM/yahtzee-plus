package com.example.Player;

import com.example.Turn.Turn;

public class Player {
    /* Instantiate Fields Here*/
    private Turn currentTurn;
    private boolean isHost; 
    private int playerId; 
    private String playerName; 
    private int scoreCardId; 

    /* Constructor */
    public Player(){
        this.currentTurn = false; 
        this.playerId = playerId;
        this.playerName = playerName; 
        this.isHost = isHost; 
        this.scoreCardId = scoreCardId;
    }

    public void takeTurn(){
        currentTurn = true; 
        
    }

    /* May be moved to inside the takeTurn() method */
    public void fillInScoreCard(){

    }

    

    /* This is the inner class representing the ScoreCard. 
     * I couldn't remember what we decided to do with the score card and the player 
     */
    private static class ScoreCard{

    }

}