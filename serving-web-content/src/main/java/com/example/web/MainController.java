package com.example.web;

import com.example.turn.Roll;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

/**
 * The main controller class for the application.
 */
@Controller
public class MainController {

    /**
     *
     * @param name
     * @param model
     * @return
     */
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name",
                required = false,
                defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("dice_0", "5");
        model.addAttribute("dice_1", "5");
        model.addAttribute("dice_2", "5");
        model.addAttribute("dice_3", "5");
        model.addAttribute("dice_4", "5");
        return "index";
    }

}