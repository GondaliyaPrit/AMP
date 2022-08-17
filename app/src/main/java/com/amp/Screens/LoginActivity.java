package com.amp.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amp.Data;
import com.amp.Utils;
import com.amp.databinding.ActivityLoginscreenBinding;
import com.amp.interface_api.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginscreenBinding binding ;
    String email ;
    String password ;
    Context context;
    String message ;
    String data ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding =ActivityLoginscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        binding.btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkvalidation();
            }
        });
    }

    void checkvalidation() {
        email = binding.edtemail.getText().toString();
        password = binding.edtpassword.getText().toString();
        if (email.isEmpty()) {
            binding.edtemail.setError("Enter Email");
            binding.edtemail.requestFocus();
        } else if (password.isEmpty()) {
            binding.edtpassword.requestFocus();
            binding.edtpassword.setError("Enter password");
        } else {
            loginapi();
        }
    }


    void loginapi() {
        if (Utils.getInstance().isNetworkConnected(this)) {
            Data.showdialog(context, "Checking Your Data..");
            Log.e("TAG", "loginapi: "+email );
            Log.e("TAG", "loginapi: "+password );
            Call<ResponseBody> call = ApiClient.API.Login(email,password);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Data.dissmissdialog();
                    try {
                        if(!response.message().equals("Unauthorized")) {
                            String loginresponse = response.body().string();
                            Log.e("TAG", "loginres: " + loginresponse);
                            JSONObject loginjson = new JSONObject(loginresponse);
                            boolean flag = loginjson.getBoolean("flag");
                            Log.e("Flag", "" + flag);
                            message = loginjson.getString("message");
                            if (flag) {
                                data = loginjson.getString("data");
                                SplashActivity.editor.putString("name", email);
                                SplashActivity.editor.putString("data", data);
                                SplashActivity.editor.commit();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }else
                        {
                            Toast.makeText(LoginActivity.this, "Session expire..", Toast.LENGTH_SHORT).show();
                            SplashActivity.editor.clear();
                            SplashActivity.editor.commit();
                            startActivity(new Intent(LoginActivity.this,LoginActivity.class));
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Log.e("Error",e.getMessage());
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

