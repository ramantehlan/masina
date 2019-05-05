package com.example.angelhack19;

import com.google.gson.annotations.SerializedName;

public class RetroPhoto {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public RetroPhoto(String message) {
        this.message = message;
    }
}
