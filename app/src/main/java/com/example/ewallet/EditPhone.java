package com.example.ewallet;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPhone extends Fragment {

    // Declare UI components
    private EditText mNewphone, mPassword;
    TextView mWarning;
    private LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment and return the view
        View view = inflater.inflate(R.layout.fragment_edit_phone, container, false);

        // Initialize UI components
        mNewphone = view.findViewById(R.id.edit_phone);
        mPassword = view.findViewById(R.id.password);
        mWarning = view.findViewById(R.id.warning);
        linearLayout = view.findViewById(R.id.linearLayout6);

        // Set an OnClickListener on the LinearLayout to trigger the openDialog method when clicked
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a confirmation dialog with centered gravity when the layout is clicked
                openDialog(Gravity.CENTER);
            }
        });

        // Return the view to be displayed
        return view;
    }

    // Method to open a custom confirmation dialog
    public void openDialog(int gravity) {
        // Create a new dialog with the context of the current fragment
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  // Request no title for the dialog
        dialog.setContentView(R.layout.activity_dialog_notificaton);  // Set the content view to a custom layout

        // Get the window of the dialog
        Window window = dialog.getWindow();
        if (window == null) {
            return;  // Exit if the window is null
        }

        // Set the layout parameters for the dialog window
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);  // Set width to match parent and height to wrap content
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  // Set a transparent background

        // Set the gravity of the window attributes
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;  // Apply the specified gravity
        window.setAttributes(windowAttributes);  // Set the updated attributes

        // Determine if the dialog is cancelable based on the gravity
        dialog.setCancelable(Gravity.CENTER == gravity);

        // Initialize the UI components of the dialog
        TextView mTitle = dialog.findViewById(R.id.title);  // TextView for the dialog title
        mTitle.setText("Edit Phone");  // Set the title text
        TextView mContent = dialog.findViewById(R.id.content);  // TextView for the dialog content
        mContent.setText("Are you sure you want to change your phone?");  // Set the content text
        Button cancel = dialog.findViewById(R.id.btn_cancel);  // Cancel button
        Button confirm = dialog.findViewById(R.id.btn_confirm);  // Confirm button

        // Set OnClickListener for the cancel button to dismiss the dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();  // Dismiss the dialog
            }
        });

        // Set OnClickListener for the confirm button to change the phone number
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhone();  // Call changePhone method to handle phone number change
                dialog.dismiss();  // Dismiss the dialog
            }
        });

        // Show the dialog
        dialog.show();
    }

    // Method to change the phone number
    private void changePhone() {
        // Create and show a progress dialog
        ProgressDialog progressDialog = new ProgressDialog(EditPhone.this.requireContext());
        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // Create a JSON object with the new phone number and password
        JsonObject obj = new JsonObject();
        obj.addProperty("newphone", mNewphone.getText().toString());  // Add new phone number to JSON object
        obj.addProperty("password", mPassword.getText().toString());  // Add password to JSON object

        // Get the ApiService instance
        ApiService apiService = ApiService.ApiUtils.getApiService(EditPhone.this.requireContext());

        // Make an asynchronous API call to change the phone number
        apiService.changePhone(obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Handle the response from the API
                try {
                    String responseString = response.body().string();  // Get the response string
                    if (responseString.equals("change phone success!")) {
                        // Notify user of success and navigate to ProfilePage
                        Toast.makeText(requireContext(), responseString, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();  // Dismiss the progress dialog
                        Intent intent = new Intent(EditPhone.this.requireContext(), ProfilePage.class);  // Create an intent to navigate to ProfilePage
                        startActivity(intent);  // Start the ProfilePage activity
                    } else {
                        // Display warning message if phone change fails
                        Toast.makeText(requireContext(), responseString, Toast.LENGTH_SHORT).show();  // Show failure message
                        mWarning.setText(responseString);  // Set the warning message
                        progressDialog.dismiss();  // Dismiss the progress dialog
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);  // Handle any I/O exceptions
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure of the API call
                Log.d("err", "onFailure: " + t);  // Log the error
                Toast.makeText(requireContext(), "Operation failed", Toast.LENGTH_SHORT).show();  // Show failure message
                progressDialog.dismiss();  // Dismiss the progress dialog
            }
        });
    }
}
