package com.tool.controller;

import com.tool.dto.GreetingCardsDTO;
import com.tool.entity.GreetingCard;
import com.tool.result.Result;
import com.tool.service.GreetingCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@Slf4j
@RequestMapping("/greeting_card")
@Api(tags = "Greeting_card-related Interface")
public class GreetingCardController {
    @Autowired
    private GreetingCardService greetingCardService;

    /**
     * Create card
     * @param greetingCardsDTO card info
     * @return success/fail
     */
    @PostMapping
    @ApiOperation("Create card")
    public Result<Void> createCard(@RequestBody GreetingCardsDTO greetingCardsDTO) {
        log.info("Create card: {}", greetingCardsDTO);
        //Insert card
        greetingCardService.createCard(greetingCardsDTO);

        return Result.success();
    }

    /**
     * Select cards by user id
     * @param id user id
     * @return cards
     */
    @PostMapping("/user/{id}")
    @ApiOperation("Select by user id")
    public Result<ArrayList<GreetingCard>> selectByUserId(@PathVariable Long id) {
        log.info("Select by user id: {}", id);

        //Select by user id
        ArrayList<GreetingCard> records = greetingCardService.selectByUserId(id);

        //total=1, not divide pages
        return Result.success(records);

    }

    /**
     * Select card by postcode
     * @param postcode postcode
     * @return Arraylist of cards
     */
    @PostMapping("/postcode/{postcode}")
    @ApiOperation("Select by postcode")
    public Result<ArrayList<GreetingCard>> selectByPostcode(@PathVariable String postcode) {
        log.info("Select by postcode: {}", postcode);

        //Select by postcode
        ArrayList<GreetingCard> records = greetingCardService.selectByPostcode(postcode);

        return Result.success(records);
    }

    /**
     * Delete card by users
     * @param cardId card id
     * @return Result
     */
    @DeleteMapping("/{cardId}")
    @ApiOperation("User delete certain card")
    public Result<Void> deleteCardByUser(@PathVariable Long cardId) {
        log.info("Delete card:{} by user.",cardId);

        //Delete card by user
        greetingCardService.deleteByUser(cardId);

        return Result.success();
    }

    @DeleteMapping("/userId/{userId}")
    @ApiOperation("delete all the cards belongs to a user")
    public Result<Void> deleteCardByUserId(@PathVariable Long userId) {
        log.info("Delete user:{} cards",userId);

        greetingCardService.deleteByUserId(userId);

        return Result.success();
    }

    /**
     * count greeting cards by postcode
     * @param postcode :position
     * @return :number of cards
     */
    @PostMapping("/count/{postcode}")
    @ApiOperation("count cards by postcode")
    public Result<Integer> countCardsByPostcode(@PathVariable String postcode) {
        log.info("Count card by postcode: {}", postcode);

        Integer numberOfCards = greetingCardService.countCardsByPostcode(postcode);

        return Result.success(numberOfCards);
    }

}
