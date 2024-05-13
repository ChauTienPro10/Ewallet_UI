package com.example.ewallet.adapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ewallet.DepositFragment;
import com.example.ewallet.WithdrawFragment;

public class TransactionAdapter extends FragmentStateAdapter {
    public TransactionAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new DepositFragment();
            default:
                return new WithdrawFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
