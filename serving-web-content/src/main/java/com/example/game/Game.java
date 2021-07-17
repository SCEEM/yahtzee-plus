package com.example.game;

import java.util.ArrayList;
import com.example.player.Player;

/**
 * This class represents the game state for all players.
 */
public class Game {

    private ArrayList<Player> playerList ;
    private int playerListIndex;
    private Player currentActivePlayer;

    
    /**
     * Constructor
     */
    public Game() {
        this.playerList = new ArrayList<Player>();
        this.playerListIndex = 0;
    }

    /**
     * Assigns a player as the current active player
     */
    public void assignActivePlayer() {
        // the player list index will continue to increment
        // the modulus ensures the index will remain within the bounds of the array
        currentActivePlayer = playerList.get(playerListIndex % playerList.size());
        ++playerListIndex;
    }

    /**
     *
     */
    public void createTurnForPlayer() {

    }



    /* This is the inner class representing the chat. Can be switched to be non-static as necessary */
    private static class Chat {

    }

}