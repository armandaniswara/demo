package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NonNull;

public class BaseResponse<T> {

    @NonNull
    private int status;

    @NonNull
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data = null;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public @NonNull String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
