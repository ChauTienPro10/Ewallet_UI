package com.example.ewallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditEmail extends Fragment {

    private LinearLayout linearLayout;
    private EditText mNewMAil;
    private EditText mpassword;
    TextView mWarning;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_email, container, false);

        linearLayout = view.findViewById(R.id.update_email);

        mNewMAil=view.findViewById(R.id.edit_email);
        mpassword=view.findViewById(R.id.passwordConfirm);
        mWarning=view.findViewById(R.id.warning);


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

                changeEmail();
                dialog.dismiss();

            }
        });

        dialog.show();
    }


    private void changeEmail(){

        ProgressDialog progressDialog = new ProgressDialog(EditEmail.this.requireContext());

        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        JsonObject obj =new JsonObject();
        obj.addProperty("newemail", mNewMAil.getText().toString());
        obj.addProperty("password", mpassword.getText().toString());
        ApiService apiService=ApiService.ApiUtils.getApiService(EditEmail.this.requireContext());
        apiService.changeEmail(obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String responString = null;
                try {
                    responString = response.body().string();
                    if(responString.equals("change email success!")){
                        Toast.makeText(requireContext(), responString, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(EditEmail.this.requireContext(), ProfilePage.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(requireContext(), responString, Toast.LENGTH_SHORT).show();
                        mWarning.setText(responString);
                        progressDialog.dismiss();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("err", "onFailure: "+t);
                Toast.makeText(requireContext(), "that bai", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

}

