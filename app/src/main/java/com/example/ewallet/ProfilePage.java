package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfilePage extends AppCompatActivity {
    private ConstraintLayout btnBack;
    private androidx.constraintlayout.widget.ConstraintLayout mToWallet;
    androidx.constraintlayout.widget.ConstraintLayout toInfor;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        btnBack = findViewById(R.id.btn_back);
        toInfor=findViewById(R.id.toEditpage);
        mToWallet=findViewById(R.id.toWallet);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this,MainActivity.class);
                startActivity(intent);
            }
        });


        toInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, EditInformation.class);
                startActivity(intent);
            }
        });

        mToWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, ETH_Wallet.class);
                startActivity(intent);
            }
        });
    }
}