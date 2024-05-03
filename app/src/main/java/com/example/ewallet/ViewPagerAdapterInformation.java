package com.example.ewallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapterInformation extends FragmentStatePagerAdapter {
    public ViewPagerAdapterInformation(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new EditEmail();
            case 1:
                return new EditPassword();
            case 2:
                return new EditPhone();
            default:
                return new EditEmail();

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Edit Email";
                break;
            case 1:
                title = "Edit Password";
                break;
            case 2:
                title = "Edit Phone";
                break;
        }
        return title;
    }
}
