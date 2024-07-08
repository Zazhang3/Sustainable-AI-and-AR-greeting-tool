package com.tool.greeting_tool.pojo.dto;

public class GreetingCard {
    private Long user_id;

    private String card_id;

    private String postcode;

    private String emoji_id;

    private String animation_id;

    private Long id;

    public String getCardId() {
        return card_id;
    }


    public void setText(String text) {
        this.card_id = text;
    }

    public void setEmoji(String emoji) {
        this.emoji_id = emoji;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setAnimation(String animation) {
        this.animation_id = animation;
    }

    public void setUser_id(Long userId) {
        this.user_id = userId;
    }

    public Long getId() {
        return id;
    }

    public String getAnimationId() {
        return animation_id;
    }

    public String getEmojiId() {
        return emoji_id;
    }
}
