package com.example.web;

import com.example.game.Game;
import com.example.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The main controller class for the application.
 */
@Controller
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
    return "index";
  }

}