package com.amp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amp.Data;
import com.amp.R;
import com.amp.Screens.AddFabricActivity;
import com.amp.Screens.EditFabricActivity;
import com.amp.Screens.InfabricActivity;
import com.amp.Screens.LoginActivity;
import com.amp.Screens.SplashActivity;
import com.amp.Utils;
import com.amp.interface_api.ApiClient;
import com.amp.models.Febdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FabricListAdapter extends RecyclerView.Adapter<FabricListAdapter.ListClass> {
    Context context;
    ArrayList<Febdata> febdatalist;
    String data;
    FabricListAdapter fabricListAdapter ;
    int InFabricID ,VenderID , Color ,BillNo,Status ;
    String imagePath , Quantity ,TakaBalesNo, VenderName, VenderType ,ColorName ;
    LinearLayoutManager linearLayoutManager;


    public FabricListAdapter(Context context, ArrayList<Febdata> febdatalist, FabricListAdapter fabricListAdapter) {
        this.context = context;
        this.febdatalist = febdatalist;
       this.fabricListAdapter = fabricListAdapter ;
    }

    @NonNull
    @Override
    public ListClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.infabric_items, parent, false);
        ListClass listClass = new ListClass(view);
        return listClass;
    }

    @Override
    public void onBindViewHolder(@NonNull ListClass holder, int position) {
        int pos = position;
        holder.txtvendorname.setText(febdatalist.get(pos).getVenderName());
        holder.colorname.setText(febdatalist.get(pos).getColorName());
        holder.Quantitycount.setText(febdatalist.get(pos).getQuantity());
        holder.Txttakebaseno.setText(febdatalist.get(pos).getTakaBalesNo());
//        String[] imageath = febdatalist.get(pos).getImagePath().split(",");
        byte[] decodedString = Base64.decode(febdatalist.get(pos).getImagePath(), Base64.DEFAULT);
        String text = new String(decodedString, StandardCharsets.UTF_8);
        Log.e("demo",text);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        holder.febimage.setImageBitmap(decodedByte);

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.showdialog(context,"Data Deleting...");
                data = SplashActivity.sharedPreferences.getString("data", "");
                Log.e("ADDFebricScreen ", "setdata: -------------->" + data);
                int febid = febdatalist.get(pos).getInFabricID();

                if (Utils.getInstance().isNetworkConnected((Activity) context)) {
                    Call<ResponseBody> call = ApiClient.API.Delfeb("Bearer " + data, febid);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if(!response.message().equals("Unauthorized")) {
                                    String Vendorresponse = response.body().string();
                                    Log.e("Vendorlist", "onResponse:----------------> " + Vendorresponse);
                                    JSONObject jsonObject = new JSONObject(Vendorresponse);
                                    boolean flag = jsonObject.getBoolean("flag");
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(context, "Data Deleted", Toast.LENGTH_SHORT).show();
                                    Log.e("FEBlist", "message:----------------> " + message);
                                    GetFebList();
                                }
                                else
                                {
                                    Toast.makeText(context, "Login Again", Toast.LENGTH_SHORT).show();
                                    SplashActivity.editor.clear();
                                    SplashActivity.editor.commit();
                                    context.startActivity(new Intent(context, LoginActivity.class));
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
                    Utils.erroraleart(context, "Check Internet Connection", "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        holder.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditFabricActivity.class);
                intent.putExtra("fabid",febdatalist.get(pos).getInFabricID());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return febdatalist.size();
    }


    public class ListClass extends RecyclerView.ViewHolder {
        TextView txtvendorname, colorname, Quantitycount, Txttakebaseno;
        ImageView febimage, deletebtn,editbtn;

        public ListClass(@NonNull View itemView) {
            super(itemView);
            txtvendorname = itemView.findViewById(R.id.txtvendorname);
            colorname = itemView.findViewById(R.id.colorname);
            Quantitycount = itemView.findViewById(R.id.Quantitycount);
            Txttakebaseno = itemView.findViewById(R.id.Txttakebaseno);
            febimage = itemView.findViewById(R.id.febimage);
            deletebtn = itemView.findViewById(R.id.deletebtn);
            editbtn = itemView.findViewById(R.id.editbtn);
        }
    }

    private void GetFebList() {
        if (Utils.getInstance().isNetworkConnected((Activity) context)) {
            Call<ResponseBody> call = ApiClient.API.AllFabricList("Bearer "+data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Data.dissmissdialog();
                    try {
                        ArrayList<Febdata> newfabdata = new ArrayList<>();
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
                                newfabdata.add(new Febdata(InFabricID,VenderID,Color,BillNo,Status,imagePath,Quantity,TakaBalesNo,VenderName,VenderType,ColorName));

                                fabricListAdapter = new FabricListAdapter(context,febdatalist, fabricListAdapter);
                                febdatalist.removeAll(febdatalist);
                                febdatalist.addAll(newfabdata);
                                notifyDataSetChanged();
                            }

                        }
                        else{
                            Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
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
            Utils.erroraleart(context, "Check Internet Connection", "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

    }
}
