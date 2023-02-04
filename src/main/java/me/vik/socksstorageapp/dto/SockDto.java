package me.vik.socksstorageapp.dto;

import lombok.Data;
import me.vik.socksstorageapp.model.Color;
import me.vik.socksstorageapp.model.Size;

@Data
public class SockDto {

    private Color color;

    private Size size;

    private int cottonPart;

    private int quantity;

    public Color getColor() {
        return color;
    }
}
