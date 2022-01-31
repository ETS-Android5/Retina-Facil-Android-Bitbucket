package com.unichristus.lit.retinafacil2.Views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.unichristus.lit.retinafacil2.Models.Question;
import com.unichristus.lit.retinafacil2.R;
import com.unichristus.lit.retinafacil2.Utils.DictQuizImage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.widget.ImageView.ScaleType.FIT_CENTER;

public class QuizActivity extends AppCompatActivity {

    ProgressDialog dialog;

    ArrayList<Question>  questions;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth auth;

    TextView title;
    TextView toolbarText,textCorrect,textIncorrect;
    ImageView image;

    ViewFlipper viewFlipper;
    ConstraintLayout layoutPar,layoutImpar,comments;
    LinearLayout options;
    FrameLayout confirmBtn;
    ScrollView scrollView;

    Button advanceBtn;

    boolean par = true;


    int count = 0;
    int correctIndex = 0;
    int correct,incorrect;

    DictQuizImage dictQuizImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("quiz");
        myRef.keepSynced(true);

        auth = FirebaseAuth.getInstance();


        dictQuizImage = new DictQuizImage();

        findViews();

        showLoadingAlert();
        loadQuestions();



    }

    public void findViews(){

        toolbarText = findViewById(R.id.toolbarText);
        textCorrect = findViewById(R.id.correctText);
        textIncorrect = findViewById(R.id.incorrectText);

        viewFlipper = findViewById(R.id.viewFlipper);

        advanceBtn = findViewById(R.id.advancebutton);

        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));

        confirmBtn = findViewById(R.id.btnConfirm);
        layoutPar = findViewById(R.id.questionpar);
        layoutImpar = findViewById(R.id.questionimpar);





    }

    public void loadQuestions(){

        questions = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot questionDataSnapshot : dataSnapshot.getChildren()){

                   questions.add(questionDataSnapshot.getValue(Question.class));

                }

                hideLoadingAlert();

                nextQuestion();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }

    public void nextQuestion(){

        //count++;

        if(count < questions.size()){

            if(par)
                updateLayout(layoutPar);
            else
                updateLayout(layoutImpar);

            //
        }

        else{

            Bundle b = new Bundle();

            Intent i = new Intent(this,ResultActivity.class);

            b.putInt("correct",correct);
            b.putInt("incorrect",incorrect);

            i.putExtras(b);

            startActivity(i);
            finish();
            //toResultActivity
        }




    }

    public void showLoadingAlert(){
        dialog = ProgressDialog.show(QuizActivity.this, "",
                "Carregando...", true);

    }

    public void hideLoadingAlert(){

        dialog.dismiss();
        viewFlipper.setVisibility(View.VISIBLE);

    }

    public void clickOptionLine(View v){


        if(v.getTag().equals("close")){

            for(int i = 0; i < options.getChildCount();i++){

                ((FrameLayout) options.getChildAt(i)).getChildAt(1).animate().translationX(0);
                ((FrameLayout) options.getChildAt(i)).getChildAt(1).setTag("close");
            }

            v.animate().translationX(confirmBtn.getWidth() * (-1));
            v.setTag("open");
        }
        else{
            v.animate().translationX(confirmBtn.getWidth() * (0));
            v.setTag("close");
        }





    }

    public void confirmAnswers(View v){

        comments.setVisibility(View.VISIBLE);

        int index = Integer.parseInt((String) v.getTag());
        TextView option = (TextView) ((LinearLayout)((FrameLayout) options.getChildAt(index)).getChildAt(1)).getChildAt(1);

        String correctAnswer = questions.get(count).getCorrectAnswer();

        if(option.getText().toString().equals(correctAnswer))
            correct++;
        else
            incorrect++;

        for(int i = 0; i < options.getChildCount();i++){

            final LinearLayout linha = (LinearLayout) ((FrameLayout) options.getChildAt(i)).getChildAt(1);
            linha.animate().translationX(0).setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {


                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(scrollView, "scrollY", 0, options.getHeight()).setDuration(250);
                    objectAnimator.start();

                    //scrollView.smoothScrollTo(0,options.getHeight());
                    advanceBtn.setVisibility(View.VISIBLE);

                    linha.animate().setListener(null);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            linha.setTag("close");

            if(((TextView)linha.getChildAt(1)).getText().toString().equals(questions.get(count).getCorrectAnswer())){

                ((FrameLayout) options.getChildAt(i)).setForeground(new ColorDrawable(getResources().getColor(R.color.colorPrimaryTransparet)));

                correctIndex = i;

            }

            //TextView option = (TextView) ((LinearLayout)((FrameLayout) options.getChildAt(index)).getChildAt(1)).getChildAt(1);
        }

        //options.setEnabled(false);

    }

    public void closeQuiz(View v){

        final AlertDialog alerta;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sair do Quiz");
        builder.setMessage("Tem certeza que deseja sair do Quiz?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                finish();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });


        alerta = builder.create();
        alerta.show();
    }

    public void showCorrectAnswer(){



    }

    public void changeLayoutShow(){

        par = !par;

    }

    public void updateLayout(View v){

        title = v.findViewById(R.id.title);
        title.setText(questions.get(count).getTitle());

        scrollView = v.findViewById(R.id.scrollView);

        image = v.findViewById(R.id.zoomageView);
        Glide.with(this).load(dictQuizImage.getDrawable(questions.get(count).getImage())).into(image);
        //image.setImageResource(dictQuizImage.getDrawable(questions.get(count).getImage()));

        comments = v.findViewById(R.id.commentLayout);
        ((TextView) comments.findViewById(R.id.coment)).setText(questions.get(count).getComment());

        options = v.findViewById(R.id.options);
        ArrayList<String> o = questions.get(count).getArrayOptions();

        for(int i = 0; i < options.getChildCount();i++){

            TextView option = (TextView) ((LinearLayout)((FrameLayout) options.getChildAt(i)).getChildAt(1)).getChildAt(1);

            option.setText(o.get(i));
        }

        //image.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fadeoutrotate));

    }

    public void clickAdvance(View v){

        count++;
        changeLayoutShow();

        ((FrameLayout) options.getChildAt(correctIndex)).setForeground(null);

        comments.setVisibility(View.GONE);
        advanceBtn.setVisibility(View.GONE);

        scrollView.scrollTo(0,0);

        image.setScaleType(FIT_CENTER);

        nextQuestion();
        viewFlipper.showNext();

        updateToolbarTexts();

    }

    public void updateToolbarTexts(){

        toolbarText.setText("Questão " + (count+1) + " de " + questions.size());

        textCorrect.setText("" + correct);
        textIncorrect.setText("" + incorrect);
    }

}
