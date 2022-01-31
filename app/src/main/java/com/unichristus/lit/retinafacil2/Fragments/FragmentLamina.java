package com.unichristus.lit.retinafacil2.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unichristus.lit.retinafacil2.Models.Lamina;
import com.unichristus.lit.retinafacil2.R;
import com.unichristus.lit.retinafacil2.Utils.DictLamina;
import com.unichristus.lit.retinafacil2.Views.LaminasActivity;

/**
 * Created by lit on 10/05/2018.
 */

public class FragmentLamina extends Fragment {


    private View view;
    Context context;
    LaminasActivity main;
    ConstraintLayout cardlayout;
    TextView title,titleShow,comments;
    ImageView imageView,imageViewShow;
    public Lamina lamina;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_lamina, container, false);
        } catch (InflateException e) {

        }

        context = inflater.getContext();

        main = (LaminasActivity) context;

        findViews(view);



        return view;
    }

    public void findViews(View view){

        cardlayout = view.findViewById(R.id.cardlayout);
        title = view.findViewById(R.id.title);
        imageView = view.findViewById(R.id.imageview);


        cardlayout.setScaleX(0.8f);
        cardlayout.setScaleY(0.8f);

        setData(lamina);

    }


    public void setData(Lamina lamina){

        DictLamina dictLamina = new DictLamina();

        title.setText(lamina.getName());
        Glide.with(this).load(dictLamina.getImage(lamina.getImage())).into(imageView);


    }


}