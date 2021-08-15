package com.yahtzee.controller;

import com.yahtzee.game.Game;
import com.yahtzee.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 * The main controller class for the application.
 */
@Controller
@MessageMapping("/") 
public class MainController {

  @Autowired
  ApplicationContext ctx;

  @Autowired
  Game game;

  /**
   * Main entry point
   *
   * @param model the Model
   * @return index
   */
  @GetMapping("/")
  public String greeting(Model model) {
    Player newPlayer = game.createPlayer();
    JSONArray playerListJson = makePlayerListJSON(game.getPlayerList());

    model.addAttribute("playerId", newPlayer.getPlayerId());
    model.addAttribute("playerList", playerListJson);
    model.addAttribute("scoreList", game.getScoreList());
    model.addAttribute("activePlayerId", game.getCurrentActivePlayer().getPlayerId());
    return "index";
  }

  /**
   *
   */
  @MessageMapping("/getPlayerList")
  @SendTo("/topic/updatePlayerList")
  public JSONArray updatePlayerList() {
    ArrayList<Player> playerList = game.getPlayerList();
    return makePlayerListJSON(playerList);
  }

  /**
   *
   * @param chatMessage a list of the values to keep
   */
  @MessageMapping("/chat")
  @SendTo("/topic/chat")
  public String setKeepers(String chatMessage) {
    System.out.println("SET KEEPERS: " + chatMessage);
    return chatMessage;
  }

  private JSONArray makePlayerListJSON(ArrayList<Player> playerList) {
    JSONArray playerListJson = new JSONArray();
    for(Player player : playerList) {
      JSONObject playerJson = new JSONObject();
      playerJson.put("playerId", player.getPlayerId());
      playerJson.put("score", player.getTotalScore());
      playerListJson.add(playerJson);
    }
    return playerListJson;
  }
}