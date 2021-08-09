package com.yahtzee;

import com.yahtzee.game.Game;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class GameConfiguration {
    @Bean
    public Game game() {
        return new Game();
    }
}
