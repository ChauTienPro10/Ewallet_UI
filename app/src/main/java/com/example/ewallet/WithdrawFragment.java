package com.example.ewallet;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.ewallet.adapter.pindialogAdapter;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WithdrawFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WithdrawFragment extends Fragment implements pindialogAdapter.PinDialogListener{

    private EditText mWithdawInput;
    private Button mSubmit;
    pindialogAdapter dialogFragment = new pindialogAdapter();
    final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WithdrawFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WithdrawFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WithdrawFragment newInstance(String param1, String param2) {
        WithdrawFragment fragment = new WithdrawFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);
        mWithdawInput=view.findViewById(R.id.withdraw);
        mSubmit=view.findViewById(R.id.button_withdraw);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mWithdawInput.getText().toString().equals("")){
                    showPinDialog();
                }
                else{
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mWithdawInput.setBackground(borderDrawable);
                    mWithdawInput.setHint("Please enter amount");

                }
            }
        });
        mWithdawInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mWithdawInput.removeTextChangedListener(this);

                try {
                    String originalString = s.toString().replaceAll(",", "");

                    double value = decimalFormat.parse(originalString).doubleValue();

                    String formattedString = decimalFormat.format(value).replaceAll("\\.",",");
                    mWithdawInput.setText(formattedString);
                    mWithdawInput.setSelection(formattedString.length());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mWithdawInput.addTextChangedListener(this);
            }
        });

        return view;
    }

    @Override
    public void onPinEntered(String pin) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pincode", pin);
        ApiService apiService=ApiService.ApiUtils.getApiService(WithdrawFragment.this.requireContext());
        apiService.authenPin(jsonObject).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()==true){
                    setWithdraw(apiService);
                }
                else{
                    Toast.makeText(WithdrawFragment.this.requireContext(), "Failure!" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAuthentication() {
        getPincode();
        dialogFragment.dismiss();
        Toast.makeText(WithdrawFragment.this.requireContext(), "authenticate success !", Toast.LENGTH_SHORT).show();
    }
    private void showPinDialog() {

        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), "pin_dialog");
    }

    private void setWithdraw(ApiService apiService){
        ProgressDialog progressDialog = new ProgressDialog(WithdrawFragment.this.requireContext());
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("amount", mWithdawInput.getText().toString());
        apiService.withdrawSer(jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responString;
                try {
                    responString = response.body().string();
                    if(responString.contains("you was withdrawal ")){
                        Toast.makeText(WithdrawFragment.this.requireContext(), responString , Toast.LENGTH_SHORT).show();
                        mWithdawInput.setText("");
                    }
                    else{
                        Toast.makeText(WithdrawFragment.this.requireContext(), responString , Toast.LENGTH_SHORT).show();
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

    private void getPincode(){

        ApiService apiService=ApiService.ApiUtils.getApiService(WithdrawFragment.this.requireContext());
        apiService.getPin().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String pin=response.body().string();
                    if(!pin.equals("can't authentication")){
                        onPinEntered(pin);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}