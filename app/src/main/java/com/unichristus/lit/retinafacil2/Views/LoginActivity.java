package com.unichristus.lit.retinafacil2.Views;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.unichristus.lit.retinafacil2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by lit on 28/03/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText login,password,email,cadastroPassword,confirmPassword;
    Button cadastrarBtn,loginBtn;
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();

        mAuth = FirebaseAuth.getInstance();

    }


    public void findViews(){

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        email = findViewById(R.id.cadastroEmail);
        cadastroPassword = findViewById(R.id.cadastroPassword);
        confirmPassword = findViewById(R.id.confirmPassword);

        cadastrarBtn = findViewById(R.id.button2);
        loginBtn = findViewById(R.id.cadastroBtn);

        viewFlipper = findViewById(R.id.viewFlipper);
    }

    private boolean isNetworkConnected() {
        // TODO é necessário checar a velocidade de internet?
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return cm.getActiveNetworkInfo() != null && networkInfo.isConnected();
    }

    private boolean validateLoginForm() {
        boolean valid = true;

        if(isNetworkConnected()){
            String email = login.getText().toString();
            if (TextUtils.isEmpty(email)) {
                login.setError("Campo não preenchido");
                valid = false;
            } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                login.setError("Email inválido!");
                valid = false;
            } else {
                login.setError(null);
            }

            String senha = password.getText().toString();
            if (TextUtils.isEmpty(senha)) {
                password.setError("Campo não preenchido");
                valid = false;
            } else {
                password.setError(null);
            }
        } else{
            valid = false;
            Toast.makeText(this, R.string.auth_no_internet_connection,
                    Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    private boolean validateCadastroForm() {
        boolean valid = true;

        if(isNetworkConnected()){
            //Campo Email
            String cadastroEmail = email.getText().toString().trim();
            if (TextUtils.isEmpty(cadastroEmail)) {
                email.setError("Campo não preenchido");
                valid = false;
            } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(cadastroEmail).matches()){
                email.setError("Email inválido!");
                valid = false;
            } else {
                email.setError(null);
            }

            //Campo Senha
            String senha = cadastroPassword.getText().toString().trim();
            if (TextUtils.isEmpty(senha)) {
                cadastroPassword.setError("Campo não preenchido");
                valid = false;
            } else if(senha.length() < 6){
                cadastroPassword.setError("A senha deve conter no mínimo 6 dígitos");
                valid = false;
            } else {
                cadastroPassword.setError(null);
            }

            //Campo Confirma Senha
            String passwordCofirm = confirmPassword.getText().toString().trim();
            if (TextUtils.isEmpty(passwordCofirm)) {
                confirmPassword.setError("Campo não preenchido");
                valid = false;
            } else if(!passwordCofirm.equals(senha)){
                confirmPassword.setError("As senhas estão diferentes.");
                valid = false;
            } else {
                confirmPassword.setError(null);
            }

        } else{
            valid = false;
            Toast.makeText(this, R.string.auth_no_internet_connection,
                    Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    public void clickLogin(View v){

        if(validateLoginForm()){
            mAuth.signInWithEmailAndPassword(login.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("LoginActivity", "signInWithEmail:onComplete:" + task.isSuccessful());

                            Toast.makeText(getApplicationContext(), R.string.auth_sucess,
                                    Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                            finish();

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w("LoginActivity", "signInWithEmail:failed", task.getException());
                                Toast.makeText(getApplicationContext(), R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }


    }

    public void clickCadastrar(View v){

        if(validateCadastroForm()){

            mAuth.createUserWithEmailAndPassword(email.getText().toString(), cadastroPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("LoginActivity", "createUserWithEmail:onComplete:" + task.isSuccessful());


                            Toast.makeText(getApplicationContext(),"Cadastro realizado com sucesso",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }


    }

    public void clickCancel(View v){
        toLogin();
    }

    public void toCadastrar(View v){

        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));

        viewFlipper.showNext();

        email.setText(login.getText().toString());
        cadastroPassword.setText(password.getText().toString());

    }

    public void toLogin(){

        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));

        viewFlipper.showPrevious();


    }
}
