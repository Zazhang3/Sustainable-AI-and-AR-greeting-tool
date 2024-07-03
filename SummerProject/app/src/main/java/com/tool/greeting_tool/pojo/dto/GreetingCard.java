package com.tool.greeting_tool.pojo.dto;

public class GreetingCard {
    private Long user_id;

    private Integer card_id;

    private String postcode;

    private Integer emoji_id;

    private Integer animation_id;

    private Long id;

    public Integer getCardId() {
        return card_id;
    }


    public void setText(Integer text) {
        this.card_id = text;
    }

    public void setEmoji(Integer emoji) {
        this.emoji_id = emoji;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setAnimation(Integer animation) {
        this.animation_id = animation;
    }

    public void setUser_id(Long userId) {
        this.user_id = userId;
    }

    public Long getId() {
        return id;
    }
}
