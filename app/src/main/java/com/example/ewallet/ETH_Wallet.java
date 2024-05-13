package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ETH_Wallet extends AppCompatActivity {
    ConstraintLayout btn_back_eth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth_wallet);

        btn_back_eth=findViewById(R.id.btn_back_eth);
        btn_back_eth.setOnClickListener(v -> startActivity(new Intent(ETH_Wallet.this, MainActivity.class)));

    }
}