package com.print.parkingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.print.parkingapp.R;
import com.print.parkingapp.api.RetrofitClient;
import com.print.parkingapp.model.User;
import com.print.parkingapp.model.VoitureReponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCarActivity extends AppCompatActivity {
    private int xidCar;
    private String xplaqueCar;
    private EditText etplaque;
    private Button edtBtn;
    private String yplaque;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        Intent mine = getIntent();
        xidCar = mine.getIntExtra("xidCar",0);
        xplaqueCar = mine.getStringExtra("xplaqueCar");

        etplaque = findViewById(R.id.plaqueCarEdit);
        edtBtn = findViewById(R.id.editCarBtn);

        etplaque.setText(xplaqueCar);

        edtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yplaque = etplaque.getText().toString();
                if(yplaque.equals("")){
                    etplaque.setError("Champ Oblogatoire");
                }else{
                    updateData();
                }

            }
        });
    }

    private void updateData(){
        Call<VoitureReponse> call = RetrofitClient.getInstance().getMyApi().updateCar(xidCar, yplaque);
        call.enqueue(new Callback<VoitureReponse>() {
            @Override
            public void onResponse(Call<VoitureReponse> call, Response<VoitureReponse> response) {
                int code = response.body().getCode();
                String msg = response.body().getMsg();

                Toast.makeText(EditCarActivity.this, "Code "+code+" msg "+msg, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<VoitureReponse> call, Throwable t) {
                Toast.makeText(EditCarActivity.this, "Localisation "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}