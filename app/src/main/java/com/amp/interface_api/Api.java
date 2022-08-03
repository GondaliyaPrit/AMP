package com.amp.interface_api;


import com.amp.Screens.SplashActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface Api {
    String token = SplashActivity.sharedPreferences.getString("data", "");


    @FormUrlEncoded
    @POST("api/Login/Login")
    Call<ResponseBody> Login(@Field("Username") String Username,
                             @Field("Password") String Password);


    @GET("api/Fabric/AllColorMaster")
    Call<ResponseBody> FebColor(@Header("Authorization") String tokon);

    @GET("api/Fabric/AllVender")
    Call<ResponseBody> Vendorlist(@Header("Authorization") String tokon);


    @FormUrlEncoded
    @POST("api/Fabric/AddUpdateFabric")
    Call<ResponseBody> Addfebricdata(@Header("Authorization") String tokon,@Field("VenderID") int VenderID,
                                     @Field("Color") int Color,
                                     @Field("imagePath[]") List<String> imagePath,
                                     @Field("Quantity") String Quantity,
                                     @Field("BillNo") int BillNo,
                                     @Field("TakaBalesNo") String TakaBalesNo,
                                     @Field("Status") int Status );

}