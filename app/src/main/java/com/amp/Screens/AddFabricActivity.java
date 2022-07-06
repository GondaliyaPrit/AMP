package com.amp.Screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.amp.R;
import com.amp.adapters.FabricImagesAdapter;
import com.amp.databinding.ActivityAddFabricBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddFabricActivity extends AppCompatActivity {

    ActivityAddFabricBinding binding;
    String[] items = new String[]{"one", "two", "three"};
    Context context;
    int[] images = new int[]{R.drawable.demo,R.drawable.demo,R.drawable.demo};
    String imagePath;
    Uri fileUri;
    ArrayList<Uri> imageslist;
    LinearLayoutManager linearlayoutmanager;
    FabricImagesAdapter fabricImagesAdapter;
    int position = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFabricBinding.inflate(getLayoutInflater());
        setdata();
        initclicklistener();
        setContentView(binding.getRoot());
    }

    private void setdata() {
        imageslist = new ArrayList<>();
        context = this;
        linearlayoutmanager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL,false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, items);
        binding.spinner1.setAdapter(adapter);
        binding.spinner2.setAdapter(adapter);
    }

    private void initclicklistener() {
        binding.btnclickphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageslist.size() <= 2){
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                }else {
                    Toast.makeText(context, "Maximum 3 Image Upload Here", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnuploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageslist.size() <= 2) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(i, "Select Picture"), 0);
                }else {
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

        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,InfabricActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        binding.rlimages.setVisibility(View.VISIBLE);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                FromCamera(data);
            }
            if (requestCode == 0){
                FromGallery(data.getData());
            }
        }
    }

    private void FromGallery(Uri uri) {
        imageslist.add(uri);
        binding.rvimages.setLayoutManager(linearlayoutmanager);
        fabricImagesAdapter = new FabricImagesAdapter(context,imageslist);
        binding.rvimages.setAdapter(fabricImagesAdapter);
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
        imagePath = destination.getPath();
        fileUri = getImageUri(this, thumbnail);
        imageslist.add(fileUri);
        binding.rvimages.setLayoutManager(linearlayoutmanager);
        fabricImagesAdapter = new FabricImagesAdapter(context,imageslist);
        binding.rvimages.setAdapter(fabricImagesAdapter);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}