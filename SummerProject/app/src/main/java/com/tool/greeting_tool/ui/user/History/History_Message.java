package com.tool.greeting_tool.ui.user.History;

import androidx.annotation.NonNull;

public class History_Message {

    private int id;
    private Long cardId;

    private String Message;

    public History_Message(int id, String Message, Long cardId){
        this.Message = Message;
        this.id = id;
        this.cardId = cardId;
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

    public Long getCardId() {
        return cardId;
    }

    @NonNull
    public String getMessage() {
        return Message;
    }

}
