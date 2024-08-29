package com.tool.result;

import lombok.Data;

import java.io.Serializable;

/**
 * results returned by the back-end unification
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; // 1-success,0-failure
    private String msg; // error message
    private T data; // data

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
