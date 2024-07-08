package com.tool.service;

import com.tool.dto.GreetingCardsDTO;
import com.tool.entity.GreetingCard;

import java.util.ArrayList;

public interface GreetingCardService {
    /**
     * Create card
     * @param greetingCardsDTO
     */
    void createCard(GreetingCardsDTO greetingCardsDTO);

    /**
     * Select cards by user id
     *
     * @param userId
     * @return
     */
    ArrayList<GreetingCard> selectByUserId(Long userId);

    /**
     * Select cards by postcode
     *
     * @param postcode
     * @return
     */

    ArrayList<GreetingCard> selectByPostcode(String postcode);

    /**
     * delete card by users
     * @param cardId
     */

    void deleteByUser(Long cardId);

    /**
     * delete card by user's ID
     * @param userId
     */
    void deleteByUserId(Long userId);
}
