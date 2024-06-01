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

    // Declare UI elements
    private TextView mReceiver;
    private TextView mNote;
    private TextView mAmount;
    private LinearLayout mDone;
    private TextView mDate;

    @SuppressLint("MissingInflatedId") // Suppressing lint warning for missing inflated ID
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_money_transfer);

        // Initialize UI elements
        mReceiver=findViewById(R.id.textView25);
        mAmount=findViewById(R.id.textView32);
        mDate=findViewById(R.id.textView35);
        mNote=findViewById(R.id.textView26);
        mDone=findViewById(R.id.done);

        // Retrieve data from the intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("receiver"); // Receiver's name
        String amount = intent.getStringExtra("amount"); // Transaction amount

        // Set data to UI elements
        mReceiver.setText(name); // Set receiver's name
        mAmount.setText(amount); // Set transaction amount
        mDate.setText(new Date().toString()); // Set current date as transaction date
        mNote.setText(intent.getStringExtra("note")); // Set transaction note

        // Set click listener for the "Done" button
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main activity
                Intent intent=new Intent(Successful_money_transfer.this, MainActivity.class );
                startActivity(intent);
            }
        });
    }
}
