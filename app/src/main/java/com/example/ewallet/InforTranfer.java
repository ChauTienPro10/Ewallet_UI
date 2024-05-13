package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class InforTranfer extends AppCompatActivity implements pindialogAdapter.PinDialogListener {
    private TextView mName;
    private TextView mPhone;
    private LinearLayout mSubmit;

    String phone;
    final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private EditText mAmount;
    private EditText mNote;

    pindialogAdapter dialogFragment = new pindialogAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_tranfer);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        phone=intent.getStringExtra("phone");

        mName=findViewById(R.id.textView29);
        mPhone=findViewById(R.id.textView43);
        mSubmit=findViewById(R.id.buttonConfirm);
        mName.setText(name);
        mPhone.setText(phone);
        mAmount=findViewById(R.id.editTextText);
        mNote=findViewById(R.id.editTextText2);
        mAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAmount.removeTextChangedListener(this);

                try {
                    String originalString = s.toString().replaceAll(",", "");

                    double value = decimalFormat.parse(originalString).doubleValue();

                    String formattedString = decimalFormat.format(value).replaceAll("\\.",",");
                    mAmount.setText(formattedString);
                    mAmount.setSelection(formattedString.length());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mAmount.addTextChangedListener(this);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPinDialog();
            }
        });
    }
    private void showPinDialog() {
        try{
//            DialogFragment df = new DialogFragment();
//            df.show(getSupportFragmentManager(), "MyDF");

            dialogFragment.show(getSupportFragmentManager(), "pin_dialog");
        }
        catch (Exception e){
            Log.d("dialogTien", "showPinDialog: "+e.toString());
        }

    }

    @Override
    public void onPinEntered(String pin) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pincode", pin);
        ApiService apiService=ApiService.ApiUtils.getApiService(InforTranfer.this);
        apiService.authenPin(jsonObject).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                setTransfer(apiService);
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
        Toast.makeText(InforTranfer.this, "authenticate success !", Toast.LENGTH_SHORT).show();
    }
    private void getPincode(){

        ApiService apiService=ApiService.ApiUtils.getApiService(InforTranfer.this);
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
    private void setTransfer(ApiService apiService){
        ProgressDialog progressDialog = new ProgressDialog(InforTranfer.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("amount", mAmount.getText().toString());
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("note",mNote.getText().toString());
        Log.d("tientest", "setTransfer: "+ mAmount.getText().toString()+" -"+phone);
        apiService.transfer(jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responString;
                try {
                    responString = response.body().string();
                    Toast.makeText(InforTranfer.this, responString, Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                    Intent intent=new Intent(InforTranfer.this, Successful_money_transfer.class );
                    intent.putExtra("receiver",mName.getText());
                    intent.putExtra("amount",mAmount.getText()+ "vnd");
                    intent.putExtra("note",mNote.getText().toString());
                    startActivity(intent);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("tientest", "setTransfer: "+ t.toString());
                progressDialog.dismiss();
            }
        });
    }
}