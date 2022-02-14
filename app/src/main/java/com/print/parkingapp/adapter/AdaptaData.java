package com.print.parkingapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.print.parkingapp.R;
import com.print.parkingapp.activities.EditCarActivity;
import com.print.parkingapp.activities.Facturer;
import com.print.parkingapp.activities.MainActivity;
import com.print.parkingapp.api.RetrofitClient;
import com.print.parkingapp.model.AfficherFacture;
import com.print.parkingapp.model.AfficherVoiture;
import com.print.parkingapp.model.FactureResponse;
import com.print.parkingapp.model.User;
import com.print.parkingapp.model.VoitureReponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdaptaData extends RecyclerView.Adapter<AdaptaData.HolderData>{
    private Context ctx;
    private int idvx;
    private String vplaquex;
    private List<AfficherVoiture> listget;
    private List<AfficherVoiture> listeVoiture;
    private List<AfficherFacture> listfac;

    public AdaptaData(Context ctx, List<AfficherVoiture> listeVoiture) {
        this.ctx = ctx;
        this.listeVoiture = listeVoiture;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        AfficherVoiture dm = listeVoiture.get(position);

        holder.vId.setText(String.valueOf(dm.getId_frais_parking()));
        holder.vplaque.setText(dm.getPlaque());
        holder.varrive.setText(dm.getArrive());
    }

    @Override
    public int getItemCount() {
        return listeVoiture.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView vId, vplaque, varrive;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            vId = itemView.findViewById(R.id.idVoiture);
            vplaque = itemView.findViewById(R.id.vplaque);
            varrive = itemView.findViewById(R.id.varrive);



            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    vplaquex = vplaque.getText().toString();
                    idvx = Integer.parseInt(vId.getText().toString());


                    AlertDialog.Builder dialogCar = new AlertDialog.Builder(ctx);
                    dialogCar.setMessage(vplaquex+" Options:");
                   // dialogCar.setIcon(R.mipmap.ic_launcher_round);
                    dialogCar.setCancelable(true);

                    dialogCar.setPositiveButton("Facturer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getFacture();
                            dialog.dismiss();
                        }
                    });
                    dialogCar.setNeutralButton("Supprimer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            suppCar();
                            dialog.dismiss();
                            Handler hand = new Handler();
                            hand.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainActivity) ctx).retrieveData();
                                }
                            }, 1000);

                        }
                    });

                    dialogCar.setNegativeButton("Editer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getData();
                            dialog.dismiss();
                        }
                    });

                    dialogCar.show();

                    return false;
                }
            });

        }


        private void suppCar(){
            Call<VoitureReponse> call = RetrofitClient.getInstance().getMyApi().deleteCar(idvx);
            call.enqueue(new Callback<VoitureReponse>() {
                @Override
                public void onResponse(Call<VoitureReponse> call, Response<VoitureReponse> response) {
                    int code = response.body().getCode();
                    String msg = response.body().getMsg();

                    Toast.makeText(ctx, "code "+code+" msg "+msg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<VoitureReponse> call, Throwable t) {
                    Toast.makeText(ctx, "Serveur "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getData(){
            Call<VoitureReponse> call1 = RetrofitClient.getInstance().getMyApi().getVoiture(idvx);

            call1.enqueue(new Callback<VoitureReponse>() {
                @Override
                public void onResponse(Call<VoitureReponse> call, Response<VoitureReponse> response) {
                    int code = response.body().getCode();
                    String msg = response.body().getMsg();
                    listget = response.body().getData();

                    int varIdVtx = listget.get(0).getId_frais_parking();
                    String varPlaqueVtx = listget.get(0).getPlaque();

                    Intent sendEditor = new Intent(ctx, EditCarActivity.class);
                    sendEditor.putExtra("xidCar", varIdVtx);
                    sendEditor.putExtra("xplaqueCar", varPlaqueVtx);
                    ctx.startActivity(sendEditor);

                    Toast.makeText(ctx, "code "+code+" msg "+msg+" id "+varIdVtx+" plaque "+varPlaqueVtx, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<VoitureReponse> call, Throwable t) {
                    Toast.makeText(ctx, "Serveur "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void getFacture(){
            Call<FactureResponse> call2 = RetrofitClient.getInstance().getMyApi().taFacture(idvx);
            call2.enqueue(new Callback<FactureResponse>() {
                @Override
                public void onResponse(Call<FactureResponse> call, Response<FactureResponse> response) {
                    int code = response.body().getCode();
                    String msg = response.body().getMsg();
                    listfac = response.body().getData();

                    int varIdVar = listfac.get(0).getId_frais_parking();
                    String varPlaqueVar = listfac.get(0).getPlaque();
                    String varArriveVar = listfac.get(0).getArrive();
                    String varDepartVar = listfac.get(0).getDepart();
                    String varMontantVar = listfac.get(0).getMontant();
                    String varNumeroRecuVar = listfac.get(0).getNumero_recu();

                    Intent sendFac = new Intent(ctx, Facturer.class);
                    sendFac.putExtra("myid", varIdVar);
                    sendFac.putExtra("myplaque", varPlaqueVar);
                    sendFac.putExtra("myarrive",varArriveVar);
                    sendFac.putExtra("mydepart", varDepartVar);
                    sendFac.putExtra("mymontant", varMontantVar);
                    sendFac.putExtra("myrecu",varNumeroRecuVar);
                    ctx.startActivity(sendFac);

                    Toast.makeText(ctx, "code "+code+" msg "+msg+" id "+varIdVar+" plaque "+varPlaqueVar+" arrive "+varArriveVar+" Depart "+varDepartVar+" Montant "+varMontantVar+" Recu "+varNumeroRecuVar, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<FactureResponse> call, Throwable t) {
                    Toast.makeText(ctx, "Serveur "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
