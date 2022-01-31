package com.unichristus.lit.retinafacil2.Models;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.HashMap;

/**
 * Created by jonathanaj on 23/04/18.
 */

public class Image {

    String date;
    String foto;
    Bitmap bitmap;

    public Image(HashMap<String,String> image) {
        this.date = image.get("data");
        this.foto = image.get("foto");
        this.bitmap = getFotoBitmap(image.get("foto"));
    }

    public Image(String foto,String date) {
        this.date = date;
        this.foto = foto;
        this.bitmap = null;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Bitmap getFotoBitmap(String fotoString){

        byte[] decodedString = Base64.decode(fotoString, Base64.DEFAULT);
        Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return bm;

    }

    public Bitmap getBitmap() {
        return bitmap;
    }


    public void realeseBitmapMemory(){

        if(bitmap != null){

            bitmap.recycle();
            bitmap = null;
        }

    }

    public HashMap<String,String> asHashMap(){

        HashMap<String,String> i = new HashMap<>();

        i.put("date",date);
        i.put("foto",foto);

        return i;
    }
}
