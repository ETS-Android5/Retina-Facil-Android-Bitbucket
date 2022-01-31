package com.unichristus.lit.retinafacil2.Views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unichristus.lit.retinafacil2.Adapters.AdapterGalleryRecycleView;
import com.unichristus.lit.retinafacil2.Models.Image;
import com.unichristus.lit.retinafacil2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    FirebaseDatabase database;
    DatabaseReference myRef;

    String name;
    String id;
    ArrayList<Image> fotos = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Bundle b = this.getIntent().getExtras();

        //fotos = b.getStringArrayList("pacienteFotos");
        id = b.getString("id");
        name = b.getString("name");


        //paciente = (Paciente) getIntent().getSerializableExtra("paciente");

        findViews();
        loadPhotos();
    }


    public void findViews(){

        mRecyclerView =  findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);



    }


    public void loadPhotos(){

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("patients").child(id).child("galeria").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                //Long i = dataSnapshot.getChildrenCount();

                for(DataSnapshot photoDataSnapshot : dataSnapshot.getChildren()){

                    Image image = new Image((HashMap<String,String>)photoDataSnapshot.getValue());
                    fotos.add(image);

                }


                mAdapter = new AdapterGalleryRecycleView(fotos,getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        });


    }
}
