package com.example.game;

import java.util.ArrayList;
import java.util.Map;

import com.example.player.Player;

/**
 * This class represents the game state for all players.
 */
public class Game {

  private ArrayList<Player> playerList;
  private ArrayList<Integer> scoreList;
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
    this.system = new Player(-1); // a representation of the system for Chat purposes
    this.chat = new Chat();
    this.scoreList = new ArrayList<Integer>();
  }

  public Player createPlayer() {
      Player newPlayer = new Player(this.playerList.size());
      this.playerList.add(newPlayer);
      this.scoreList.add(newPlayer.getPlayerId(), 0);
      if (playerList.size() == 1) {
          this.currentActivePlayer = newPlayer;
          newPlayer.takeTurn();
      }
      return newPlayer;
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
  public void assignActivePlayer() {
    // the player list index will continue to increment
    // the modulus ensures the index will remain within the bounds of the array
    currentActivePlayer = playerList.get(playerListIndex % playerList.size());
    ++playerListIndex;

    currentActivePlayer.takeTurn();
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