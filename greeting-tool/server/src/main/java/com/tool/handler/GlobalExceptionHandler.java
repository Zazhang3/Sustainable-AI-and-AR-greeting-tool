package com.tool.handler;

import com.tool.constant.MessageConstant;
import com.tool.exception.BaseException;
import com.tool.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Global exceptions handler
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * get exception
     * @param ex BaseException
     * @return Result
     */
    @ExceptionHandler
    public Result<Void> exceptionHandler(BaseException ex){
        log.error("Exception Message：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
    /**
     * handle SQL exceptions
     * @param ex SQLIntegrityConstraintViolationException
     * @return Result
     */
    @ExceptionHandler
    public Result<Void> exceptionHandler(SQLIntegrityConstraintViolationException ex){

        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] arr = message.split(" ");
            String username = arr[2];
            String msg = username+ MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else {
            //TODO
            //other SQL errors
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }

    }
}
