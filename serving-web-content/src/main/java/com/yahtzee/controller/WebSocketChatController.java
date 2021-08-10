package com.yahtzee.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.yahtzee.domain.WebSocketChatMessage;

@Controller
public class WebSocketChatController {

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/chat")
	public WebSocketChatMessage sendMessage(@Payload WebSocketChatMessage webSocketChatMessage) {
		return webSocketChatMessage;
	}
}