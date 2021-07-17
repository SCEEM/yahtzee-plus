package com.example.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.player.Player;



public class Chat {

    // private ArrayList<String> 
    private HashMap<Player, String> messages;

    public Chat(){
        // this.messages = new ArrayList<String>();
        this.messages = new HashMap<Player, String>();
        
    }

    public void addMessage(Player currentPlayer, String msg){
        // this.messages.add(msg);
        this.messages.put(currentPlayer, msg);
    }

    

    public ArrayList<String> getAllMessages(){
        // return new ArrayList<String>(messages);
        return new ArrayList<String>(this.messages.values());
    }
}