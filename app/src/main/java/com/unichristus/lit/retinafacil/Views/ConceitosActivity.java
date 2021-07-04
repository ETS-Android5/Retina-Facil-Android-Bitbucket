package com.unichristus.lit.retinafacil.Views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.unichristus.lit.retinafacil.R;

public class ConceitosActivity extends AppCompatActivity {


    ListView listView;
    ConstraintLayout epidemiologia,fisiopatogenia,classificacao,orientacao;
    String topic[] = {"Epidemiologia e fatores de risco","Fisiopatogenia","Classificação da Retinopatia diabética e Edema macular",
            "Rastreamento e Orientações","Imagens da Retinopatia Diabética"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conceitos);

        findViews();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){

                    case 0:
                        epidemiologia.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        fisiopatogenia.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        classificacao.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        orientacao.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        Intent i = new Intent(getApplicationContext(), LaminasActivity.class);
                        Bundle b = new Bundle();
                        b.putString("title","Imagens da Retinopatia diabética");
                        b.putString("path","retinoplastia/conceitos");

                        i.putExtras(b);

                        startActivity(i);
                        break;
                }

//                currentPatiente = pacientes.get(position);
//                flipNext();
            }
        });
    }

    public void findViews(){

        epidemiologia = findViewById(R.id.epidemiologia);
        fisiopatogenia = findViewById(R.id.fisiopatogenia);
        classificacao = findViewById(R.id.classificação);
        orientacao = findViewById(R.id.orientacao);

        listView = findViewById(R.id.listview);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,topic){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };

        listView.setAdapter(adapter);
    }

    public void closeActivity(View v){
        finish();
    }

    public void closeLayout(View v){

        ((ConstraintLayout) v.getParent()).setVisibility(View.GONE);
    }

}
