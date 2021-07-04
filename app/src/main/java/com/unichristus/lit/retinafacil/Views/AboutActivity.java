package com.unichristus.lit.retinafacil.Views;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.unichristus.lit.retinafacil.R;
import com.google.firebase.database.FirebaseDatabase;

public class AboutActivity extends AppCompatActivity {


    ConstraintLayout infos[];
    ViewFlipper viewFlipper;
    private float initialX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

      viewFlipper = findViewById(R.id.viewFlipper);

      infos = new ConstraintLayout[3];
      infos[0] = findViewById(R.id.layout1);
      infos[1] = findViewById(R.id.layout2);
      infos[2] = findViewById(R.id.layout3);
    }

    public void closeActivity(View v){

        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {

        int x = viewFlipper.getRight();
        int y = viewFlipper.getBottom();

        int startRadius = x/2 *(-1);
        int endRadius = (int) Math.hypot(viewFlipper.getWidth(), viewFlipper.getHeight());





        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = touchevent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = touchevent.getX();
                if (initialX > finalX) {
                    if (viewFlipper.getDisplayedChild() == 2)
                        break;

                    Animator anim = ViewAnimationUtils.createCircularReveal(infos[viewFlipper.getDisplayedChild()+1], x, y, startRadius, endRadius);
                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.fadeinbutton));
                    viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.fadeoutbutton));
                    viewFlipper.showNext();
                    anim.start();

                } else {
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;

                    Animator anim = ViewAnimationUtils.createCircularReveal(infos[viewFlipper.getDisplayedChild()-1], x, y, startRadius, endRadius);
                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.fadeinbutton));
                    viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.fadeoutbutton));
                    viewFlipper.showPrevious();
                    anim.start();
                }
                break;
        }
        return false;
    }



}
