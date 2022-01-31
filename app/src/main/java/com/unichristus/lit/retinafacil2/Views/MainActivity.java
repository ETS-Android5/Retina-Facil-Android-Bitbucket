package com.unichristus.lit.retinafacil2.Views;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.unichristus.lit.retinafacil2.R;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


    public void clickCapturar(View v){

        Intent i = new Intent(this, PatientActivity.class);
        startActivity(i);
    }

    public void clickFundoDeOlho(View v){

        Intent i = new Intent(this, LaminasActivity.class);
        Bundle b = new Bundle();

        b.putString("title","Fundo de olho normal");
        b.putString("path","olhonormal");

        i.putExtras(b);

        startActivity(i);
    }

    public void clickRetinopatia(View v){

        Intent i = new Intent(this, RetinopatiaActivity.class);
        startActivity(i);
    }

    public void clickTestes(View v){



        Intent i = new Intent(this, QuizActivity.class);
        startActivity(i);
    }

    public void clickInformacoes(View v){



        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    public void clickConceitos(View v){

        Intent i = new Intent(this, ConceitosActivity.class);
        startActivity(i);
    }
}
