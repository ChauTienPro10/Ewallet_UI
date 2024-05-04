package com.example.ewallet.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.ewallet.AuthenticationCallback;
import com.example.ewallet.FingerPrint;
import com.example.ewallet.LoginPage;
import com.example.ewallet.MainActivity;
import com.example.ewallet.R;

import java.util.ArrayList;
import java.util.Objects;

public class pindialogAdapter extends DialogFragment {


    private TextView mTofingerPrint;
    View view1, view2, view3, view4, view5, view6;
    String passCode ="";
    String num_01,num_02,num_03,num_04,num_05,num_06;




    public interface PinDialogListener {
        void onPinEntered(String pin);
        void onAuthentication();
    }

    private pindialogAdapter.PinDialogListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null && targetFragment instanceof pindialogAdapter.PinDialogListener) {
            mListener = (pindialogAdapter.PinDialogListener) targetFragment;
        } else {
            Log.d("kkk", "null" + " must implement PinDialogListener");
            throw new ClassCastException(targetFragment != null ? targetFragment.getClass().getSimpleName() : "null" + " must implement PinDialogListener");
        }
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.pincode, null);
        ImageView closeBottomSheet;
        ConstraintLayout btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_X;
//        xac thuc van tay
        mTofingerPrint=dialogView.findViewById(R.id.toFingerprint);


        mTofingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerPrint fingerPrint = new FingerPrint((AppCompatActivity) pindialogAdapter.this.requireActivity());
                AuthenticationCallback authenticationCallback=new AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSuccess() {
                        mListener.onAuthentication();

                    }

                    @Override
                    public void onAuthenticationFailure() {
                        Toast.makeText(pindialogAdapter.this.requireContext(), "authenticate failure !", Toast.LENGTH_SHORT).show();
                    }
                };
                fingerPrint.Authenticate(authenticationCallback);
            }
        });
//        ket thuc xac thuc van tay

        ArrayList<String> numbers_list = new ArrayList<>();
        closeBottomSheet = dialogView.findViewById(R.id.closeBottomSheet);
        closeBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view1 = dialogView.findViewById(R.id.view1);
        view2 = dialogView.findViewById(R.id.view2);
        view3 = dialogView.findViewById(R.id.view3);
        view4 = dialogView.findViewById(R.id.view4);
        view5 = dialogView.findViewById(R.id.view5);
        view6 = dialogView.findViewById(R.id.view6);

        btn_1 = dialogView.findViewById(R.id.btn_1);
        btn_2 = dialogView.findViewById(R.id.btn_2);
        btn_3 = dialogView.findViewById(R.id.btn_3);
        btn_4 = dialogView.findViewById(R.id.btn_4);
        btn_5 = dialogView.findViewById(R.id.btn_5);
        btn_6 = dialogView.findViewById(R.id.btn_6);
        btn_7 = dialogView.findViewById(R.id.btn_7);
        btn_8 = dialogView.findViewById(R.id.btn_8);
        btn_9 = dialogView.findViewById(R.id.btn_9);
        btn_0 = dialogView.findViewById(R.id.btn_0);
        btn_X = dialogView.findViewById(R.id.btn_X);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("1");
                passNumber(numbers_list);
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("2");
                passNumber(numbers_list);
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("3");
                passNumber(numbers_list);
            }
        });


        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("4");
                passNumber(numbers_list);
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("5");
                passNumber(numbers_list);
            }
        });

        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("6");
                passNumber(numbers_list);
            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("7");
                passNumber(numbers_list);
            }
        });
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("8");
                passNumber(numbers_list);
            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("9");
                passNumber(numbers_list);
            }
        });
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("0");
                passNumber(numbers_list);
            }
        });
        btn_X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.clear();
                passNumber(numbers_list);
            }
        });



        builder.setView(dialogView)
                .setTitle("Enter PIN")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder sb = new StringBuilder();
                        for (String number : numbers_list) {
                            sb.append(number);
                        }
                        String pin = sb.toString();
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






    private void passNumber(ArrayList<String> numbers_list) {
        if (numbers_list.size() == 0) {
            view1.setBackgroundResource(R.drawable.bg_view_gray_digits);
            view2.setBackgroundResource(R.drawable.bg_view_gray_digits);
            view3.setBackgroundResource(R.drawable.bg_view_gray_digits);
            view4.setBackgroundResource(R.drawable.bg_view_gray_digits);
            view5.setBackgroundResource(R.drawable.bg_view_gray_digits);
            view6.setBackgroundResource(R.drawable.bg_view_gray_digits);
        } else {
            switch (numbers_list.size()) {
                case 1:
                    num_01= numbers_list.get(0);
                    view1.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 2:
                    num_02= numbers_list.get(1);
                    view2.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 3:
                    num_03= numbers_list.get(2);
                    view3.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 4:
                    num_04= numbers_list.get(3);
                    view4.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 5:
                    num_05= numbers_list.get(4);
                    view5.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 6:
                    num_06= numbers_list.get(5);
                    view6.setBackgroundResource(R.drawable.bg_view_digits);

                    passCode = num_01+num_02+num_03+num_04+num_05+num_06;
                    Log.d("PassCode",passCode);
                    break;
            }
        }

    }
}
