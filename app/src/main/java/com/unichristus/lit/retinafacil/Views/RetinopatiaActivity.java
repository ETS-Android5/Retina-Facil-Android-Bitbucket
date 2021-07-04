package com.unichristus.lit.retinafacil.Views;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.unichristus.lit.retinafacil.R;

public class RetinopatiaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retinopatia);

    }


    public void clickProliferativa(View v){

        Intent i = new Intent(this, LaminasActivity.class);
        Bundle b = new Bundle();

        b.putString("title","Retinopatia Diabética Proliferativa");
        b.putString("path","retinoplastia/proliferativa");

        i.putExtras(b);

        startActivity(i);
    }

    public void clickNaoproliferativa(View v){

        Intent i = new Intent(this, LaminasActivity.class);
        Bundle b = new Bundle();

        b.putString("title","Retinopatia Diabética não Proliferativa");
        b.putString("path","retinoplastia/naoproliferativa");

        i.putExtras(b);

        startActivity(i);
    }

    public void closeActivity(View v){

        finish();
    }

}
