package com.amp.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.amp.R;
import com.amp.adapters.FabricImagesAdapter;
import com.amp.databinding.ActivityAddFabricBinding;

public class AddFabricActivity extends AppCompatActivity {

    ActivityAddFabricBinding binding;
    String[] items = new String[]{"one", "two", "three"};
    Context context;
    int[] images = new int[]{R.drawable.demo,R.drawable.demo,R.drawable.demo};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFabricBinding.inflate(getLayoutInflater());
        setdata();
        initclicklistener();
        setContentView(binding.getRoot());
    }

    private void setdata() {
        context = this;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, items);
        binding.spinner1.setAdapter(adapter);
        binding.spinner2.setAdapter(adapter);
        binding.rvimages.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL,false));
        FabricImagesAdapter fabricImagesAdapter = new FabricImagesAdapter(context,images);
        binding.rvimages.setAdapter(fabricImagesAdapter);
    }

    private void initclicklistener() {
    }
}