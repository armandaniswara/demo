package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
public class BaseResponse<T> {

    @NonNull
    private int status;

    @NonNull
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data = null;

    @Builder
    public BaseResponse(@NonNull int status, @NonNull String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
