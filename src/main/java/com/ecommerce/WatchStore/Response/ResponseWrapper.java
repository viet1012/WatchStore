package com.ecommerce.WatchStore.Response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
@Getter
@Setter
public class ResponseWrapper<T> {

    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("isSuccess")
    private boolean isSuccess;

    @JsonProperty("result")
    private T result;

    public ResponseWrapper(int statusCode, String message, boolean isSuccess, T result) {
        this.statusCode = statusCode;
        this.message = message;
        this.isSuccess = isSuccess;
        this.result = result;
    }


}
