package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        return "index";
    }

}