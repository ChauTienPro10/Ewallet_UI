package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PaymentProcessing extends AppCompatActivity {
    private ConstraintLayout btn_backProcessing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_processing);

        btn_backProcessing=findViewById(R.id.btn_back_processing);
        btn_backProcessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentProcessing.this,TranferMoney.class);
                startActivity(intent);
            }
        });


    }
}