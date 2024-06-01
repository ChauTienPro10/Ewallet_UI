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

    // Declare UI components
    EditText mNewPass, mConfirmPass, mAuthenPass;
    private LinearLayout linearLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);

        // Initialize UI elements
        linearLayout = view.findViewById(R.id.update_password);
        mNewPass = view.findViewById(R.id.newPassInput);
        mConfirmPass = view.findViewById(R.id.confirmPass);
        mAuthenPass = view.findViewById(R.id.authenPass);

        // Set click listener on the LinearLayout
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input fields and open dialog if conditions are met
                if (mNewPass.getText().toString().equals("")) {
                    // If new password field is empty, highlight it and show a hint
                    highlightField(mNewPass);
                    mNewPass.setHint("Please enter new password");
                } else if (!mNewPass.getText().toString().equals(mConfirmPass.getText().toString())) {
                    // If new password and confirm password fields do not match, highlight confirm password field and show a hint
                    highlightField(mConfirmPass);
                    mConfirmPass.setHint("Password is incorrect");
                } else {
                    // If validation passes, open dialog
                    openDialog(Gravity.CENTER);
                }
            }
        });

        // Return the view to be displayed
        return view;
    }

    // Method to highlight an EditText field
    private void highlightField(EditText editText) {
        // Create a border drawable with red stroke and white background
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setStroke(2, Color.RED); // Set border width and color
        borderDrawable.setColor(Color.WHITE); // Set background color
        editText.setBackground(borderDrawable); // Set the drawable as EditText background
    }

    // Method to open a dialog
    public void openDialog(int gravity) {
        // Create a new dialog with the context of the current fragment
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide the title bar
        dialog.setContentView(R.layout.activity_dialog_pass_word); // Set the content view to a custom layout

        // Get the window of the dialog
        Window window = dialog.getWindow();
        if (window == null) {
            return; // Return if window is null
        }

        // Set dialog window attributes
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT); // Set window dimensions
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Set transparent background

        // Set window gravity
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        // Set dialog cancelable based on gravity
        dialog.setCancelable(Gravity.CENTER == gravity);

        // Initialize dialog UI components
        Button cancel = dialog.findViewById(R.id.btn_cancel); // Cancel button
        Button confirm = dialog.findViewById(R.id.btn_confirm); // Confirm button

        // Set OnClickListener for cancel button to dismiss the dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Set OnClickListener for confirm button to initiate password change
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass(); // Call method to change password
            }
        });

        // Show the dialog
        dialog.show();
    }

    // Method to change the password
    private void changePass() {
        // Create a progress dialog to show while waiting for response
        ProgressDialog progressDialog = new ProgressDialog(EditPassword.this.requireContext());
        progressDialog.setMessage("Waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(); // Show the progress dialog

        // Create a JSON object with new password and authentication password
        JsonObject obj = new JsonObject();
        obj.addProperty("newpassword", mNewPass.getText().toString()); // Add new password to JSON object
        obj.addProperty("password", mAuthenPass.getText().toString()); // Add authentication password to JSON object

        // Get ApiService instance
        ApiService apiService = ApiService.ApiUtils.getApiService(EditPassword.this.requireContext());

        // Make an asynchronous API call to change password
        apiService.changePass(obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Handle response from the API
                String responseString = null;
                try {
                    responseString = response.body().string(); // Get response body as string
                    if (responseString.equals("change password success!")) {
                        // If password change is successful, show a toast and navigate to ProfilePage
                        Toast.makeText(requireContext(), responseString, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditPassword.this.requireContext(), ProfilePage.class); // Create intent to navigate
                        startActivity(intent); // Start the ProfilePage activity
                    } else {
                        // If password change fails, show a toast with the response message
                        Toast.makeText(requireContext(), responseString, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e); // Handle any IO exceptions
                }
                progressDialog.dismiss(); // Dismiss the progress dialog
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure of the API call
                progressDialog.dismiss(); // Dismiss the progress dialog
            }
        });
    }
}
