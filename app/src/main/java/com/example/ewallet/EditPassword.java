package com.example.ewallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditPassword extends Fragment {
    EditText mNewPass, mConfirmPass, mAuthenPass;

    private LinearLayout linearLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);

        linearLayout = view.findViewById(R.id.update_password);
        mNewPass=view.findViewById(R.id.newPassInput);
        mConfirmPass=view.findViewById(R.id.confirmPass);
        mAuthenPass=view.findViewById(R.id.authenPass);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNewPass.getText().toString().equals("")){
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mNewPass.setBackground(borderDrawable);
                    mNewPass.setHint("Please enter new password");
                }
                else if(!mNewPass.getText().toString().equals(mConfirmPass.getText().toString())){
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mConfirmPass.setBackground(borderDrawable);
                    mConfirmPass.setHint("password is incorrect");
                }
                else{
                    openDialog(Gravity.CENTER);
                }

            }
        });

        return view;
    }

    public void openDialog(int gravity) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_pass_word);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windownAtribute = window.getAttributes();
        windownAtribute.gravity = gravity;
        window.setAttributes(windownAtribute);

        if (Gravity.CENTER == gravity) {
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
                changePass();
            }
        });
        dialog.show();
    }
    private void changePass(){
        ProgressDialog progressDialog = new ProgressDialog(EditPassword.this.requireContext());

        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        JsonObject obj =new JsonObject();
        obj.addProperty("newpassword", mNewPass.getText().toString());
        obj.addProperty("password", mAuthenPass.getText().toString());
        ApiService apiService=ApiService.ApiUtils.getApiService(EditPassword.this.requireContext());
        apiService.changePass(obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responString = null;
                try {
                    responString = response.body().string();
                    if(responString.equals("change password success!")){
                        Toast.makeText(requireContext(), responString, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(EditPassword.this.requireContext(), ProfilePage.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(requireContext(), responString, Toast.LENGTH_SHORT).show();


                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
