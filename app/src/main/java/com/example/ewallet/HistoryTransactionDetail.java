package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryTransactionDetail extends AppCompatActivity {
    // Declare UI components
    private ImageView detailPic;
    private TextView detailContent, detailAmount_1, detailAmount_2, detailTime;
    private ConstraintLayout btn_back_histrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transaction_detail);

        // Initialize UI components
        detailPic = findViewById(R.id.detailPic);
        detailAmount_1 = findViewById(R.id.detailAmount_1);
        detailAmount_2 = findViewById(R.id.detailAmount_2);
        detailContent = findViewById(R.id.detailContent);
        detailTime = findViewById(R.id.detailTime);
        btn_back_histrans = findViewById(R.id.btn_back_histrans);

        // Get the data passed through the Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Set the image resource
            detailPic.setImageResource(bundle.getInt("Image"));
            // Set the amount text in both TextViews
            detailAmount_1.setText(bundle.getString("Amount"));
            detailAmount_2.setText(bundle.getString("Amount"));
            // Set the content text
            detailContent.setText(bundle.getString("Content"));
            // Set the time text
            detailTime.setText(bundle.getString("Time"));
        }

        // Set a click listener on the back button to navigate to HistoryTransactionPage
        btn_back_histrans.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryTransactionDetail.this, HistoryTransactionPage.class);
            startActivity(intent);
        });
    }
}
