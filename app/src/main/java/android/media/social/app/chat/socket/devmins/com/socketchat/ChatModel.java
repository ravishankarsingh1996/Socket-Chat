package android.media.social.app.chat.socket.devmins.com.socketchat;

import java.util.ArrayList;

public class ChatModel {

    private String message;
    private String userName;
    private int sender;

    public ChatModel(String message, String userName, int sender) {
        this.message = message;
        this.userName = userName;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }
}
