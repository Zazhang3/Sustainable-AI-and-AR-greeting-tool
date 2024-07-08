package com.tool.service;

import com.tool.dto.GreetingCardsDTO;
import com.tool.entity.GreetingCard;

import java.util.ArrayList;

public interface GreetingCardService {
    /**
     * Create card
     * @param greetingCardsDTO greetingCardsDTO
     */
    void createCard(GreetingCardsDTO greetingCardsDTO);

    /**
     * Select cards by user id
     *
     * @param userId user id
     * @return Arraylist
     */
    ArrayList<GreetingCard> selectByUserId(Long userId);

    /**
     * Select cards by postcode
     * @param postcode postcode
     * @return Arraylist
     */

    ArrayList<GreetingCard> selectByPostcode(String postcode);

    /**
     * delete card by users
     * @param cardId card id
     */

    void deleteByUser(Long cardId);

    /**
     * delete card by user's ID
     * @param userId user id
     */
    void deleteByUserId(Long userId);


    /**
     * count greeting cards by postcode
     * @param postcode position
     * @return number of cards
     */
    int countCardsByPostcode(String postcode);
}
