package com.example;

import com.example.game.Game;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfiguration {
    @Bean
    public Game game() {
        return new Game();
    }
}
