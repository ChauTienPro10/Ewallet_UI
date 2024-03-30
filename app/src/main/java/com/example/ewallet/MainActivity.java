package com.example.ewallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TabLayout mtablayout;
    private ViewPager2 mviewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Chuyển sang trang LoginPage
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
        finish();

        mtablayout =findViewById(R.id.tabLayout);
        mviewpager=findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        mviewpager.setAdapter(viewPagerAdapter);
        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(mtablayout,mviewpager,(tab, position) -> {

            tab.setText("Tab " + (position + 1));
        });
        tabLayoutMediator.attach();

    }

    public void callApi(){
                ApiService.apiService.check().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Toast.makeText(MainActivity.this,"call api success"+response.body(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this,"call api error",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}