package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class HistoryTransactionDetail extends AppCompatActivity {
    ImageView detailPic;
    TextView detailContent,detailAmount_1,detailAmount_2,detailTime;
    ConstraintLayout btn_back_histrans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transaction_detail);

        detailPic=findViewById(R.id.detailPic);
        detailAmount_1=findViewById(R.id.detailAmount_1);
        detailAmount_2=findViewById(R.id.detailAmount_2);
        detailContent=findViewById(R.id.detailContent);
        detailTime=findViewById(R.id.detailTime);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {

            detailPic.setImageResource(bundle.getInt("Image"));
            detailAmount_1.setText(bundle.getString("Amount"));
            detailAmount_2.setText(bundle.getString("Amount"));
            detailContent.setText(bundle.getString("Content"));
            detailTime.setText(bundle.getString("Time"));
        }

        btn_back_histrans=findViewById(R.id.btn_back_histrans);
        btn_back_histrans.setOnClickListener(v -> startActivity(new Intent(HistoryTransactionDetail.this,HistoryTransactionPage.class)));

    }
}