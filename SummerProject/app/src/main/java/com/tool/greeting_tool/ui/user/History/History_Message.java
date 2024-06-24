package com.tool.greeting_tool.ui.user.History;

import androidx.annotation.NonNull;

public class History_Message {

    private int id;

    private String Message;

    public History_Message(int id, String Message){
        this.Message = Message;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getMessage() {
        return Message;
    }

}
