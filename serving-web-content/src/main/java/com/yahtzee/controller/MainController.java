package com.yahtzee.controller;

import com.yahtzee.game.Game;
import com.yahtzee.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The main controller class for the application.
 */
@Controller
@MessageMapping("/") 
public class MainController {

    @Autowired
    ApplicationContext ctx;

  /**
   * @param name
   * @param model
   * @return
   */
  @GetMapping("/")
  public String greeting(@RequestParam(name = "name",
      required = false,
      defaultValue = "World") String name, Model model) {
        Game game = ctx.getBean(Game.class);
        Player newPlayer = game.createPlayer();
        model.addAttribute("playerId", newPlayer.getPlayerId());
        model.addAttribute("playerList", game.getPlayerList());
        model.addAttribute("scoreList", game.getScoreList());
    return "index";
  }
}