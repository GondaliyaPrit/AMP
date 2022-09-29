package com.amp.Screens;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amp.Data;
import com.amp.Utils;
import com.amp.databinding.ActivitySkucodeBinding;
import com.amp.interface_api.ApiClient;
import com.amp.interface_api.EditData;
import com.amp.models.Skulist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SKUCodeActivity extends AppCompatActivity {
    ActivitySkucodeBinding binding;
    LinearLayoutManager linearLayoutManager;
    Context context;
    ArrayList<Skulist> skulistArrayList;
    String data;
    int SKUID;
    int SKUCuttingID;
    int SizeID;
    String SizeName, qrcodedata;
    int ProcessID;
    int Qtys;
    int sum = 0, qtysum = 0;
    HashMap<Integer, Integer> apimap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivitySkucodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = SKUCodeActivity.this;
        data = SplashActivity.sharedPreferences.getString("data", "");

        Intent intent = getIntent();
        qrcodedata = intent.getStringExtra("qrcodedata");
        if (qrcodedata != null) {
            binding.edtskunumber.setText(qrcodedata);
            Getskudata(Integer.parseInt(qrcodedata), context);
        }

        skulistArrayList = new ArrayList<>();
        binding.serchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edtskunumber.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Enter SKU Code .. !", Toast.LENGTH_SHORT).show();
                } else {
                    int skucode = Integer.parseInt(binding.edtskunumber.getText().toString());
                    Getskudata(skucode, context);
                }
            }
        });
        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("map", "" + apimap);
                int index = 0;
                JSONArray jsonArray = new JSONArray();
                for (Map.Entry<Integer, Integer> entry : apimap.entrySet()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("SizeID", skulistArrayList.get(entry.getKey()).getSizeID());
                        jsonObject.put("Qty", Integer.parseInt(entry.getValue().toString()));
                        jsonArray.put(jsonObject);
                        index++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                UpdateSkuData(jsonArray);
            }
        });

        binding.edtskunumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!v.getText().toString().isEmpty()) {
                        Getskudata(Integer.parseInt(v.getText().toString()), context);
                    } else {
                        Toast.makeText(context, "Please Enter SKU Code", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void UpdateSkuData(JSONArray jsonArray) {
        Data.showdialog(context, "Data Updating...");
        if (Utils.getInstance().isNetworkConnected(this)) {
            Call<ResponseBody> call = ApiClient.API.UpdateSkuData("Bearer " + data, SKUID, SKUCuttingID, jsonArray);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Data.dissmissdialog();
                    try {
                        String responses = response.body().string();
                        Log.e("updatedata", responses);
                        JSONObject jsonObject = new JSONObject(responses);
                        if (jsonObject.getBoolean("flag")) {
                            Toast.makeText(SKUCodeActivity.this, "Data Submit Succesfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SKUCodeActivity.this, "Something gone wrong...", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } else {
            Utils.erroraleart(this, "Check Internet Connection", "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
    }

    public void GetProccessList(int skucode, Context context) {
        Call<ResponseBody> call = ApiClient.API.GetProcessList("Bearer " + data, skucode);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Data.dissmissdialog();
                try {
                    if (!response.message().equals("Unauthorized")) {
                        String processlistresponse = response.body().string();
                        Log.e("Vendorlist", "onResponse:----------------> " + processlistresponse);
                        JSONObject jsonObject = new JSONObject(processlistresponse);
                        boolean flag = jsonObject.getBoolean("flag");
                        String message = jsonObject.getString("message");
                        if (flag) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            if(data.length()> 0){
                                JSONObject dataobject = data.getJSONObject(0);
                                int processid = dataobject.getInt("ProcessID");

                                binding.processlayout.setVisibility(View.VISIBLE);
                                binding.txtprocess.setText("Process Name : " + dataobject.getString("ProcessName"));
                                Log.e("processid", "" + processid);
                            }
                        }
                    } else {
                        Toast.makeText(SKUCodeActivity.this, "Session expire..", Toast.LENGTH_SHORT).show();
                        SplashActivity.editor.clear();
                        SplashActivity.editor.commit();
                        startActivity(new Intent(SKUCodeActivity.this, LoginActivity.class));
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    public void Getskudata(int skucode, Context context) {
        Data.showdialog(context, "Geting Data...");
        if (Utils.getInstance().isNetworkConnected(this)) {
            Call<ResponseBody> call = ApiClient.API.Getskudata("Bearer " + data, skucode);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    binding.rlnodata.setVisibility(View.GONE);
                    try {
                        if (!response.message().equals("Unauthorized")) {
                            GetProccessList(skucode, context);
                            String Vendorresponse = response.body().string();
                            Log.e("Vendorlist", "onResponse:----------------> " + Vendorresponse);
                            JSONObject jsonObject = new JSONObject(Vendorresponse);
                            boolean flag = jsonObject.getBoolean("flag");
                            String message = jsonObject.getString("message");
                            if (flag) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                skulistArrayList.clear();
                                if (data.length() > 0) {
                                    binding.recyclerview.setVisibility(View.VISIBLE);
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
                                        binding.rlsubmitbtn.setVisibility(View.VISIBLE);
                                        binding.totalll.setVisibility(View.VISIBLE);
                                        binding.totalsum.setText("" + sum);
                                        skulistArrayList.add(new Skulist(SKUID, SKUCuttingID, SizeID, SizeName, ProcessID, Qtys, sum));
                                        linearLayoutManager = new LinearLayoutManager(SKUCodeActivity.this, RecyclerView.VERTICAL, false);
                                        binding.recyclerview.showShimmerAdapter();
                                        binding.recyclerview.setLayoutManager(linearLayoutManager);

                                        SKUAdapter skuAdapter = new SKUAdapter(SKUCodeActivity.this, skulistArrayList, new EditData() {
                                            @Override
                                            public void Data(HashMap<Integer, Integer> map) {
                                                qtysum = 0;
                                                for (Map.Entry<Integer, Integer> mapdata : map.entrySet()) {
                                                    qtysum += mapdata.getValue();
                                                }
                                                binding.qtysum.setText("" + qtysum);
                                                apimap = map;
                                            }
                                        });
                                        binding.recyclerview.setAdapter(skuAdapter);
                                    }
                                }

                            } else {
                                binding.rlnodata.setVisibility(View.VISIBLE);
                                binding.recyclerview.setVisibility(View.GONE);
                                binding.rlsubmitbtn.setVisibility(View.GONE);
                                binding.tabtittle.setVisibility(View.GONE);
                                binding.totalll.setVisibility(View.GONE);
                                binding.processlayout.setVisibility(View.GONE);
                                Toast.makeText(SKUCodeActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                Log.e("SKUCODEActivity", "message:----------------> " + message);
                            }
                        } else {
                            Toast.makeText(SKUCodeActivity.this, "Session expire..", Toast.LENGTH_SHORT).show();
                            SplashActivity.editor.clear();
                            SplashActivity.editor.commit();
                            startActivity(new Intent(SKUCodeActivity.this, LoginActivity.class));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Data.dissmissdialog();
                    Log.e("TAG", "error: " + t.getMessage());
                }
            });
        } else {
            Utils.erroraleart(this, "Check Internet Connection", "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
    }
}