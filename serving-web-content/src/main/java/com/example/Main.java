package com.example;

import com.example.game.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class Main {

	/**
	 * Main entry point for application.
	 *
	 * @param args an array of command line arguments
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(GameConfiguration.class);
		Game game = ctx.getBean(Game.class);

		SpringApplication.run(Main.class, args);
	}

}
