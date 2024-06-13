package com.tool.service;

import com.tool.dto.GreetingCardsDTO;

public interface GreetingCardService {
    /**
     * Create card
     * @param greetingCardsDTO
     */
    void createCard(GreetingCardsDTO greetingCardsDTO);

    /**
     * Select cards by user id
     * @param id
     */
    void selectByUserId(Long id);

    /**
     * Select cards by postcode
     * @param postcode
     */

    void selectByPostcode(String postcode);

    /**
     * delete card by users
     * @param userId
     * @param cardId
     */

    void deleteByUser(Long userId, Long cardId);
}
