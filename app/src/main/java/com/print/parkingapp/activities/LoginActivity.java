package com.print.parkingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.print.parkingapp.R;
import com.print.parkingapp.api.RetrofitClient;
import com.print.parkingapp.api.ServiceApi;
import com.print.parkingapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin;

    public final static String SHARED_PREF_NAME = "log_user_info";
    public final static String ADMIN_ID = "admin_id";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editTextEmail = findViewById(R.id.user_email);
        editTextPassword = findViewById(R.id.user_pwd);
        buttonLogin = findViewById(R.id.btn_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }





    private void loginUser(){
        String email_user = editTextEmail.getText().toString();
        String pwd_user = editTextPassword.getText().toString();

        if (email_user.isEmpty()){
            editTextEmail.setError("Saisissez l'email");
            editTextEmail.requestFocus();
            return;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email_user).matches())
        {
            editTextEmail.setError("Saisissez l'email valide");
            editTextEmail.requestFocus();
            return;
        }
        else if (pwd_user.isEmpty())
        {
            editTextPassword.setError("Saisissez le mot de passe");
            editTextPassword.requestFocus();
            return;
        }else{

            Call<User> call = RetrofitClient.getInstance().getMyApi().doLogin(email_user, pwd_user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body().getResponse().equals("data")){
                        int getIdUser = response.body().getAdmin_id();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(ADMIN_ID, getIdUser);
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this,"Erreur du mot de passe ou email",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this,"Localisation "+t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });


        }
    }




}