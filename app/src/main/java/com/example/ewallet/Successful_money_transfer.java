package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

public class Successful_money_transfer extends AppCompatActivity {

    private TextView mReceiver;
    private TextView mNote;
    private  TextView mAmount;
    private LinearLayout mDone;

    private  TextView mDate;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_money_transfer);
        mReceiver=findViewById(R.id.textView25);
        mAmount=findViewById(R.id.textView32);
        mDate=findViewById(R.id.textView35);
        mNote=findViewById(R.id.textView26);
        mDone=findViewById(R.id.done);
        Intent intent = getIntent();
        String name = intent.getStringExtra("receiver");
        String amount = intent.getStringExtra("amount");
        mReceiver.setText(name);
        mAmount.setText(amount);
        mDate.setText(new Date().toString());
        mNote.setText(intent.getStringExtra("note"));
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Successful_money_transfer.this, MainActivity.class );

                startActivity(intent);
            }
        });
    }
}