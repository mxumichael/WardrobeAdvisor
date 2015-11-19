package com.example.anandchandrasekar.wardrobeadvisor;

import java.util.ArrayList;

/**
 * Created by anandchandrasekar on 11/9/15.
 */
public class Item {
    public static final int STATE_CLEAN = 0;
    public static final int STATE_DIRTY = 1;
    public static final int STATE_INWASH = 2;

    private int id;
    private String name;
    private String color;
    private String size;
    private String type;
    private String weather;
    private String brand;
    private String description;
    private String imagePath;
    private Integer state;

    public Item(int id, String name, String type, String color, String size, String brand, String weather, String description, String image_path) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.size = size;
        this.type = type;
        this.weather = weather;
        this.brand = brand;
        this.imagePath = image_path;
        this.state = STATE_CLEAN;
    }

    public Item(int id, String name, String type, String color, String size, String brand, String weather, String description, String image_path, Integer state) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.size = size;
        this.type = type;
        this.weather = weather;
        this.brand = brand;
        this.imagePath = image_path;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public String getWeather() {
        return weather;
    }

    public String getBrand() {
        return brand;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Integer getState() {
        return state;
    }

    public String getStateName() {
        switch (state){
            case STATE_CLEAN:
                return "Clean";
            case STATE_DIRTY:
                return "Dirty";
            case STATE_INWASH:
                return "In Wash";
        }
        return state.toString();
    }

}
