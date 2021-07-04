package com.unichristus.lit.retinafacil.Views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.unichristus.lit.retinafacil.Adapters.AdapterGalleryRecycleView;
import com.unichristus.lit.retinafacil.BuildConfig;
import com.unichristus.lit.retinafacil.Models.Image;
import com.unichristus.lit.retinafacil.Models.Paciente;
import com.unichristus.lit.retinafacil.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unichristus.lit.retinafacil.Utils.GlideApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.widget.ImageView.ScaleType.FIT_CENTER;


/**
 * Created by lit on 28/03/2018.
 */

public class PatientActivity extends AppCompatActivity {

    ArrayList<Paciente> pacientes = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth auth;
    ArrayList<String> patientsNames = new ArrayList<>();
    ListView listView;
    ViewFlipper viewFlipper;
    FloatingActionButton addButton;
    FloatingActionMenu addPhoto;
    Paciente currentPatiente;
    ConstraintLayout layoutOptions;
    ImageView photoViewer;
    FrameLayout saveFrameLayout;
    FrameLayout deleteFrameLayout;
    Uri imageUri;
    Context context;
    ArrayList<Integer> selectedIndexes;

    String userId;
    int currentPatientIndex = 0;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public boolean deleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();

        userId = auth.getCurrentUser().getUid();

        Toast.makeText(this,"Carregando Pacientes",Toast.LENGTH_SHORT).show();
        context = this;

        findViews();
        loadPacientes();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currentPatientIndex = position;
                currentPatiente = pacientes.get(position);
                flipNext();
            }
        });
    }


    public void loadPacientes(){


        myRef.child("users").child(userId).child("patients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                clear();

                for(DataSnapshot patientDataSnapshot : dataSnapshot.getChildren()){

                    Paciente patient = new Paciente((HashMap<String,Object>) patientDataSnapshot.getValue());

                    pacientes.add(patient);
                    patientsNames.add(patient.getName());

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,patientsNames){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text = (TextView) view.findViewById(android.R.id.text1);
                        text.setTextColor(Color.BLACK);
                        return view;
                    }
                };

                currentPatiente = pacientes.get(currentPatientIndex);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.child("users").child(userId).child("patients").addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                Paciente patient = new Paciente((HashMap<String,Object>) dataSnapshot.getValue());

                pacientes.set(pacientes.indexOf(currentPatiente),patient);

                currentPatiente = patient;
                reloadGaleria();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }


        });

    }

    public void flipNext(){

        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));

        if(viewFlipper.getDisplayedChild() == 0){

            AnimateButtons(addPhoto,addButton);

            viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));

            reloadGaleria();

        }
        else{

            photoViewer.setScaleType(FIT_CENTER);
            viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.show_from_midle));
            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.stay));

            //addPhoto.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadeoutbutton));
            AnimateButtons(null, addPhoto);


        }

        viewFlipper.showNext();

    }

    public void flipBack(){

        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
        int index = viewFlipper.getDisplayedChild();

        if(viewFlipper.getDisplayedChild() == 1){

            clearGaleria();
            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));

            AnimateButtons(addButton,addPhoto);

        }
        else{
 
            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_from_midle));
            viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.stay));

            AnimateButtons(addPhoto,null);

            if(saveFrameLayout.getVisibility() == View.VISIBLE)
                saveFrameLayout.setVisibility(View.GONE);


            reloadGaleria();
        }


        viewFlipper.showPrevious();


    }

    public void findViews(){


        addButton = findViewById(R.id.floatingActionButton2);

        photoViewer = findViewById(R.id.imageView14);

        listView = findViewById(R.id.listView);
        viewFlipper = findViewById(R.id.viewFlipper);
        layoutOptions = findViewById(R.id.layoutoptions);

        saveFrameLayout = findViewById(R.id.savebar);
        deleteFrameLayout = findViewById(R.id.deletebar);

        mRecyclerView =  findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        addPhoto =  findViewById(R.id.floatingActionMenu);
        configureFloatingMenu();


    }

    public void clickBackGallery(View v){

        //LoadGallery loadGallery = new LoadGallery(this);
        //loadGallery.execute(0);
        flipBack();

    }

    public void clearGaleria(){

        mAdapter = new AdapterGalleryRecycleView();
        mRecyclerView.setAdapter(mAdapter);


    }

    public void openNewPatientDialog(View v){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_newpatient_dialog);

        final EditText patientET = (EditText) dialog.findViewById(R.id.patientET);

        Button cancel = dialog.findViewById(R.id.buttoncancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button create = dialog.findViewById(R.id.buttoncreate);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = patientET.getText().toString();

                if(!name.equals(""))
                    createPatientFirebase(name);
                else
                    Toast.makeText(getApplicationContext(),"Insira o nome do paciente",Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void savePhoto(View v){

        Bitmap bitmap = ((BitmapDrawable)photoViewer.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Image image = new Image(base64,getDate());
        myRef.child("users").child(userId).child("patients").child(currentPatiente.getId()).child("galeria").push().setValue(image);

        Toast.makeText(this,"Imagem Salva", Toast.LENGTH_SHORT).show();

        currentPatiente.getGaleria().add(image);


        saveFrameLayout.setVisibility(View.GONE);

    }

    public void createPatientFirebase(String name){


        String id =  myRef.child("users").child(userId).child("patients").push().getKey();

        myRef.child("users").child(userId).child("patients").child(id).setValue(new Paciente(name,id));




    }

    public void configureFloatingMenu(){

        com.github.clans.fab.FloatingActionButton addCamera = findViewById(R.id.camerafloatbutton);
        addCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoByCamera();
            }
        });

        com.github.clans.fab.FloatingActionButton addGaleria = findViewById(R.id.galeriafloatbutton);
        addGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoByGaleria();
            }
        });

    }

    public void getPhotoByCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.unichristus.lit.retinafacil.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 0);
            }
        }

        saveFrameLayout.setVisibility(View.VISIBLE);
    }
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void getPhotoByGaleria(){

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
//                    Bitmap image = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    GlideApp.with(getApplicationContext()).load(currentPhotoPath).into(photoViewer);
                    flipNext();
                    saveFrameLayout.setVisibility(View.VISIBLE);
                    break;
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){

                    imageUri = imageReturnedIntent.getData();
                    GlideApp.with(getApplicationContext()).load(imageUri).into(photoViewer);

                    flipNext();

                    saveFrameLayout.setVisibility(View.VISIBLE);

                }
                break;
        }
    }

    public void AnimateButtons(final View show, final View hide){

        final Animation fadeInbutton = AnimationUtils.loadAnimation(this, R.anim.fadeinbutton);
        fadeInbutton.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

               if(show != null)
                    show.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Animation fadeOutbutton = AnimationUtils.loadAnimation(this, R.anim.fadeoutbutton);
        fadeOutbutton.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                hide.setVisibility(View.GONE);

                if(show != null){

                    show.setVisibility(View.VISIBLE);
                    show.startAnimation(fadeInbutton);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

       if(hide != null)
           hide.startAnimation(fadeOutbutton);
       else{
           show.startAnimation(fadeInbutton);
       }

    }

    public ImageView getPhotoViewer(){
        return photoViewer;
    }

    public void openDialogDeletePatient(View v){

        final AlertDialog alerta;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar paciente");
        builder.setMessage("Tem certeza que deseja deletar este paciente?");
        builder.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                deletePatient(currentPatiente);
                flipBack();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });


        alerta = builder.create();
        alerta.show();
    }

    public void deletePatient(Paciente p){

        myRef.child("users").child(userId).child("patients").child(p.getId()).removeValue();

    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

        if(viewFlipper.getDisplayedChild() > 0){
            flipBack();
        }
        else{
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
         clear();


    }

    public void clear(){

        for(int i =0; i< pacientes.size(); i++){

            pacientes.get(i).releaseGaleriaBitmaps();
        }

        pacientes.clear();
        patientsNames.clear();

    }

    public String getDate(){

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public void closeActivity(View v){

        finish();
    }


    public void clickDelete(View v){

        //Muda o valor do boolean
        deleteMode = deleteMode == false;

        if(deleteFrameLayout.getVisibility() == View.GONE)
            AnimateButtons(deleteFrameLayout,addPhoto);

        else
            AnimateButtons(addPhoto,deleteFrameLayout);
        reloadGaleria();





    }

    public void reloadGaleria(){

        clearGaleria();

        mAdapter = new AdapterGalleryRecycleView(currentPatiente.getGaleria(),context);
        mRecyclerView.setAdapter(mAdapter);

    }

    public boolean getDeleteMode(){

        return deleteMode;
    }

    public void openDeletePhotoDialog(View v){

        final AlertDialog alerta;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar fptps");
        builder.setMessage("Tem certeza que deseja deletar as fotos selecionadas");
        builder.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                deletePhotos(selectedIndexes);
                reloadGaleria();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });


        alerta = builder.create();
        alerta.show();

    }

    public void deletePhotos(ArrayList<Integer> indexes){


        for(int i = 0; i < indexes.size(); i++){

            int j = indexes.get(i) - i;
            currentPatiente.getGaleria().remove(j);

        }


        myRef.child("users").child(userId).child("patients").child(currentPatiente.getId()).setValue(currentPatiente.asHashMap());


        reloadGaleria();

    }

    public void updateIndexes(ArrayList<Integer> indexes){

        selectedIndexes = indexes;


    }

}
