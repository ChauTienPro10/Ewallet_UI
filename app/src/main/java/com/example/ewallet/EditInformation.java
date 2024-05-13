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

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ConstraintLayout btnBack;
//    androidx.constraintlayout.widget.ConstraintLayout mToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
//        mToEdit.findViewById(R.id.toEditpage);


        ViewPagerAdapterInformation viewPagerAdapterInformation = new ViewPagerAdapterInformation(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT );
        viewPager.setAdapter(viewPagerAdapterInformation);
        tabLayout.setupWithViewPager(viewPager);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditInformation.this,ProfilePage.class);
                startActivity(intent);
            }
        });
    }
}