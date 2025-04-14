package com.example.taxio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.material.card.MaterialCardView;

public class WhatIsITR extends AppCompatActivity {
    MaterialCardView bt2,  bt4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_is_itr);
        bt2 = findViewById(R.id.bt2);

        bt4 = findViewById(R.id.bt4);
    }
    public void openNew(View view){

        Intent n2 = new Intent(this, Job.class);

        Intent n4 = new Intent(this, Business.class);

        switch (view.getId()){

            case R.id.bt2:
                startActivity(n2);
                break;

            case R.id.bt4:
                startActivity(n4);
                break;

        }
    }
}