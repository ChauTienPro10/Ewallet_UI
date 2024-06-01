package com.example.ewallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ewallet.adapter.TransactionAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TransactionPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_page);

        // Find the back button view using its ID
        ConstraintLayout btn_back_dep_with = findViewById(R.id.btn_back_dep_with);
        // Set an OnClickListener to handle the click event for the back button
        btn_back_dep_with.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate back to the MainActivity
                Intent intent = new Intent(TransactionPage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Find the ViewPager2 view using its ID
        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        // Set an adapter for the ViewPager2 to handle page changes
        viewPager2.setAdapter(new TransactionAdapter(this));

        // Find the TabLayout view using its ID
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        // Create a TabLayoutMediator to link the TabLayout and ViewPager2
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            // Configure each tab based on its position in the ViewPager2
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        // Set the text and icon for the first tab (Deposit)
                        tab.setText("Deposit");
                        tab.setIcon(R.drawable.send);
                        break;
                    }
                    case 1: {
                        // Set the text and icon for the second tab (Withdraw)
                        tab.setText("Withdraw");
                        tab.setIcon(R.drawable.receive);
                        break;
                    }
                }
            }
        }
        );

        // Attach the TabLayoutMediator to sync the TabLayout with the ViewPager2
        tabLayoutMediator.attach();
    }
}
