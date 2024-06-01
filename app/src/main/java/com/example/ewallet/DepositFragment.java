package com.example.ewallet;

import android.annotation.SuppressLint;
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
import android.util.Log;
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


public class DepositFragment extends Fragment implements pindialogAdapter.PinDialogListener {

    // Declare UI elements
    private EditText inputtAmount;
    private TextView midcard;
    private TextView mName;
    private TextView mbalane;
    pindialogAdapter dialogFragment = new pindialogAdapter();
    private Button deposit;
    final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    // Parameters for fragment initialization
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Required empty public constructor
    public DepositFragment() {
    }

    // Method to create a new instance of the fragment
    public static DepositFragment newInstance(String param1, String param2) {
        DepositFragment fragment = new DepositFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deposit, container, false);

        // Initialize UI elements
        deposit = view.findViewById(R.id.deposit_button);
        inputtAmount = view.findViewById(R.id.amount_text);
        midcard = view.findViewById(R.id.textView10);
        mName = view.findViewById(R.id.textView20);
        mbalane = view.findViewById(R.id.textView12);

        // Set up button click listener
        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if amount is entered
                if (!inputtAmount.getText().toString().equals("")) {
                    showPinDialog();
                } else {
                    // If amount is not entered, show error
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Set border thickness and color
                    borderDrawable.setColor(Color.WHITE); // Set background color for EditText
                    inputtAmount.setBackground(borderDrawable);
                    inputtAmount.setHint("Please enter amount");
                }
            }
        });

        // Add text change listener to format amount input
        inputtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputtAmount.removeTextChangedListener(this);
                try {
                    // Remove formatting and parse input to double
                    String originalString = s.toString().replaceAll(",", "");
                    double value = decimalFormat.parse(originalString).doubleValue();

                    // Format the value with commas and set it back to EditText
                    String formattedString = decimalFormat.format(value).replaceAll("\\.", ",");
                    inputtAmount.setText(formattedString);
                    inputtAmount.setSelection(formattedString.length());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                inputtAmount.addTextChangedListener(this);
            }
        });

        return view;
    }


    // Method to initiate deposit process
    private void setDeposit(ApiService apiService, String pin) {
        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(DepositFragment.this.requireContext());
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // Prepare JSON object with deposit details
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("amount", inputtAmount.getText().toString());
        jsonObject.addProperty("pin", pin);

        // Make API call to deposit amount
        apiService.depositSer(jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responString;
                try {
                    responString = response.body().string();
                    if (responString.contains("OK")) {
                        // If deposit is successful, show success message and navigate to transaction page
                        Toast.makeText(DepositFragment.this.requireContext(), responString, Toast.LENGTH_SHORT).show();
                        inputtAmount.setText("");
                        Intent intent = new Intent(DepositFragment.this.requireContext(), TransactionPage.class);
                        startActivity(intent);
                    } else {
                        // If deposit fails, show error message
                        Toast.makeText(DepositFragment.this.requireContext(), responString, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Dismiss progress dialog
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Dismiss progress dialog in case of failure
                progressDialog.dismiss();
            }
        });
    }

    // Method called when PIN is entered
    @Override
    public void onPinEntered(String pin) {
        Log.d("pincode", "onPinEntered: " + pin);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pincode", pin);
        ApiService apiService = ApiService.ApiUtils.getApiService(DepositFragment.this.requireContext());
        // Authenticate PIN with API
        apiService.authenPin(jsonObject).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() == true) {
                    // If PIN authentication is successful, initiate deposit process
                    setDeposit(apiService, pin);
                } else {
                    // If PIN authentication fails, show error message
                    Toast.makeText(DepositFragment.this.requireContext(), "that bai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Handle failure scenario
            }
        });
    }

    // Method called after successful authentication
    @Override
    public void onAuthentication() {
        // Get PIN code after successful authentication
        getPincode();
        // Dismiss PIN dialog
        dialogFragment.dismiss();
        // Show success message
        Toast.makeText(DepositFragment.this.requireContext(), "authenticate success !", Toast.LENGTH_SHORT).show();
    }

    // Method to show PIN dialog
    private void showPinDialog() {
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), "pin_dialog");
    }

    // Method to get PIN code after successful authentication
    private void getPincode() {
        ApiService apiService = ApiService.ApiUtils.getApiService(DepositFragment.this.requireContext());
        apiService.getPin().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String pin = response.body().string();
                    if (!pin.equals("can't authentication")) {
                        // If PIN is retrieved successfully, proceed with PIN entered callback
                        onPinEntered(pin);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure scenario
            }
        });
    }

    // Method to initialize UI elements and fetch card details
    private void init() {
        ProgressDialog progressDialog = new ProgressDialog(DepositFragment.this.requireContext());
        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ApiService apiService = ApiService.ApiUtils.getApiService(DepositFragment.this.requireContext());
        apiService.getCard().enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                Card card = response.body();
                if (card != null) {
                    // Update UI with card details
                    midcard.setText(addSpaceEveryFourCharacters(card.getCard_number()));
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    mName.setText(sharedPreferences.getString("fullName", ""));
                    mbalane.setText(card.getBalance().toString());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                // Handle failure scenario
            }
        });
    }

    // Method to add space every four characters in a string
    public String addSpaceEveryFourCharacters(String input) {
        StringBuilder builder = new StringBuilder();
        int count = 0;

        for (int i = 0; i < input.length(); i++) {
            if (count == 4) {
                builder.append(' ');
                count = 0;
            }
            builder.append(input.charAt(i));
            count++;
        }

        return builder.toString();
    }
}