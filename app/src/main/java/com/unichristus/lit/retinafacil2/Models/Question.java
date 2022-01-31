package com.unichristus.lit.retinafacil2.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by lit on 07/05/2018.
 */

public class Question {

    String comment;
    String correct_key;
    String image;
    HashMap<String,String> options;
    String title;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCorrect_key() {
        return correct_key;
    }

    public void setCorrect_key(String correct_key) {
        this.correct_key = correct_key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public HashMap<String, String> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, String> options) {
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCorrectAnswer(){

        return options.get(correct_key);
    }

    public ArrayList<String> getArrayOptions(){

        ArrayList<String> g = new ArrayList<>();

        g.add(options.get("a"));
        g.add(options.get("b"));
        g.add(options.get("c"));
        g.add(options.get("d"));

        Collections.shuffle(g);

        return g;

    }
}
