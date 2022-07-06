package com.amp.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.amp.R;
import com.amp.adapters.FabricListAdapter;
import com.amp.databinding.ActivityInfabricBinding;

public class InfabricActivity extends AppCompatActivity {
    ActivityInfabricBinding binding;
    FabricListAdapter fabricListAdapter;
    Context context;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfabricBinding.inflate(getLayoutInflater());
        setdata();
        initclicklistener();
        setContentView(binding.getRoot());
    }

    private void setdata() {
        context = this;
        fabricListAdapter = new FabricListAdapter(context);
        linearLayoutManager = new LinearLayoutManager(context , RecyclerView.VERTICAL,false);
        binding.rvlist.setLayoutManager(linearLayoutManager);
        binding.rvlist.setAdapter(fabricListAdapter);
    }

    private void initclicklistener() {
    }
}