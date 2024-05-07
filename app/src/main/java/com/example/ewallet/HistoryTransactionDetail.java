package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class HistoryTransactionDetail extends AppCompatActivity {
    ImageView detailPic;
    TextView detailContent,detailAmount_1,detailAmount_2,detailTime;
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
            int imageResourceId = bundle.getInt("image");
            Glide.with(this)
                    .load(imageResourceId)
                    .into(detailPic);
            detailAmount_1.setText(bundle.getString("amount"));
            detailAmount_2.setText(bundle.getString("amount"));
            detailContent.setText(bundle.getString("content"));
            detailTime.setText(bundle.getString("time"));
        }

    }
}