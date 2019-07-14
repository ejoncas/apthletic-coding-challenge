package com.apthletic.codingchallenge.web;

public class StatusResponse {

    private final String status;

    public static StatusResponse of(String msg) {
        return new StatusResponse(msg);
    }

    private StatusResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
