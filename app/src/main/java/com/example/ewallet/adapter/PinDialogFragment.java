package com.example.ewallet.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.ewallet.R;

public class PinDialogFragment extends DialogFragment {
    public interface PinDialogListener {
        void onPinEntered(String pin);
    }

    private PinDialogListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null && targetFragment instanceof PinDialogListener) {
            mListener = (PinDialogListener) targetFragment;
        } else {
            Log.d("kkk", "null" + " must implement PinDialogListener");
            throw new ClassCastException(targetFragment != null ? targetFragment.getClass().getSimpleName() : "null" + " must implement PinDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.pin_dialog, null);

        final EditText pinEditText = dialogView.findViewById(R.id.pin_edit_text);

        builder.setView(dialogView)
                .setTitle("Enter PIN")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pin = pinEditText.getText().toString().trim();
                        mListener.onPinEntered(pin);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();

        // Đặt màu nền cho dialog
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        return dialog;
    }
}