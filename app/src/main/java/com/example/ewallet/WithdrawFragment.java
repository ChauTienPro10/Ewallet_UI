package com.example.ewallet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewallet.adapter.pindialogAdapter;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;

import Entities.Card;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawFragment extends Fragment implements pindialogAdapter.PinDialogListener {
    // Declare UI elements
    private EditText mAmount;
    private TextView mIdcard;
    private TextView mName;
    private TextView mbalance;
    private EditText mWithdawInput;
    private Button mSubmit;

    // Dialog for entering PIN
    pindialogAdapter dialogFragment = new pindialogAdapter();

    // Decimal format for amount input
    final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    // Fragment arguments
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Constructor
    public WithdrawFragment() {
    }

    // Factory method to create a new instance of this fragment
    public static WithdrawFragment newInstance(String param1, String param2) {
        WithdrawFragment fragment = new WithdrawFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // Called when the fragment is created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve arguments passed to the fragment
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Called to create the view hierarchy associated with the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);

        // Initialize UI elements
        mWithdawInput = view.findViewById(R.id.withdraw);
        mSubmit = view.findViewById(R.id.button_withdraw);
        mbalance = view.findViewById(R.id.textView12);
        mIdcard = view.findViewById(R.id.textView10);
        mName = view.findViewById(R.id.textView20);

        // Initialize card details
        init();

        // Set click listener for the submit button
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the amount input is not empty
                if (!mWithdawInput.getText().toString().equals("")) {
                    // Show PIN dialog
                    showPinDialog();
                } else {
                    // Highlight the amount input if it's empty
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Set border thickness and color
                    borderDrawable.setColor(Color.WHITE); // Set background color
                    mWithdawInput.setBackground(borderDrawable);
                    mWithdawInput.setHint("Please enter amount");
                }
            }
        });

        // Add text watcher to format amount input
        mWithdawInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Remove text watcher to avoid infinite loop
                mWithdawInput.removeTextChangedListener(this);

                try {
                    // Parse and format the input
                    String originalString = s.toString().replaceAll(",", "");
                    double value = decimalFormat.parse(originalString).doubleValue();
                    String formattedString = decimalFormat.format(value).replaceAll("\\.", ",");
                    mWithdawInput.setText(formattedString);
                    mWithdawInput.setSelection(formattedString.length());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Add text watcher back
                mWithdawInput.addTextChangedListener(this);
            }
        });

        return view;
    }

    // Handle PIN entered in dialog
    @Override
    public void onPinEntered(String pin) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pincode", pin);

        // Authenticate PIN
        ApiService apiService = ApiService.ApiUtils.getApiService(WithdrawFragment.this.requireContext());
        apiService.authenPin(jsonObject).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() == true) {
                    // Proceed with withdrawal if PIN is correct
                    setWithdraw(apiService, pin);
                } else {
                    // Display failure message if PIN is incorrect
                    Toast.makeText(WithdrawFragment.this.requireContext(), "Failure!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Handle failure
            }
        });
    }

    // Handle successful authentication
    @Override
    public void onAuthentication() {
        // Fetch PIN from API
        getPincode();
        // Dismiss PIN dialog
        dialogFragment.dismiss();
        // Display success message
        Toast.makeText(WithdrawFragment.this.requireContext(), "Authentication success!", Toast.LENGTH_SHORT).show();
    }

    // Show PIN dialog
    private void showPinDialog() {
        // Set this fragment as the target fragment for the PIN dialog
        dialogFragment.setTargetFragment(this, 0);
        // Show the PIN dialog
        dialogFragment.show(getFragmentManager(), "pin_dialog");
    }

    // Process withdrawal
    private void setWithdraw(ApiService apiService, String pin) {
        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(WithdrawFragment.this.requireContext());
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // Prepare withdrawal request
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("amount", mWithdawInput.getText().toString());
        jsonObject.addProperty("pin", pin);

        // Perform withdrawal request
        apiService.withdrawSer(jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseString;
                try {
                    // Process response
                    responseString = response.body().string();
                    if (responseString.contains("OK")) {
                        // Show success message and navigate to transaction page
                        Toast.makeText(WithdrawFragment.this.requireContext(), responseString, Toast.LENGTH_SHORT).show();
                        mWithdawInput.setText("");
                        Intent intent = new Intent(WithdrawFragment.this.requireContext(), TransactionPage.class);
                        startActivity(intent);
                    } else {
                        // Show error message
                        Toast.makeText(WithdrawFragment.this.requireContext(), responseString, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Dismiss progress dialog
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
                // Dismiss progress dialog
                progressDialog.dismiss();
            }
        });
    }

    // Fetch PIN code from API
    private void getPincode() {
        // Get API service instance
        ApiService apiService = ApiService.ApiUtils.getApiService(WithdrawFragment.this.requireContext());
        // Make API call to get PIN
        apiService.getPin().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    // Retrieve PIN from response
                    String pin = response.body().string();
                    // Check if PIN retrieval was successful
                    if (!pin.equals("can't authentication")) {
                        // Proceed with PIN authentication
                        onPinEntered(pin);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
            }
        });
    }

    // Initialize card details
    private void init() {
        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(WithdrawFragment.this.requireContext());
        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // Get API service instance
        ApiService apiService = ApiService.ApiUtils.getApiService(WithdrawFragment.this.requireContext());
        // Fetch card details from API
        apiService.getCard().enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                // Process response
                Card card = response.body();
                if (card != null) {
                    // Update UI with card details
                    mIdcard.setText(addSpaceEveryFourCharacters(card.getCard_number()));
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    mName.setText(sharedPreferences.getString("fullName", ""));
                    mbalance.setText(card.getBalance().toString());
                }
                // Dismiss progress dialog
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                // Handle failure
                // Dismiss progress dialog
                progressDialog.dismiss();
            }
        });
    }

    // Utility method to add spaces every four characters to a string
    public String addSpaceEveryFourCharacters(String input) {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            if (count == 4) {
                builder.append(' '); // Add space after every four characters
                count = 0;
            }
            builder.append(input.charAt(i));
            count++;
        }

        return builder.toString();
    }
}