package com.yahtzee.domain;


public class WebSocketChatMessage {
	private String type;
	private String content;
	private String sender;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
}

// /**
//  *
//  */
// public class Chat {

//   // private ArrayList<String>
//   private HashMap<Player, String> messages;

//   public Chat() {
//     // this.messages = new ArrayList<String>();
//     this.messages = new HashMap<Player, String>();

//   }

//   public void addMessage(Player currentPlayer, String msg) {
//     // this.messages.add(msg);
//     this.messages.put(currentPlayer, msg);
//   }


//   public ArrayList<String> getAllMessages() {
//     // return new ArrayList<String>(messages);
//     return new ArrayList<String>(this.messages.values());
//   }
// }