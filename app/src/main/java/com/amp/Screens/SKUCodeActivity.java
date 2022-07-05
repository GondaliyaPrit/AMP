package com.amp.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;

import com.amp.R;
import com.amp.databinding.ActivitySkucodeBinding;

public class SKUCodeActivity extends AppCompatActivity {
    ActivitySkucodeBinding binding ;
    LinearLayoutManager  linearLayoutManager ;

    String[] Size  = {"XS","S","M","L","XL","XXL","3XL"};
    String[] Qty  = {"10","20","30","40","50","60","70"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySkucodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        SKUAdapter skuAdapter = new SKUAdapter(this,Size,Qty);
        binding.recyclerview.setAdapter(skuAdapter);


        
    }
}