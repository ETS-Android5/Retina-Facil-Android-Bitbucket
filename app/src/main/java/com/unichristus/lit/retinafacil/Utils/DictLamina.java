package com.unichristus.lit.retinafacil.Utils;

import com.unichristus.lit.retinafacil.R;

import java.util.HashMap;

/**
 * Created by lit on 10/05/2018.
 */

public class DictLamina {

    HashMap<String,Integer> dict;

    public DictLamina(){

        dict = new HashMap<>();

        dict.put("imagerd1", R.drawable.imagerd1);
        dict.put("imagerd2", R.drawable.imagerd2);
        dict.put("imagerd3", R.drawable.imagerd3);
        dict.put("imagerd4", R.drawable.imagerd4);
        dict.put("imagerd5", R.drawable.imagerd5);
        dict.put("imagerd6", R.drawable.imagerd6);
        dict.put("imagerd7", R.drawable.imagerd7);
        dict.put("imagerd8", R.drawable.imagerd8);

        dict.put("retino1", R.drawable.retino1);
        dict.put("retino2", R.drawable.retino2);
        dict.put("retino3", R.drawable.retino3);
        dict.put("retino4", R.drawable.retino4);
        dict.put("retino5", R.drawable.retino5);
        dict.put("retino6", R.drawable.retino6);
        dict.put("retino7", R.drawable.retino7);
        dict.put("retino8", R.drawable.retino8);
        dict.put("retino9", R.drawable.retino9);
        dict.put("retino10", R.drawable.retino10);
        dict.put("retino11", R.drawable.retino11);
        dict.put("retino15", R.drawable.retino15);
        dict.put("retino13", R.drawable.retino13);
        dict.put("retino12", R.drawable.retino12);
        dict.put("retino14", R.drawable.retino14);

        dict.put("fundoolho1", R.drawable.fundoolho1);
        dict.put("fundoolho2", R.drawable.fundoolho2);
        dict.put("fundoolho3", R.drawable.fundoolho3);
        dict.put("fundoolho4", R.drawable.fundoolho4);
        dict.put("fundoolho5", R.drawable.fundoolho5);
        dict.put("fundoolho6", R.drawable.fundoolho6);
        dict.put("fundoolho7", R.drawable.fundoolho7);
        dict.put("fundoolho8", R.drawable.fundoolho8);
        dict.put("fundoolho9", R.drawable.fundoolho9);
        dict.put("fundoolho10", R.drawable.fundoolho10);
        dict.put("fundoolho11", R.drawable.fundoolho11);
        dict.put("fundoolho12", R.drawable.fundoolho12);
        dict.put("fundoolho13", R.drawable.fundoolho13);


    }
    public Integer getImage(String name){

        return dict.get(name);
    }

}
