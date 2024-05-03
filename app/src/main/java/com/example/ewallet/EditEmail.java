package com.example.ewallet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditEmail extends Fragment {

    private LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_email, container, false);

        linearLayout = view.findViewById(R.id.update_email);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Gravity.CENTER);
            }
        });

        return view;
    }

    public void openDialog(int gravity) {
        final Dialog dialog = new Dialog(requireContext());
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      dialog.setContentView(R.layout.activity_dialog_notificaton);

      Window window = dialog.getWindow();
      if(window == null) {
          return;
      }

      window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
      window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

      WindowManager.LayoutParams windownAtribute = window.getAttributes();
      windownAtribute.gravity = gravity;
      window.setAttributes(windownAtribute);

      if(Gravity.CENTER == gravity) {
          dialog.setCancelable(true);

      } else {
          dialog.setCancelable(false);
      }

      Button cancel = dialog.findViewById(R.id.btn_cancel);
        Button confirm = dialog.findViewById(R.id.btn_confirm);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Edit Email", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

}

