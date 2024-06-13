package com.tool.service.impl;


import com.tool.dto.GreetingCardsDTO;
import com.tool.mapper.GreetingCardMapper;
import com.tool.service.GreetingCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreetingCardServiceImpl implements GreetingCardService {

   @Autowired
   private GreetingCardMapper greetingCardMapper;

    /**
     * Create card
     * @param greetingCardsDTO
     */
    @Override
    public void createCard(GreetingCardsDTO greetingCardsDTO) {
        //TODO
    }

   /**
    * Select cards by user id
    * @param id
    */
   @Override
    public void selectByUserId(Long id) {
        //TODO
    }

  /**
   * Select cards by postcode
   * @param postcode
   */
   @Override
    public void selectByPostcode(String postcode) {
        //TODO
    }

  /**
   * Delete card by users
   * @param userId
   * @param cardId
   */
   @Override
   public void deleteByUser(Long userId, Long cardId) {
       //TODO
   }

}
