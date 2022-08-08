package com.amp.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.amp.Data;
import com.amp.R;
import com.amp.Utils;
import com.amp.adapters.FabricListAdapter;
import com.amp.databinding.ActivityInfabricBinding;
import com.amp.interface_api.ApiClient;
import com.amp.models.Febdata;
import com.amp.models.Vendorlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfabricActivity extends AppCompatActivity {
    ActivityInfabricBinding binding;
    FabricListAdapter fabricListAdapter;
    Context context;
    String data  ;
    int InFabricID ,VenderID , Color ,BillNo,Status ;
    String imagePath , Quantity ,TakaBalesNo, VenderName, VenderType ,ColorName ;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Febdata> febdatalist  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityInfabricBinding.inflate(getLayoutInflater());
        setdata();
        initclicklistener();
        setContentView(binding.getRoot());
    }

    private void setdata() {
        context = this;
        data = SplashActivity.sharedPreferences.getString("data",
                "");
        Log.e("ADDFebricScreen ", "setdata: -------------->"+data);
        febdatalist = new ArrayList<>();
        GetFebList();

    }

    private void initclicklistener() {

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void GetFebList() {
        Data.showdialog(context,null);
        if (Utils.getInstance().isNetworkConnected(this)) {
            Call<ResponseBody> call = ApiClient.API.AllFabricList("Bearer "+data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Data.dissmissdialog();
                    try {
                        String Vendorresponse =response.body().string();
                        Log.e("Vendorlist", "onResponse:----------------> "+Vendorresponse);
                        JSONObject jsonObject = new JSONObject(Vendorresponse);
                        boolean flag  = jsonObject.getBoolean("flag");
                        String  message  = jsonObject.getString("message");
                        if(flag)
                        {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i =0 ; i<data.length();i++)
                            {
                                JSONObject dataobject = data.getJSONObject(i);
                                InFabricID = dataobject.getInt("InFabricID");
                                VenderID = dataobject.getInt("VenderID");
                                Color = dataobject.getInt("Color");
                                BillNo = dataobject.getInt("BillNo");
                                imagePath = dataobject.getString("imagePath");
                                Quantity = dataobject.getString("Quantity");
                                TakaBalesNo = dataobject.getString("TakaBalesNo");
                                Status = dataobject.getInt("Status");
                                VenderName = dataobject.getString("VenderName");
                                VenderType = dataobject.getString("VenderType");
                                ColorName = dataobject.getString("ColorName");
                                febdatalist.add(new Febdata(InFabricID,VenderID,Color,BillNo,Status,imagePath,Quantity,TakaBalesNo,VenderName,VenderType,ColorName));

                                fabricListAdapter = new FabricListAdapter(context,febdatalist, fabricListAdapter);
                                linearLayoutManager = new LinearLayoutManager(context , RecyclerView.VERTICAL,false);
                                binding.rvlist.setLayoutManager(linearLayoutManager);
                                binding.rvlist.setAdapter(fabricListAdapter);
                            }

                        }
                        else{
                            Toast.makeText(InfabricActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                            Log.e("AddFabricActivity", "message:----------------> "+message);
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