package com.example.game;

import java.util.ArrayList;

import com.example.player.Player;

/**
 * This class represents the game state for all players.
 */
public class Game {

  private ArrayList<Player> playerList;
  private int playerListIndex;
  private Player currentActivePlayer, system;
  private Chat chat;


  /**
   * Constructor
   */
  public Game() {
    this.playerList = new ArrayList<Player>();
    this.playerListIndex = 0;
    this.currentActivePlayer = null; // placeholder for assignActivePlayer functionality
    this.system = new Player(); // a representation of the system for Chat purposes
    this.chat = new Chat();
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

  public void addPlayerMessage(String msg) {
    this.chat.addMessage(this.currentActivePlayer, msg);
  }


  public void addSystemMessage(String msg) {
    this.chat.addMessage(system, msg);
  }

  public ArrayList<String> getAllMessages() {
    return this.chat.getAllMessages();
  }


}