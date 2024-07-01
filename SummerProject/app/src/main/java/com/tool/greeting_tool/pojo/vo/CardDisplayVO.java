package com.tool.greeting_tool.pojo.vo;

public class CardDisplayVO {

    private String textId;
    private String emojiId;
    private String animationId;

    public CardDisplayVO(String textId, String emojiId, String animationId) {
        this.animationId = animationId;
        this.emojiId = emojiId;
        this.textId = textId;
    }


    public String getAnimationId() {
        return animationId;
    }

    public String getEmojiId() {
        return emojiId;
    }

    public String getTextId() {
        return textId;
    }

    public void setAnimationId(String animationId) {
        this.animationId = animationId;
    }

    public void setEmojiId(String emojiId) {
        this.emojiId = emojiId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }
}
