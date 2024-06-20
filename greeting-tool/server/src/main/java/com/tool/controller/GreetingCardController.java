package com.tool.controller;

import com.tool.dto.GreetingCardsDTO;
import com.tool.entity.GreetingCard;
import com.tool.result.PageResult;
import com.tool.result.Result;
import com.tool.service.GreetingCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/greeting_card")
@Api(tags = "Greeting_card-related Interface")
public class GreetingCardController {
    @Autowired
    private GreetingCardService greetingCardService;

    /**
     * Create card
     * @param greetingCardsDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Create card")
    public Result createCard(@RequestBody GreetingCardsDTO greetingCardsDTO) {
        log.info("Create card: {}", greetingCardsDTO);
        //Insert card
        greetingCardService.createCard(greetingCardsDTO);

        return Result.success();
    }

    /**
     * Select cards by user id
     * @param id
     * @return
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
     * @param postcode
     * @return
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
     * @param cardId
     * @return
     */
    @DeleteMapping("/{cardId}")
    @ApiOperation("User delete certain card")
    public Result deleteCardByUser(@PathVariable Long cardId) {
        log.info("Delete card:{} by user.",cardId);

        //Delete card by user
        greetingCardService.deleteByUser(cardId);

        return Result.success();
    }


}
