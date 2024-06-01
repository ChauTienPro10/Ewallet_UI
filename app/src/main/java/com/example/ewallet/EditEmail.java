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

    // Declare UI components
    private LinearLayout linearLayout;
    private EditText mNewMail;
    private EditText mPassword;
    TextView mWarning;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_email, container, false);

        // Initialize UI elements
        linearLayout = view.findViewById(R.id.update_email);
        mNewMail = view.findViewById(R.id.edit_email);
        mPassword = view.findViewById(R.id.passwordConfirm);
        mWarning = view.findViewById(R.id.warning);

        // Set click listener for the update email button
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open dialog when the button is clicked
                openDialog(Gravity.CENTER);
            }
        });
        // Return the view to be displayed
        return view;
    }

    // Method to open the dialog
    public void openDialog(int gravity) {
        // Create and set up the dialog
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide the title
        dialog.setContentView(R.layout.activity_dialog_notificaton); // Set custom dialog layout

        // Configure the window of the dialog
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT); // Set dimensions
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Set transparent background

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity; // Set gravity
        window.setAttributes(windowAttributes);

        // Set dialog cancelable based on gravity
        dialog.setCancelable(Gravity.CENTER == gravity);

        // Initialize buttons in the dialog
        Button cancel = dialog.findViewById(R.id.btn_cancel);
        Button confirm = dialog.findViewById(R.id.btn_confirm);

        // Set click listener for the cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when cancel button is clicked
                dialog.dismiss();
            }
        });

        // Set click listener for the confirm button
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change email and dismiss the dialog when confirm button is clicked
                changeEmail();
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }

    // Method to handle email change
    private void changeEmail() {
        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(EditEmail.this.requireContext());
        progressDialog.setMessage("Waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // Create JSON object with email and password
        JsonObject obj = new JsonObject();
        obj.addProperty("newemail", mNewMail.getText().toString()); // Add new email
        obj.addProperty("password", mPassword.getText().toString()); // Add password

        // Make API call to change email
        ApiService apiService = ApiService.ApiUtils.getApiService(EditEmail.this.requireContext());
        apiService.changeEmail(obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Handle successful response
                try {
                    String responseString = response.body().string(); // Get response string
                    if (responseString.equals("change email success!")) {
                        // Display success message and navigate to profile page
                        Toast.makeText(requireContext(), responseString, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss(); // Dismiss progress dialog
                        Intent intent = new Intent(EditEmail.this.requireContext(), ProfilePage.class); // Create intent
                        startActivity(intent); // Start activity
                    } else {
                        // Display error message
                        Toast.makeText(requireContext(), responseString, Toast.LENGTH_SHORT).show();
                        mWarning.setText(responseString);
                        progressDialog.dismiss(); // Dismiss progress dialog
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure response
                Log.d("err", "onFailure: " + t);
                Toast.makeText(requireContext(), "Failed to change email", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss(); // Dismiss progress dialog
            }
        });
    }
}
