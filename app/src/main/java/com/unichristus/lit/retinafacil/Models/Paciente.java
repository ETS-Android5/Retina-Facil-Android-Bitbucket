package com.unichristus.lit.retinafacil.Models;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lit on 20/04/2018.
 */

public class Paciente {

    String name;
    ArrayList<Image> galeria;
    String id;

    public Paciente(HashMap<String,Object> dict) {

        this.name = (String) dict.get("name");
        this.id = (String) dict.get("id");

        this.galeria = new ArrayList<>();
        if(dict.get("galeria") != null){
            for(String key : ((HashMap<String,String>) dict.get("galeria")).keySet()){
                HashMap<String,String> galeriaMap = (HashMap<String, String>) ((HashMap<String, Object>) dict.get("galeria")).get(key);
                galeria.add(new Image(galeriaMap));
            }
        }
    }

    public Paciente (String name, String id){
        this.name = name;
        this.id = id;
        galeria = new ArrayList<>();


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Image> getGaleria() {
        return galeria;
    }

    public void setGaleria(ArrayList<Image> images) {

        if(images == null)
            this.galeria = new ArrayList<>();
        else
            this.galeria = galeria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getGalleryArray(){

        ArrayList<String> gallery = new ArrayList<>();

        for(int i = 0; i < galeria.size(); i++){

            gallery.add(galeria.get(i).getFoto());
        }

        return  gallery;
    }

    public void releaseGaleriaBitmaps(){

        for(int i =0; i < galeria.size(); i++){

            galeria.get(i).realeseBitmapMemory();

        }

    }

    public HashMap<String,Object> asHashMap(){

        HashMap<String,Object> p = new HashMap<>();

        p.put("name",this.name);
        p.put("id",this.id);

        HashMap<String,Object> galeriaHash = new HashMap<>();

        for(int i = 0; i < galeria.size(); i++){

            String firebaseId = FirebaseDatabase.getInstance().getReference().push().getKey();
            galeriaHash.put(firebaseId,galeria.get(i).asHashMap());
        }

        p.put("galeria",galeriaHash);

        return  p;
    }

}
