package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePage extends AppCompatActivity {
    // Declare UI elements
    private ConstraintLayout btnBack;
    private ConstraintLayout mToWallet;
    private ConstraintLayout toInfor;
    private TextView mName, mEmail;
    private Button mLogout;

    // Override onCreate method
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view
        setContentView(R.layout.activity_profile_page);

        // Initialize UI elements
        mLogout=findViewById(R.id.logout_button);
        btnBack = findViewById(R.id.btn_back);
        toInfor=findViewById(R.id.toEditpage);
        mToWallet=findViewById(R.id.toWallet);
        mName=findViewById(R.id.textView1);
        mEmail=findViewById(R.id.textView2);

        // Set name and email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mName.setText(sharedPreferences.getString("fullName", ""));
        mEmail.setText(sharedPreferences.getString("mailshare",""));

        // Set onClickListener for logout button
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform logout action
                ApiService apiService=ApiService.ApiUtils.getApiService(ProfilePage.this);
                apiService.logout().enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // Clear SharedPreferences and navigate to login page
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(ProfilePage.this, LoginPage.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Handle failure
                    }
                });
            }
        });

        // Set onClickListener for back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(ProfilePage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for editing information
        toInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EditInformation activity
                Intent intent = new Intent(ProfilePage.this, EditInformation.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for navigating to wallet
        mToWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ETH_Wallet activity
                Intent intent = new Intent(ProfilePage.this, ETH_Wallet.class);
                startActivity(intent);
            }
        });
    }
}
