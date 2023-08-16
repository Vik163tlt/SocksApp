package me.vik.socksstorageapp.model;


public enum Color {
    RED("красный"),
    GREEN("зеленый"),
    PINK("розовый"),
    BLUE ("голубой"),
    WHITE("белый"),
    ORANGE("оранжевый"),
    BROWN("коричневый"),
    YELLOW("желтый"),
    VIOLET("фиолетовый"),
    BLACK("черный");

    private final String text;

    Color(String text) {
        this.text = text;
    }
}
