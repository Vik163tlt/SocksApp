package me.vik.socksstorageapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Size {
    @JsonProperty("S: 33-37")
    S("33-37"),
    @JsonProperty("M: 38-41")
    M("38-41"),
    @JsonProperty("L: 42-45")
    L("42-45");

    private final String text;

    Size(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
