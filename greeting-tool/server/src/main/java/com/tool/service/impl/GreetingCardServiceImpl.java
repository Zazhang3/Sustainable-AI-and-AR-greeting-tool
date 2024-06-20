package com.tool.service.impl;


import com.tool.constant.MessageConstant;
import com.tool.dto.GreetingCardsDTO;
import com.tool.entity.GreetingCard;
import com.tool.exception.GreetingCardNotFoundException;
import com.tool.mapper.GreetingCardMapper;
import com.tool.service.GreetingCardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

        GreetingCard newGreetingCard = new GreetingCard();
        BeanUtils.copyProperties(greetingCardsDTO,newGreetingCard);
        greetingCardMapper.createCard(newGreetingCard);

    }

   /**
    * Select cards by user id
    *
    * @param userId
    * @return
    */
   @Override
    public ArrayList<GreetingCard> selectByUserId(Long userId) {

       ArrayList<GreetingCard> greetingCards= new ArrayList<>();
       greetingCards = greetingCardMapper.getByUserId(userId);

       return greetingCards;
   }

  /**
   * Select cards by postcode
   *
   * @param postcode
   * @return
   */
   @Override
    public ArrayList<GreetingCard> selectByPostcode(String postcode) {

       ArrayList<GreetingCard> greetingCards= new ArrayList<>();
       greetingCards = greetingCardMapper.getByPostcode(postcode);

       return greetingCards;
   }

    /**
     * Delete card by users
     * @param cardId
     */
   @Override
   public void deleteByUser(Long cardId) {
       GreetingCard greetingCard = greetingCardMapper.getByCardId(cardId);

       if(greetingCard == null) {
           throw new GreetingCardNotFoundException(MessageConstant.CARD_NOT_FOUND);
       }
       greetingCardMapper.deleteCardByUser(cardId);
   }

}
