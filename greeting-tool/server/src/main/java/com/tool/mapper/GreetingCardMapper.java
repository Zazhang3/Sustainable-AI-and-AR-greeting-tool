package com.tool.mapper;

import com.tool.entity.GreetingCard;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface GreetingCardMapper {

    /**
     * Create a new greeting card
     * @param greetingCard
     */
    @Insert("INSERT INTO greeting_cards (card_id, postcode, user_id, emoji_id, animation_id) " +
            "VALUES (#{card_id},#{postcode},#{user_id},#{emoji_id},#{animation_id})")
    void createCard(GreetingCard greetingCard);

    /**
     * Find all cards a user created(select)
     * @param id
     * @return
     */
    @Select("SELECT * FROM greeting_cards WHERE user_id=#{id}")
    @Results({
            @Result(property = "card_id", column = "card_id", javaType = String.class),
            @Result(property = "user_id", column = "user_id", javaType = Long.class),
            @Result(property = "emoji_id", column = "emoji_id", javaType = String.class),
            @Result(property = "animation_id", column = "animation_id",javaType = String.class)
    })
    ArrayList<GreetingCard> getByUserId(Long id);

    /**
     * Find cards based on postcode(select)
     * @param postcode
     * @return
     */
    @Select("SELECT * FROM greeting_cards WHERE postcode=#{postcode}")
    @Results({
            @Result(property = "card_id", column = "card_id", javaType = String.class),
            @Result(property = "user_id", column = "user_id", javaType = Long.class),
            @Result(property = "emoji_id", column = "emoji_id", javaType = String.class),
            @Result(property = "animation_id", column = "animation_id",javaType = String.class)
    })
    ArrayList<GreetingCard> getByPostcode(String postcode);

    /**
     * Find cards by card_id
     * @param cardId
     * @return
     */
    @Select("SELECT * FROM greeting_cards WHERE id=#{cardId}")
    GreetingCard getByCardId(Long cardId);

    /**
     * Delete card by card_id
     * @param cardId
     */
    @Delete("DELETE FROM greeting_cards WHERE id=#{cardId}")
    void deleteCardByUser(Long cardId);
}
