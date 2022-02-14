package com.print.parkingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.print.parkingapp.R;
import com.print.parkingapp.api.RetrofitClient;
import com.print.parkingapp.model.User;
import com.print.parkingapp.model.VoitureReponse;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Facturer extends AppCompatActivity {

    private TextView tplaque;
    private TextView tarrive;
    private TextView tdepart;
    private TextView tmontant;
    private TextView tnumRecu;
    private Button btnImprimer;
    private Button btnAlert;

    private int zidfac;
    private String zplaque;
    private String zarrive;
    private String zdepart;
    private String zpmontant;
    private String znumRecu;

    public static final int PERMISSION_BLUETOOTH = 1;

    private final Locale locale = new Locale("id", "ID");
    private final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", locale);
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturer);

        Intent me = getIntent();
        zidfac = me.getIntExtra("myid",0);
        zplaque = me.getStringExtra("myplaque");
        zarrive = me.getStringExtra("myarrive");
        zdepart = me.getStringExtra("mydepart");
        zpmontant = me.getStringExtra("mymontant");
        znumRecu = me.getStringExtra("myrecu");

        tplaque = findViewById(R.id.affPlaque);
        tarrive = findViewById(R.id.affArrive);
        tdepart = findViewById(R.id.affDepart);
        tmontant = findViewById(R.id.affMontant);
        tnumRecu = findViewById(R.id.affNumRecu);
        btnImprimer = findViewById(R.id.imprimerBtn);
        btnAlert = findViewById(R.id.alertBtn);

        tplaque.setText(zplaque);
        tarrive.setText(zarrive);
        tdepart.setText(zdepart);
        tmontant.setText(zpmontant);
        tnumRecu.setText(znumRecu);



        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerter();
                finish();
            }
        });

    }

    private void alerter(){
        Call<VoitureReponse> call = RetrofitClient.getInstance().getMyApi().alertApi(zidfac);
        call.enqueue(new Callback<VoitureReponse>() {
            @Override
            public void onResponse(Call<VoitureReponse> call, Response<VoitureReponse> response) {
                int code = response.body().getCode();
                String msg = response.body().getMsg();
                Toast.makeText(Facturer.this, "Code "+code+" Msg "+msg+" id "+zidfac, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<VoitureReponse> call, Throwable t) {
                Toast.makeText(Facturer.this, "Serveur "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void doPrint(View view) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, Facturer.PERMISSION_BLUETOOTH);
            } else {
                BluetoothConnection connection = BluetoothPrintersConnections.selectFirstPaired();
                if (connection != null) {
                    EscPosPrinter printer = new EscPosPrinter(connection, 203, 48f, 32);
                    final String text = "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,
                            this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.soco,
                                    DisplayMetrics.DENSITY_LOW, getTheme())) + "</img>\n" +
                            "[L]\n" +
                            "[L]" + df.format(new Date()) + "\n" +
                            "[C]================================\n" +
                            "[L]<b>Paiement parking</b>\n" +
                            "[L]\n" +
                            "[L]PlAQUE:[R]" + zplaque + "\n" +
                            "[L]ARRIVEE: " + zarrive + "\n" +
                            "[L]DEPART: " + zdepart + "\n" +
                            "[C]--------------------------------\n" +
                            "[L]Montant:[R]" + zpmontant + "BIF\n" +
                            "[L]<b>No RECU:[R]" +znumRecu + "</b>\n" +
                            "[C]--------------------------------\n" +
                            "[L]<qrcode>"+znumRecu+"</qrcode>\n"+
                            "[C]--------------------------------\n" +
                            "[L]\n" +
                            "[C]La Mairie est à votre service\n" +
                            "[L]\n" +
                            "[L]===============================\n";


                    printer.printFormattedText(text);
                    finish();
                } else {
                    Toast.makeText(this, "No printer was connected!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("APP", "Can't print", e);
        }
    }

}