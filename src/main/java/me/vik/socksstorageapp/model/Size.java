package me.vik.socksstorageapp.model;

public enum Size {
    S("33-37"),
    M("38-41"),
    L("42-45");

    private final String text;

    Size(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
