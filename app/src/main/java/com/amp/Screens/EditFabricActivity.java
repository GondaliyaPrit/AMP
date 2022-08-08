package com.amp.Screens;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amp.Data;
import com.amp.Utils;
import com.amp.adapters.FabricImagesAdapter;
import com.amp.databinding.ActivityEditFabricBinding;
import com.amp.interface_api.ApiClient;
import com.amp.models.Colorlist;
import com.amp.models.Vendorlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFabricActivity extends AppCompatActivity {

    ActivityEditFabricBinding binding;
    ArrayList<Colorlist> colorlists;
    ArrayList<Vendorlist> vendorlistArrayList;
    Context context;
    String imagePath, colorname, vendortype, vendorname, fabquanity, takebaleseno, data;
    Uri fileUri;
    ArrayList<Uri> imageslist;
    ArrayList<String> colornamelist, vendornamelist, base64list;
    LinearLayoutManager linearlayoutmanager;
    FabricImagesAdapter fabricImagesAdapter;
    int vendorid = 0, febvendorid = 0, febcolorid = 0, billno = 0, position = 1, colorid, fabid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditFabricBinding.inflate(getLayoutInflater());
        setdata();
        initclicklistener();
        setContentView(binding.getRoot());
    }

    private void setdata() {
        context = this;
        fabid= getIntent().getIntExtra("fabid",0);
        Toast.makeText(context, ""+fabid, Toast.LENGTH_SHORT).show();
        imageslist = new ArrayList<>();
        colorlists = new ArrayList<>();
        colornamelist = new ArrayList<>();
        vendorlistArrayList = new ArrayList<>();
        vendornamelist = new ArrayList<>();
        base64list = new ArrayList<>();
        data = SplashActivity.sharedPreferences.getString("data", "");
        Log.e("ADDFebricScreen ", "setdata: -------------->" + data);
        context = this;
        Getcolorlist();
        GetVendorlist();
        linearlayoutmanager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
    }

    private void initclicklistener() {

        binding.btnclickphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageslist.size() <= 2) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    Toast.makeText(context, "Maximum 3 Image Upload Here", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnuploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageslist.size() <= 2) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(i, "Select Picture"), 0);
                } else {
                    Toast.makeText(context, "Maximum 3 Image Upload Here", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.imgleftmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (linearlayoutmanager.findLastCompletelyVisibleItemPosition() < (fabricImagesAdapter.getItemCount() + 1)) {
//                    linearlayoutmanager.scrollToPosition(linearlayoutmanager.findLastCompletelyVisibleItemPosition() - 1);
//                }
                if (position > 0) {
                    position = position - 1;
                    binding.rvimages.smoothScrollToPosition(position);
                } else {
                    binding.rvimages.smoothScrollToPosition(0);
                }
            }
        });

        binding.imgrightmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearlayoutmanager.findLastCompletelyVisibleItemPosition() < (fabricImagesAdapter.getItemCount() - 1)) {
                    linearlayoutmanager.scrollToPosition(linearlayoutmanager.findLastCompletelyVisibleItemPosition() + 1);
                }
            }
        });

        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                febvendorid = vendorlistArrayList.get(position).getVendorid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                febcolorid = colorlists.get(position).getColorid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabquanity = binding.edquntity.getText().toString();
                String bn = binding.edbillno.getText().toString();
                takebaleseno = binding.edbalesno.getText().toString();

                Log.e("data", "fabquanity = " + fabquanity + " billno = " + billno + "takebaleseno =  " + takebaleseno + "base64list size " + base64list.size());
                if (fabquanity != null && bn != null && takebaleseno != null && febcolorid > 0 && febvendorid > 0 && base64list.size() > 0 && takebaleseno.contains("-")) {
                    billno = Integer.parseInt(bn);
                } else if (fabquanity.isEmpty() || bn.isEmpty() || takebaleseno.isEmpty() || febcolorid < 0 || febvendorid < 0 || base64list.size() <= 0) {
                    Utils.erroraleart(context, "All fields are required !", "ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } else if (!takebaleseno.contains("-")) {
                    Utils.erroraleart(context, "Comma Must be require in TakaBalesNo", "ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            binding.rlimages.setVisibility(View.VISIBLE);
            if (requestCode == 1) {
                FromCamera(data);
            }
            if (requestCode == 0) {
                FromGallery(data.getData());
            }
        }
    }

    private void FromGallery(Uri uri) {
        imageslist.add(uri);
        binding.rvimages.setLayoutManager(linearlayoutmanager);
        fabricImagesAdapter = new FabricImagesAdapter(context, imageslist);
        binding.rvimages.setAdapter(fabricImagesAdapter);
        InputStream iStream = null;
        try {
            iStream = getContentResolver().openInputStream(uri);
            byte[] inputData = getBytes(iStream);
            String encodedString = Base64.encodeToString(inputData, Base64.DEFAULT);
            base64list.add(encodedString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void FromCamera(Intent data) {
        Log.e("camera", "camera");
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] byteArray = bytes.toByteArray();
        String encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        base64list.add(encodedString);
        imagePath = destination.getPath();
        fileUri = getImageUri(this, thumbnail);
        imageslist.add(fileUri);
        binding.rvimages.setLayoutManager(linearlayoutmanager);
        fabricImagesAdapter = new FabricImagesAdapter(context, imageslist);
        binding.rvimages.setAdapter(fabricImagesAdapter);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void Getcolorlist() {
        if (Utils.getInstance().isNetworkConnected(this)) {
            Data.showdialog(context, "Loading..");
            Call<ResponseBody> call = ApiClient.API.FebColor("Bearer " + data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Data.dissmissdialog();
                    try {
                        String Colorresponse = response.body().string();
                        Log.e("AddFabricActivity", "onResponse:----------------> " + Colorresponse);
                        JSONObject jsonObject = new JSONObject(Colorresponse);
                        boolean flag = jsonObject.getBoolean("flag");
                        String message = jsonObject.getString("message");
                        if (flag) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject dataobject = data.getJSONObject(i);
                                colorid = dataobject.getInt("Id");
                                colorname = dataobject.getString("ColorName");
                                colorlists.add(new Colorlist(colorid, colorname));
                            }

                            for (int i = 0; i < colorlists.size(); i++) {
                                colornamelist.add(colorlists.get(i).getColorname());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, colornamelist);
                            binding.spinner1.setAdapter(adapter);


                        } else {
                            Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            Log.e("AddFabricActivity", "message:----------------> " + message);
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

    private void GetVendorlist() {
        if (Utils.getInstance().isNetworkConnected(this)) {
            Call<ResponseBody> call = ApiClient.API.Vendorlist("Bearer " + data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Data.dissmissdialog();
                    try {
                        String Vendorresponse = response.body().string();
                        Log.e("Vendorlist", "onResponse:----------------> " + Vendorresponse);
                        JSONObject jsonObject = new JSONObject(Vendorresponse);
                        boolean flag = jsonObject.getBoolean("flag");
                        String message = jsonObject.getString("message");
                        if (flag) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject dataobject = data.getJSONObject(i);
                                vendorid = dataobject.getInt("Id");
                                vendorname = dataobject.getString("Name");
                                vendortype = dataobject.getString("VenderType");
                                vendorlistArrayList.add(new Vendorlist(vendorid, vendorname, vendortype));
                            }

                            for (int i = 0; i < vendorlistArrayList.size(); i++) {
                                vendornamelist.add(vendorlistArrayList.get(i).getVendorname());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, vendornamelist);
                            binding.spinner2.setAdapter(adapter);


                        } else {
                            Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            Log.e("AddFabricActivity", "message:----------------> " + message);
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