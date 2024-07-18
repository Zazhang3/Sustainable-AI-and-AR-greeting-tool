package com.tool.greeting_tool.ui.user.History;

import androidx.annotation.NonNull;

public class History_Message {

    private int id;
    private final Long cardId;

    private final String Message;

    private final String create_time;

    public String getCreate_time() {
        return create_time;
    }

    public History_Message(int id, String Message, Long cardId, String create_time){
        this.Message = Message;
        this.id = id;
        this.cardId = cardId;
        this.create_time = create_time;
    }

    public void setId(int id) {
        this.id = id;
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
