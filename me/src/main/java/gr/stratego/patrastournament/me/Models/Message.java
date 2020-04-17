package gr.stratego.patrastournament.me.Models;

import gr.stratego.patrastournament.me.StrategoApplication;
import gr.stratego.patrastournament.me.Utils.StringUtils;

public class Message {

    private String username;
    private String text; // message body
    private String color;

    public Message() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isCurrentUser() {
        if (StrategoApplication.getCurrentUser() != null) {
            return StringUtils.areEqual(StrategoApplication.getCurrentUser().getDisplayName(), username);
        }
        return false;
    }

}