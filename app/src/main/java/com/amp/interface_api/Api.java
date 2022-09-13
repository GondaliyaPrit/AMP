package com.amp.interface_api;


import com.amp.Screens.SplashActivity;

import org.json.JSONArray;

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
    Call<ResponseBody> Addfebricdata(@Header("Authorization") String tokon, @Field("VenderID") int VenderID,
                                     @Field("Color") int Color,
                                     @Field("imagePath[]") List<String> imagePath,
                                     @Field("Quantity") String Quantity,
                                     @Field("BillNo") int BillNo,
                                     @Field("TakaBalesNo") String TakaBalesNo,
                                     @Field("Status") int Status);

    @GET("api/Fabric/AllFabricList")
    Call<ResponseBody> AllFabricList(@Header("Authorization") String tokon);


    @FormUrlEncoded
    @POST("api/Fabric/DeleteFabric")
    Call<ResponseBody> Delfeb(@Header("Authorization") String tokon,
                              @Field("InFabricID") int InFabricID);


    @FormUrlEncoded
    @POST("api/SKUCutting/GetSKUCuttingProcessDetailV2")
    Call<ResponseBody> Getskudata(@Header("Authorization") String tokon,
                                  @Field("SKUCuttingID") int SKUCuttingID);

    @FormUrlEncoded
    @POST("api/SKUCutting/GetSKUCuttingProcessDetailV2")
    Call<ResponseBody> UpdateSkuData(@Header("Authorization") String tokon,
                                     @Field("SKUID") int SKUID,
                                     @Field("SKUCuttingID") int SKUCuttingID,
                                     @Field("SizeARY[]") JSONArray data);

}

