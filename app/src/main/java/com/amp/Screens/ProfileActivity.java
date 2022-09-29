package com.amp.Screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.amp.Data;
import com.amp.databinding.ActivityProfileBinding;
import com.amp.interface_api.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    Context context;
    String data;
    String username ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setdata();
        initclicklistener();
        setContentView(binding.getRoot());

    }

    private void setdata() {
        context = this;
        data = SplashActivity.sharedPreferences.getString("data", "");
        username = SplashActivity.sharedPreferences.getString("name", "");
        getuserdata();
    }

    private void initclicklistener() {
        binding.signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SplashActivity.editor.clear();
                SplashActivity.editor.commit();
                startActivity(new Intent(context,LoginActivity.class));
                finish();
            }
        });

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,HomeActivity.class));
                finish();
            }
        });
    }



    private void getuserdata(){
        Data.showdialog(context,"Getting Data...");
        Call<ResponseBody> call = ApiClient.API.GetUserDetails("Bearer " + data,username );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Data.dissmissdialog();
                try {
                    String userresponse = response.body().string();
                    Log.e("response",userresponse);
                    JSONObject jsonObject = new JSONObject(userresponse);
                    boolean flag = jsonObject.getBoolean("flag");
                    if(flag){
                        JSONObject dataobject = jsonObject.getJSONObject("data");
                        String name = dataobject.getString("Name");
                        String username = dataobject.getString("Username");
                        String vendorname = dataobject.getString("VenderName");
                        String vendortype = dataobject.getString("VenderType");
                        binding.txtname.setText(name);
                        binding.txtusername.setText(username);
                        binding.txtvendorname.setText(vendorname);
                        binding.txtvendortype.setText(vendortype);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TAG", "onResponse: "+t.toString());

            }
        });

    }


}