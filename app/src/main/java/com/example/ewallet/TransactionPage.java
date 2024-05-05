package com.example.ewallet;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TransactionPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_page);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back homePage
                Intent intent = new Intent(TransactionPage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new TransactionAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:{
                        tab.setText("Deposit");
                        tab.setIcon(R.drawable.send);
                        break;
                    }
                    case 1:{
                        tab.setText("Withdraw");
                        tab.setIcon(R.drawable.receive);


//                        BadgeDrawable badgeDrawable= tab.getOrCreateBadge();
//                        badgeDrawable.setBackgroundColor(
//                                ContextCompat.getColor(getApplicationContext(),R.color.purple)
//                        );
//                        badgeDrawable.setVisible(true);
//                        badgeDrawable.setNumber(10);
                        break;
                    }

                }

            }
        }
        );
        tabLayoutMediator.attach();

    }
}