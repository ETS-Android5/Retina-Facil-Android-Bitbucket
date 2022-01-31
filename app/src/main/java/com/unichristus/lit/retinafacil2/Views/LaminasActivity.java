package com.unichristus.lit.retinafacil2.Views;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unichristus.lit.retinafacil2.Fragments.FragmentLamina;
import com.unichristus.lit.retinafacil2.Models.Lamina;
import com.unichristus.lit.retinafacil2.R;
import com.unichristus.lit.retinafacil2.Utils.DictLamina;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.relex.circleindicator.CircleIndicator;

import static android.widget.ImageView.ScaleType.FIT_CENTER;

/**
 * Created by lit on 10/05/2018.
 */

public class LaminasActivity extends AppCompatActivity {

    ProgressDialog dialog;

    FragmentLamina tab1,tab2,tab3;
    FragmentLamina tabs[];
    ViewPager viewPager;
    int currentIndex = 0;
    boolean first = false;

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth auth;
    ArrayList<Lamina> laminas;

    ConstraintLayout laminaLayout;

    CircleIndicator indicator;

    DictLamina dictLamina;

    TextView title,desciption,titleShow;
    ImageView imageViewShow;

    Button nextbtn,backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laminas);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();

        dictLamina = new DictLamina();

        Bundle b = this.getIntent().getExtras();

        loadLaminas(b.getString("path"));

        findViews();
        title.setText(b.getString("title"));

        viewPager.setOffscreenPageLimit(5);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            if(position == 0 && !first){
                tabs[position].getView().findViewById(R.id.cardlayout).setScaleX(1);
                tabs[position].getView().findViewById(R.id.cardlayout).setScaleY(1);

                first = true;

            }


                //                int width = viewPagerBackground.getWidth();
//                viewPagerBackground.scrollTo((int) (width * position + width * positionOffset), 0);
            }

            @Override
            public void onPageSelected(int position) {

                tabs[currentIndex].getView().findViewById(R.id.cardlayout).animate().scaleX(0.8f).scaleY(0.8f).setDuration(250);

                tabs[position].getView().findViewById(R.id.cardlayout).animate().scaleX(1).scaleY(1).setDuration(250);

                currentIndex = position;

                titleShow.setText(laminas.get(position).getName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void findViews(){

        laminaLayout = findViewById(R.id.laminaLayout);
        viewPager = findViewById(R.id.pager);
        indicator =  findViewById(R.id.indicator);

        title = findViewById(R.id.titleshow);
        titleShow = findViewById(R.id.titleshowlamina);
        imageViewShow = findViewById(R.id.imageviewshow);
        desciption = findViewById(R.id.description);

        nextbtn = findViewById(R.id.nextbtn);
        backbtn = findViewById(R.id.backbtn);

    }


    public void loadLaminas(final String path){

        showLoadingAlert();

        laminas = new ArrayList<>();

        myRef.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot laminaDataSnapshot : dataSnapshot.getChildren()){

                    Lamina lamina = laminaDataSnapshot.getValue(Lamina.class);

                    laminas.add(lamina);
                }

                tabs = new FragmentLamina[laminas.size()];

                if(path.equals("olhonormal")){

                        orderLaminas();
                }


                PagerAdapter adapter = new PagerAdapter
                        (getSupportFragmentManager(), laminas.size());
                viewPager.setAdapter(adapter);
                indicator.setViewPager(viewPager);
                hideLoadingAlert();
                //inicializaTabs();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void updateShow(Lamina lamina){

        titleShow.setText(lamina.getName());
        desciption.setText(lamina.getDescription());
        Glide.with(this).load(dictLamina.getImage(lamina.getImage())).into(imageViewShow);
        imageViewShow.setScaleType(FIT_CENTER);

    }

    public void clickCard(View v){

        openShowLayout();
        
    }

    public void openShowLayout(){

        laminaLayout.setVisibility(View.VISIBLE);
        laminaLayout.startAnimation(AnimationUtils.loadAnimation(this,R.anim.show_from_midle));

        updateShow(laminas.get(viewPager.getCurrentItem()));

        if(viewPager.getCurrentItem() == laminas.size()-1)
            nextbtn.setVisibility(View.GONE);
        else
            nextbtn.setVisibility(View.VISIBLE);

        if(viewPager.getCurrentItem() == 0)
            backbtn.setVisibility(View.GONE);
        else
            backbtn.setVisibility(View.VISIBLE);

    }

    public void closeShowLamina(View v){

        Animation hide = AnimationUtils.loadAnimation(this,R.anim.hide_from_midle);
        hide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                laminaLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        laminaLayout.startAnimation(hide);

    }

    public void nextLamina(View v){

        Animation hide = AnimationUtils.loadAnimation(this,R.anim.hide_from_midle);
        hide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                openShowLayout();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        laminaLayout.startAnimation(hide);

        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);

    }

    public void previusLamina(View v){

        Animation hide = AnimationUtils.loadAnimation(this,R.anim.hide_from_midle);
        hide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                openShowLayout();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        laminaLayout.startAnimation(hide);

        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);

    }

    public void closeActivity(View v){

        finish();
    }

    public void showLoadingAlert(){
        dialog = ProgressDialog.show(LaminasActivity                                                                                                                                                                                                                                                                                   .this, "",
                "Carregando...", true);
        dialog.setCancelable(true);
    }

    public void hideLoadingAlert(){

        dialog.dismiss();


    }

    public class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            //tabs[position].setData(laminas.get(position));
            tabs[position] = new FragmentLamina();
            tabs[position].lamina = laminas.get(position);

            return tabs[position];
        }
        @Override
        public CharSequence getPageTitle(int position) {

            //String[] tabNames = getResources().getStringArray(R.array.tabsNames);
            return "Wololo";
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    public void orderLaminas(){

        Collections.sort(laminas, new Comparator<Lamina>() {
            @Override
            public int compare(Lamina lhs, Lamina rhs) {

                int a = Integer.parseInt(lhs.getImage().substring(9));
                int b = Integer.parseInt(rhs.getImage().substring(9));
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return ((Integer) a).compareTo((Integer) b);
            }
        });

    }


}