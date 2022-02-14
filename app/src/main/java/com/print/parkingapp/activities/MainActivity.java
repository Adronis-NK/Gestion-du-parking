package com.print.parkingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.print.parkingapp.R;
import com.print.parkingapp.adapter.AdaptaData;
import com.print.parkingapp.api.RetrofitClient;
import com.print.parkingapp.model.AfficherVoiture;
import com.print.parkingapp.model.VoitureReponse;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView welcome;
    private RecyclerView vdata;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;
    private List<AfficherVoiture> listeVoiture = new ArrayList<>();
    private SwipeRefreshLayout swdata;
    private FloatingActionButton addCar;

    private int myId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcome = findViewById(R.id.welcome);
        SharedPreferences sharedPreferences =getSharedPreferences(LoginActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        welcome.setText("Trouver "+sharedPreferences.getInt(LoginActivity.ADMIN_ID, 0));
        myId = sharedPreferences.getInt(LoginActivity.ADMIN_ID, 0);

        vdata = findViewById(R.id.Vdata);

        swdata = findViewById(R.id.swData);



        lmData = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vdata.setLayoutManager(lmData);
        retrieveData();

        swdata.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swdata.setRefreshing(true);
                retrieveData();
                swdata.setRefreshing(false);
            }
        });

        addCar = findViewById(R.id.addCar);
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewCarActivity.class));
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        retrieveData();
    }

    public void retrieveData(){

        Call<VoitureReponse> call = RetrofitClient.getInstance().getMyApi().tvoiture(myId);

        call.enqueue(new Callback<VoitureReponse>() {
            @Override
            public void onResponse(Call<VoitureReponse> call, Response<VoitureReponse> response) {
                int code = response.body().getCode();
                String msg = response.body().getMsg();
                listeVoiture = response.body().getData();
                if (listeVoiture == null){
                    Toast.makeText(MainActivity.this, "Vide", Toast.LENGTH_LONG).show();
                }else {
                    adData = new AdaptaData(MainActivity.this, listeVoiture);
                    vdata.setAdapter(adData);
                    adData.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<VoitureReponse> call, Throwable t) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menumain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout_id:
                logoutUser();
                return true;
            case R.id.setting_id:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void logoutUser() {

        SharedPreferences sharedPreferences =getSharedPreferences(LoginActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        Toast.makeText(this,"Tu es bien deconnecté",Toast.LENGTH_LONG).show();
    }
}