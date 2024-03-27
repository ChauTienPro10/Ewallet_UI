package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private TabLayout mtablayout;
    private ViewPager2 mviewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtablayout =findViewById(R.id.tabLayout);
        mviewpager=findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        mviewpager.setAdapter(viewPagerAdapter);
        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(mtablayout,mviewpager,(tab, position) -> {

            tab.setText("Tab " + (position + 1));
        });
        tabLayoutMediator.attach();

    }
}