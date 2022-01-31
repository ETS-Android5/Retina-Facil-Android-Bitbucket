package com.unichristus.lit.retinafacil2.Utils;

import android.os.AsyncTask;

import com.unichristus.lit.retinafacil2.Views.PatientActivity;

/**
 * Created by lit on 02/05/2018.
 */

public class LoadGallery extends AsyncTask<Integer,Void,Void> {

    PatientActivity activity;

    public LoadGallery(PatientActivity activity){

        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Integer... integers) {

        activity.clearGaleria();
        return null;
    }

    protected void onPreExecute(){

        activity.flipBack();
        //ProgressDialog.show(activity, "", "Carregando galeria");
    }


}
