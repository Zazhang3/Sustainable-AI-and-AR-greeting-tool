package com.tool.controller.admin;

import com.tool.dto.GreetingCardsDTO;
import com.tool.result.Result;
import com.tool.service.GreetingCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@Slf4j
@RequestMapping("/admin/greeting_card")
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
    @PostMapping("/{id}")
    @ApiOperation("Select by user id")
    public Result selectByUserId(@PathVariable Long id) {
        log.info("Select by user id: {}", id);

        //Select by user id
        greetingCardService.selectByUserId(id);

        return Result.success();

    }

    /**
     * Select card by postcode
     * @param postcode
     * @return
     */
    @PostMapping("/{postcode}")
    @ApiOperation("Select by postcode")
    public Result selectByPostcode(@PathVariable String postcode) {
        log.info("Select by postcode: {}", postcode);

        //Select by postcode
        greetingCardService.selectByPostcode(postcode);

        return Result.success();
    }

    /**
     * Delete card by users
     * @param userId
     * @param cardId
     * @return
     */
    @DeleteMapping("/{userId}/{cardId}")
    @ApiOperation("User delete certain card")
    public Result deleteCardByUser(@PathVariable Long userId,@PathVariable Long cardId) {
        log.info("Delete card:{} by user: {}",cardId,userId);

        //Delete card by user
        greetingCardService.deleteByUser(userId,cardId);

        return Result.success();
    }

}
