package com.unichristus.lit.retinafacil2.Views;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.unichristus.lit.retinafacil2.R;
import com.txusballesteros.widgets.FitChart;



public class ResultActivity extends AppCompatActivity {//} implements OnRestartRequest {


    TextView acertosText, errosText,percetageText;
    int acertos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle b = this.getIntent().getExtras();

        findViews();

        acertos = b.getInt("correct");

        acertosText.setText(acertos + "");
        errosText.setText(b.getInt("incorrect") + "");

        percetageText.setText((5* acertos) + "%");

        inicializeGraphic();
    }

    public void findViews(){

        acertosText = findViewById(R.id.correctTv);
        errosText = findViewById(R.id.incorrectTv);

        percetageText= findViewById(R.id.percetage);

    }

    public void inicializeGraphic(){


        final FitChart fitChart = (FitChart)findViewById(R.id.fitChart);
        fitChart.setValue(5 * acertos);

    }

    public void closeActivity(View v){

        finish();
    }


}



