package com.print.parkingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.print.parkingapp.R;
import com.print.parkingapp.api.RetrofitClient;
import com.print.parkingapp.model.VoitureReponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCarActivity extends AppCompatActivity {

    private EditText plaqueCar;
    private Button NewCarBtn;
    private String myPlaque;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcar);
        SharedPreferences sharedPreferences =getSharedPreferences(LoginActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt(LoginActivity.ADMIN_ID, 0);
        plaqueCar = findViewById(R.id.plaqueCar);
        NewCarBtn = findViewById(R.id.NewCarBtn);

        NewCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPlaque = plaqueCar.getText().toString();
                if (myPlaque.trim().equals("")){
                    plaqueCar.setError("Remplissez cette case");
                }else {
                    saveCar();
                }
            }
        });
    }
    private void saveCar(){
        Call<VoitureReponse> call = RetrofitClient.getInstance().getMyApi().enreNouvVoiture(myPlaque, user_id);
        call.enqueue(new Callback<VoitureReponse>() {
            @Override
            public void onResponse(Call<VoitureReponse> call, Response<VoitureReponse> response) {
                int code = response.body().getCode();
                String msg = response.body().getMsg();

                Toast.makeText(NewCarActivity.this,"Code "+code+" Msg "+msg, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<VoitureReponse> call, Throwable t) {
                Toast.makeText(NewCarActivity.this,"Code "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}