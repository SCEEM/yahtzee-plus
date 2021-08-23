package com.yahtzee.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.yahtzee.player.Player;

/**
 * This class represents the game state for all players.
 */
public class Game {

  private ArrayList<Player> playerList; // the list of all Players
  private ArrayList<Integer> scoreList;
  private int playerListIndex;
  private Player currentActivePlayer;
  private static final int MAX_PLAYERS = 6;
  private final int MAX_ROUNDS = 13;


  /**
   * Constructor
   */
  public Game() {
    this.playerList = new ArrayList<>();
    this.playerListIndex = 0;
    this.currentActivePlayer = null; // placeholder for assignActivePlayer functionality
    this.scoreList = new ArrayList<Integer>();
  }

  /**
   * Create a new Player and add them to the game.
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
      }

      return newPlayer;
    } else {
      //TODO: return error message 
      return null;
    }
  }

  /**
   * Remove a Player from the game.
   *
   * @param id the id of the Player to remove
   * @return true if the Player was removed; false if they weren't found
   */
  public boolean removePlayerById(int id) {
    Player playerToRemove = this.getPlayerById(id);
    if (playerToRemove != null) {
      // edit the playerListIndex to remove this player's turns
      playerListIndex = id < (playerListIndex % playerList.size()) ?
              playerListIndex - (int) Math.ceil((double) playerListIndex / (double) playerList.size()) :
              playerListIndex - (int) Math.floor((double) playerListIndex/ (double) playerList.size());

      // remove them from the player list
      playerList.remove(playerToRemove);
      return true;
    }
    return false;
  }

  /**
   * Get a Player by their id.
   *
   * @param id the playerId
   * @return the Player
   */
  public Player getPlayerById(int id) {
    for (Player player : playerList) {
      if (player.getPlayerId() == id) {
        return player;
      }
    }
    return null;
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
    if (gameHasRoundsLeft()){ // will not assign another player to be active if there are no more rounds to play
      currentActivePlayer = playerList.get(playerListIndex % playerList.size());
      currentActivePlayer.startTurn();
    } else {
      // no active player is assigned when the game has reached 13 rounds
      currentActivePlayer = null;
    }
  }

  /**
   * will check if the game has exceeded 13 rounds of play
   * @return True if the game is on it 13th or lesser round, false otherwise
   */
  private boolean gameHasRoundsLeft(){
    int roundIdx = playerListIndex / playerList.size(); // returns a new, incremented whole number when the game returns back to the first player
    return roundIdx < MAX_ROUNDS;
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
 /** 
  * returns the player with the highest score. This method is called when he game has ended
  *
  */
  public Player getHighestScoringPlayer(){
    Player winner = null;
    int minScore  = -1;
    for (Player p: playerList){
      if (p.getTotalScore() > minScore){
        minScore = p.getTotalScore();
        winner = p;
      }
    }
    return winner;
  }

}