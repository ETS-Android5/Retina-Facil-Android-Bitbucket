package com.unichristus.lit.retinafacil2.Models;

/**
 * Created by lit on 10/05/2018.
 */

public class Lamina {

    String description;
    String image;
    String name;

    public Lamina(String description, String image, String name) {
        this.description = description;
        this.image = image;
        this.name = name;
    }

    public Lamina() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
