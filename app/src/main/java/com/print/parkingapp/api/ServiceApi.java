package com.print.parkingapp.api;

import com.print.parkingapp.model.FactureResponse;
import com.print.parkingapp.model.User;
import com.print.parkingapp.model.VoitureReponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceApi {

    String BASE_URL = "http://net-telecom.net/SOCOTECA/apiandroadrothi/tijo/";

    @GET("login.php")
    Call<User> doLogin(@Query("email") String email, @Query("password") String password);

    @FormUrlEncoded
    @POST("afficherVoiture.php")
    Call<VoitureReponse> tvoiture(
            @Field("admin_id") int admin_id
    );

    @FormUrlEncoded
    @POST("saveNewCar.php")
    Call<VoitureReponse> enreNouvVoiture(
            @Field("plaque") String plaque,
            @Field("admin_id") int admin_id
    );

    @FormUrlEncoded
    @POST("deleteCar.php")
    Call<VoitureReponse> deleteCar(
            @Field("id_frais_parking") int id_frais_parking
    );


    @FormUrlEncoded
    @POST("getVoiture.php")
    Call<VoitureReponse> getVoiture(
            @Field("id_frais_parking") int id_frais_parking
    );

    @FormUrlEncoded
    @POST("updateCar.php")
    Call<VoitureReponse> updateCar(
            @Field("id_frais_parking") int id_frais_parking,
            @Field("plaque") String plaque
    );

    @FormUrlEncoded
    @POST("facture.php")
    Call<FactureResponse> taFacture(
            @Field("id_frais_parking") int id_frais_parking
    );

    @FormUrlEncoded
    @POST("alerter.php")
    Call<VoitureReponse> alertApi(
            @Field("id_frais_parking") int id_frais_parking
    );
}
