package com.yahtzee.game;

import java.util.ArrayList;
import java.util.Map;

import com.yahtzee.player.Player;
import com.yahtzee.web.TurnController;

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
    this.playerList = new ArrayList<Player>();
    this.playerListIndex = 0;
    this.currentActivePlayer = null; // placeholder for assignActivePlayer functionality
    this.system = new Player(-1); // a representation of the system for Chat purposes
    // this.chat = new Chat();
    this.scoreList = new ArrayList<Integer>();
  }

  public Player createPlayer() {
    if (this.playerList.size() < MAX_PLAYERS){
      Player newPlayer = new Player(this.playerList.size());
      this.playerList.add(newPlayer);
      this.scoreList.add(newPlayer.getPlayerId(), 0);
      if (playerList.size() == 1) {
        this.currentActivePlayer = newPlayer;
        this.currentActivePlayerBeginsTurn();
          // newPlayer.takeTurn();
      }
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
    System.out.println("Current Player: " + this.currentActivePlayer.getPlayerId());
    return this.currentActivePlayer;
  }

  public ArrayList<Player> getPlayerList() {
      return this.playerList;
  }

  /**
   * Assigns a player as the current active player
   */
  public int assignNextActivePlayer() {
    // the player list index will continue to increment
    // the modulus ensures the index will remain within the bounds of the array
    ++playerListIndex;
    currentActivePlayer = playerList.get(playerListIndex % playerList.size());
    return currentActivePlayer.getPlayerId();
  }

  
  /**
   * retrieves the current player's ID
   * @return - int: the current player's ID
   */
  public int getCurrentPlayersID(){
    return this.currentActivePlayer.getPlayerId();
  }

  
  /**
   * creates the turn object for the current player
   */
  public void currentActivePlayerBeginsTurn(){
    this.currentActivePlayer.startTurn();
  }

  /**
   * ends the turn for the current active player once they select the "Finish Turn" button
   */
  public void currentActivePlayerEndsTurn(){
    this.currentActivePlayer.endTurn();
  }

  public boolean isActivePlayer(Player player){
    return player.isCurrentlyTakingTurn();
  }
  


  /**
   * allows the current active player to roll their dice, not including
   * any keepers the player may have previously selected
   */
  public void currentPlayerRollsDice(){
    
  }

  /**
   * allows the current active player to select which dice they want to keep from their current roll
   * only possible during the current turn for the currentActivePlayer
   */
  public void currentPlayerSelectsKeepers(){

  }

  /**
   * allows the current active player to enter in a score from a set of pre-calculated choices in their scorecard.
   * The score choices are dependent on their current roll + any keepers they have previously selected
   */
  public void currentPlayerUpdatesScoreCard(){

  }


/**
   * send the player message to the chat object for storage and to update the list of messages
   * @param player - the player sending in a message
   * @param msg - the String messgae
   */
  public void addPlayerMessage(Player player, String msg) {
    // this.chat.addMessage(player, msg);
  }

  /**
   * send a message from the system to the chat
   * @param msg - String message from the system to be seen by all players
   */
  public void addSystemMessage(String msg) {
    // this.chat.addMessage(system, msg);
  }

  /**
   * retrieves all of the messages in the chat object
   * @return - an array list containing all of the messages in the chat
   */
  public ArrayList<String> getAllMessages() {
    return null; //TODO
    // return thissc.chat.getAllMessages();
  }





}