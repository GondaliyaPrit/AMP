package com.amp.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amp.R;
import com.amp.databinding.ActivityLoginscreenBinding;

public class loginscreen extends AppCompatActivity {
    ActivityLoginscreenBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityLoginscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(loginscreen.this,HomeActivity.class));
                    finish();
            }
        });




    }
}