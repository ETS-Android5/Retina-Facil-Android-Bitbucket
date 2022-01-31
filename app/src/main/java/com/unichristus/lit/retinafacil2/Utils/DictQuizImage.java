package com.unichristus.lit.retinafacil2.Utils;

import com.unichristus.lit.retinafacil2.R;

import java.util.HashMap;

/**
 * Created by lit on 08/05/2018.
 */

public class DictQuizImage {

    HashMap<String,Integer> dict;
    HashMap<String,Integer> dictIndex;


    public DictQuizImage() {

        dict = new HashMap<>();
        dictIndex = new HashMap<>();

        dict.put("quiz1", R.drawable.quiz1);
        dict.put("quiz2", R.drawable.quiz2);
        dict.put("quiz3", R.drawable.quiz3);
        dict.put("quiz4", R.drawable.quiz4);
        dict.put("quiz5", R.drawable.quiz5);
        dict.put("quiz6", R.drawable.quiz6);
        dict.put("quiz7", R.drawable.quiz7);
        dict.put("quiz8", R.drawable.quiz8);
        dict.put("quiz9", R.drawable.quiz9);
        dict.put("quiz10", R.drawable.quiz10);
        dict.put("quiz11", R.drawable.quiz11);
        dict.put("quiz12", R.drawable.quiz12);
        dict.put("quiz13", R.drawable.quiz13);
        dict.put("quiz14", R.drawable.quiz14);
        dict.put("quiz15", R.drawable.quiz15);
        dict.put("quiz16", R.drawable.quiz16);
        dict.put("quiz17", R.drawable.quiz17);
        dict.put("quiz18", R.drawable.quiz18);
        dict.put("quiz19", R.drawable.quiz19);
        dict.put("quiz20", R.drawable.quiz20);

        dictIndex.put("a", 0);
        dictIndex.put("b", 1);
        dictIndex.put("c", 2);
        dictIndex.put("d", 3);

    }

    public Integer getDrawable(String quiz){

        return dict.get(quiz);
    }

    public Integer getIndex(String op){

        int i = new Integer(dictIndex.get(op));

        return i;
    }
}
