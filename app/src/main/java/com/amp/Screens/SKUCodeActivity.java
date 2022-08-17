package com.amp.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amp.Data;
import com.amp.Utils;
import com.amp.adapters.FabricListAdapter;
import com.amp.databinding.ActivitySkucodeBinding;
import com.amp.interface_api.ApiClient;
import com.amp.models.Febdata;
import com.amp.models.Skulist;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SKUCodeActivity extends AppCompatActivity {
    ActivitySkucodeBinding binding ;
    LinearLayoutManager  linearLayoutManager ;


    Context context ;
    ArrayList<Skulist> skulistArrayList ;
    String data  ;


    int SKUID ;
    int SKUCuttingID ;
    int SizeID ;
    String SizeName ;
    int ProcessID  ;
    int Qtys ;
    int sum = 0 ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivitySkucodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = SKUCodeActivity.this ;
        data = SplashActivity.sharedPreferences.getString("data",
                "");
        Log.e("ADDFebricScreen ", "setdata: -------------->"+data);
        skulistArrayList = new ArrayList<>();



        binding.serchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              if (binding.edtskunumber.getText().toString().isEmpty())
              {
                  Toast.makeText(context, "Enter SKU Code .. !", Toast.LENGTH_SHORT).show();
              }
              else
              {
                  int skucode = Integer.parseInt(binding.edtskunumber.getText().toString());
                  Getskudata(skucode,context);
              }

            }
        });




        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void Getskudata(int skucode, Context context)
    {
        if (Utils.getInstance().isNetworkConnected(this)) {
            Call<ResponseBody> call = ApiClient.API.Getskudata("Bearer "+data ,skucode);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    binding.recyclerview.setVisibility(View.VISIBLE);
                    try {
                        if(!response.message().equals("Unauthorized")) {
                            String Vendorresponse = response.body().string();
                            Log.e("Vendorlist", "onResponse:----------------> " + Vendorresponse);
                            JSONObject jsonObject = new JSONObject(Vendorresponse);
                            boolean flag = jsonObject.getBoolean("flag");
                            String message = jsonObject.getString("message");
                            if (flag) {
                                JSONArray data = jsonObject.getJSONArray("data");

                                if (data.length() > 0) {
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject dataobject = data.getJSONObject(i);
                                        SKUID = dataobject.getInt("SKUID");
                                        SKUCuttingID = dataobject.getInt("SKUCuttingID");
                                        SizeID = dataobject.getInt("SizeID");
                                        SizeName = dataobject.getString("SizeName");
                                        ProcessID = dataobject.getInt("SizeID");
                                        Qtys = dataobject.getInt("Qty");
                                        sum += Qtys;
                                        binding.tabtittle.setVisibility(View.VISIBLE);
                                        //     binding.submitbtn.setVisibility(View.VISIBLE);
                                        skulistArrayList.add(new Skulist(SKUID, SKUCuttingID, SizeID, SizeName, ProcessID, Qtys, sum));
                                        linearLayoutManager = new LinearLayoutManager(SKUCodeActivity.this, RecyclerView.VERTICAL, false);
                                        binding.recyclerview.showShimmerAdapter();
                                        binding.recyclerview.setLayoutManager(linearLayoutManager);
                                        SKUAdapter skuAdapter = new SKUAdapter(SKUCodeActivity.this, skulistArrayList);
                                        binding.recyclerview.setAdapter(skuAdapter);
                                    }
                                } else {


                                }

                            } else {
                                Toast.makeText(SKUCodeActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                Log.e("AddFabricActivity", "message:----------------> " + message);
                            }
                        }else
                        {
                            Toast.makeText(SKUCodeActivity.this, "Session expire..", Toast.LENGTH_SHORT).show();
                            SplashActivity.editor.clear();
                            SplashActivity.editor.commit();
                            startActivity(new Intent(SKUCodeActivity.this,LoginActivity.class));
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Data.dissmissdialog();
                    Log.e("TAG", "error: "+t.getMessage());
                }
            });
        }
        else {
            Utils.erroraleart(this, "Check Internet Connection", "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
    }

}