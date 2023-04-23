package com.cs501.finalproject;

import com.squareup.moshi.Json;

public class MyResponse {
    @Json(name = "status")
    private String status;

    @Json(name = "message")
    private String message;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
