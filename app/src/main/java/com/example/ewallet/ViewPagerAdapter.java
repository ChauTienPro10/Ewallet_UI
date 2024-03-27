package com.example.ewallet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle){
        super(fragmentManager,lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position){
        switch (position){
            case 0:
                return new tab_a();
            case 1:
                return new tab_b();
            case 2:
                return new tab_c();
            default:
                return new tab_a();

        }
    }
    @Override
    public int getItemCount() {
        return 3;
    }
}
