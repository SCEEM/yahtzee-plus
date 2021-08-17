package com.yahtzee.game;

import java.util.ArrayList;

import com.yahtzee.player.Player;
import com.yahtzee.controller.TurnController;

/**
 * This class represents the game state for all players.
 */
public class Game {

  private ArrayList<Player> playerList;
  private ArrayList<Integer> scoreList;
  private int playerListIndex;
  private Player currentActivePlayer, system;
  private static final int MAX_PLAYERS = 6;
  // private Chat chat;


  /**
   * Constructor
   */
  public Game() {
    this.playerList = new ArrayList<>();
    this.playerListIndex = 0;
    this.currentActivePlayer = null; // placeholder for assignActivePlayer functionality
    this.system = new Player(-1); // a representation of the system for Chat purposes
    // this.chat = new Chat();
    this.scoreList = new ArrayList<Integer>();
  }

  /**
   *
   * @return a new {@link Player}
   */
  public Player createPlayer() {
    if (this.playerList.size() < MAX_PLAYERS) {
      Player newPlayer = new Player(this.playerList.size());
      this.playerList.add(newPlayer);
      this.scoreList.add(newPlayer.getPlayerId());

      // if first player to join, make them active player
      if (playerList.size() == 1) {
        this.currentActivePlayer = newPlayer;
        this.currentActivePlayerBeginsTurn();
          // newPlayer.takeTurn();
      }
      System.out.println("created player " + newPlayer.getPlayerId());
      return newPlayer;
    } else {
      //TODO: return error message 
      return null;
    }
  }

  public int getPlayerListSize() {
      return this.playerList.size();
  }

  public ArrayList<Integer> getScoreList() {
      return this.scoreList;
  }

  public Player getCurrentActivePlayer() {
    return this.currentActivePlayer;
  }

  public ArrayList<Player> getPlayerList() {
      return this.playerList;
  }

  /**
   * Assigns a player as the current active player
   */
  public void assignNextActivePlayer() {
    // the player list index will continue to increment
    // the modulus ensures the index will remain within the bounds of the array
    ++playerListIndex;
    currentActivePlayer = playerList.get(playerListIndex % playerList.size());
    currentActivePlayer.startTurn();
  }

  
  /**
   * retrieves the current player's ID
   * @return - int: the current player's ID
   */
  public int getCurrentPlayersID() {
    return this.currentActivePlayer.getPlayerId();
  }

  
  /**
   * creates the turn object for the current player
   */
  public void currentActivePlayerBeginsTurn() {
    this.currentActivePlayer.startTurn();
  }

  /**
   * ends the turn for the current active player once they select the "Finish Turn" button
   */
  public void currentActivePlayerEndsTurn() {
    this.currentActivePlayer.endTurn();
  }

  public boolean isActivePlayer(Player player) {
    return player.isCurrentlyTakingTurn();
  }

}