package com.unichristus.lit.retinafacil.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.unichristus.lit.retinafacil.Models.Image;
import com.unichristus.lit.retinafacil.R;
import com.unichristus.lit.retinafacil.Views.PatientActivity;

import java.util.ArrayList;

/**
 * Created by evera on 06/02/2018.
 */

public class AdapterGalleryRecycleView extends RecyclerView.Adapter{

    private ArrayList<Image> mDataset;
    private Context context;
    private ArrayList<Integer> imagesIndex;
    private ArrayList<ImageView> allImages;

    public AdapterGalleryRecycleView(ArrayList<Image> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
        this.imagesIndex = new ArrayList<>();
        this.allImages = new ArrayList<>();
    }

    public AdapterGalleryRecycleView(){
        this.mDataset = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.line_gallery_layout, parent, false);

        final materialViewHolder holder = new materialViewHolder(view);

        final PatientActivity patientActivity = (PatientActivity) context;

        Boolean deleteMode = patientActivity.getDeleteMode();

        if(deleteMode){

            holder.checkbox.setVisibility(View.VISIBLE);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ImageView image = v.findViewById(R.id.image);

                    int index = allImages.indexOf(image);

                    if(imagesIndex.indexOf(index) == -1){

                        imagesIndex.add(index);
                        v.findViewById(R.id.image).setScaleX(0.8f);
                        v.findViewById(R.id.image).setScaleY(0.8f);

                        ((CheckBox)v.findViewById(R.id.checkBox)).setChecked(true);
                        //((ImageView)v.findViewById(R.id.checkbox)).setImageResource(R.drawable.ic_checkbox_marked);
                        //((ImageView)v.findViewById(R.id.checkbox)).setTi

                    }
                    else{

                        imagesIndex.remove(index);
                        v.findViewById(R.id.image).setScaleX(1f);
                        v.findViewById(R.id.image).setScaleY(1f);

                        ((CheckBox)v.findViewById(R.id.checkBox)).setChecked(false);
                        //((ImageView)v.findViewById(R.id.checkBox)).setImageResource(R.drawable.ic_checkbox_blank);
                    }

                    patientActivity.updateIndexes(imagesIndex);

                }
            });
        }

        else{

            holder.image.setScaleX(1f);
            holder.image.setScaleY(1f);

            holder.checkbox.setVisibility(View.GONE);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Glide.with(context).load(mDataset.get(holder.getLayoutPosition()).getBitmap()).into(patientActivity.getPhotoViewer());

                    //((ZoomageView)patientActivity.getPhotoViewer()).setImageMatrix(new Matrix());
                    patientActivity.flipNext();
                }
            });
        }


        if(mDataset.size() != 0)
            return holder;
        else
            return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        materialViewHolder materialHolder = (materialViewHolder) holder;
        Image image = mDataset.get(position);

        Glide.with(context).load(image.getBitmap()).into(materialHolder.image);

        //materialHolder.image.setTag(position);
        allImages.add( materialHolder.image);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class materialViewHolder extends RecyclerView.ViewHolder {

        final ImageView image;
        final CheckBox checkbox;

        public materialViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.image);
            //checkbox = view.findViewById(R.id.checkbox);
            checkbox = view.findViewById(R.id.checkBox);

        }

    }

    private ArrayList<Integer> getImagesIndex() {
        return imagesIndex;
    }

}
