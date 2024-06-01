package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ewallet.adapter.ViewPagerAdapterInformation;
import com.google.android.material.tabs.TabLayout;

public class EditInformation extends AppCompatActivity {

    // Declare UI components
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ConstraintLayout btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);

        // Initialize UI elements
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        // Create and set up the adapter for ViewPager
        ViewPagerAdapterInformation viewPagerAdapterInformation = new ViewPagerAdapterInformation(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapterInformation); // Set adapter to ViewPager

        // Connect TabLayout with ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Set OnClickListener for the back button
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to ProfilePage activity when back button is clicked
                Intent intent = new Intent(EditInformation.this, ProfilePage.class);
                startActivity(intent);
            }
        });
    }
}
