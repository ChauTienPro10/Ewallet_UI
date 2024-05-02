package com.example.ewallet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

import Entities.Member;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private RecyclerView.Adapter adapterContact;
    private RecyclerView recyclerViewConatact;
    private ConstraintLayout btn_Transaction;
    private ImageView getMoney;
    private ImageView toDeposit;
    private TextView mName;
    TextView mBalance;

    private ConstraintLayout sendMoney;
    FloatingActionButton scanqr;

    private LinearLayout testAuth;
    Gson gson = new Gson();
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mName=findViewById(R.id.nameMember);
        mBalance=findViewById(R.id.textView);
        

        // Test Auth
        testAuth = findViewById(R.id.testAuth);
        testAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //Contact list
        initRecyclerView();
        toDeposit=findViewById(R.id.DepositImg);
        toDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, TransactionPage.class );
                startActivity(intent);
            }
        });

        sendMoney = findViewById(R.id.imageSend);
        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TranferMoney.class);
                startActivity(intent);
            }
        });

        scanqr=findViewById(R.id.scanQR);
        getMoney=findViewById(R.id.get_money);
        btn_Transaction = findViewById(R.id.btn_Transaction);

        ImageView imageProfile = findViewById(R.id.imageProfile);
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfilePage.class);
                startActivity(intent);
            }
        });


        getMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, GenerateQrCode.class );
                startActivity(intent);
            }
        });


        btn_Transaction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Chuyển đến TransactionPageActivity
                Intent intent = new Intent(MainActivity.this,TransactionPage.class);
                startActivity(intent);
            }
        });
        scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setPrompt("Scan a barcode");
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setCameraId(0); // Use a specific camera of the device
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setCaptureActivity(ScanQR.class);
                integrator.initiateScan();

            }
        });
    }

    View view1, view2, view3, view4, view5, view6;
    String passCode ="";
    String num_01,num_02,num_03,num_04,num_05,num_06;
    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_authentication_page);

        ImageView closeBottomSheet;
        ConstraintLayout btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_X;



        ArrayList<String> numbers_list = new ArrayList<>();

        closeBottomSheet = dialog.findViewById(R.id.closeBottomSheet);
        closeBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        view1 = dialog.findViewById(R.id.view1);
        view2 = dialog.findViewById(R.id.view2);
        view3 = dialog.findViewById(R.id.view3);
        view4 = dialog.findViewById(R.id.view4);
        view5 = dialog.findViewById(R.id.view5);
        view6 = dialog.findViewById(R.id.view6);

        btn_1 = dialog.findViewById(R.id.btn_1);
        btn_2 = dialog.findViewById(R.id.btn_2);
        btn_3 = dialog.findViewById(R.id.btn_3);
        btn_4 = dialog.findViewById(R.id.btn_4);
        btn_5 = dialog.findViewById(R.id.btn_5);
        btn_6 = dialog.findViewById(R.id.btn_6);
        btn_7 = dialog.findViewById(R.id.btn_7);
        btn_8 = dialog.findViewById(R.id.btn_8);
        btn_9 = dialog.findViewById(R.id.btn_9);
        btn_0 = dialog.findViewById(R.id.btn_0);
        btn_X = dialog.findViewById(R.id.btn_X);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("1");
                passNumber(numbers_list);
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("2");
                passNumber(numbers_list);
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("3");
                passNumber(numbers_list);
            }
        });


        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("4");
                passNumber(numbers_list);
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("5");
                passNumber(numbers_list);
            }
        });

        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("6");
                passNumber(numbers_list);
            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("7");
                passNumber(numbers_list);
            }
        });
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("8");
                passNumber(numbers_list);
            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("9");
                passNumber(numbers_list);
            }
        });
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.add("0");
                passNumber(numbers_list);
            }
        });
        btn_X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbers_list.clear();
                passNumber(numbers_list);
            }
        });
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations= R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    private void passNumber(ArrayList<String> numbers_list) {
        if (numbers_list.size() == 0) {
            view1.setBackgroundResource(R.drawable.bg_view_gray_digits);
            view2.setBackgroundResource(R.drawable.bg_view_gray_digits);
            view3.setBackgroundResource(R.drawable.bg_view_gray_digits);
            view4.setBackgroundResource(R.drawable.bg_view_gray_digits);
            view5.setBackgroundResource(R.drawable.bg_view_gray_digits);
            view6.setBackgroundResource(R.drawable.bg_view_gray_digits);
        } else {
            switch (numbers_list.size()) {
                case 1:
                    num_01= numbers_list.get(0);
                    view1.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 2:
                    num_02= numbers_list.get(1);
                    view2.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 3:
                    num_03= numbers_list.get(2);
                    view3.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 4:
                    num_04= numbers_list.get(3);
                    view4.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 5:
                    num_05= numbers_list.get(4);
                    view5.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 6:
                    num_06= numbers_list.get(5);
                    view6.setBackgroundResource(R.drawable.bg_view_digits);

                    passCode = num_01+num_02+num_03+num_04+num_05+num_06;
                    Log.d("PassCode",passCode);
                    break;
            }
        }

    }

    private void initRecyclerView() {
        ArrayList<ContactsDomain> items=new ArrayList<>();
        items.add(new ContactsDomain("David","user_1"));
        items.add(new ContactsDomain("Alice","user_2"));
        items.add(new ContactsDomain("Rose","user_3"));
        items.add(new ContactsDomain("Sara","user_4"));
        items.add(new ContactsDomain("David","user_5"));

        recyclerViewConatact=findViewById(R.id.viewList);
        recyclerViewConatact.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        adapterContact=new ContactsAdapter(items);
        recyclerViewConatact.setAdapter(adapterContact);
    }


    private void openFormDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Data");

        // Inflate the layout for the dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_qr_form, null);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText etName = view.findViewById(R.id.et_name);

        builder.setView(view);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString();
                Toast.makeText(MainActivity.this, "Entered name: " + name, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                ApiService apiService = ApiService.ApiUtils.getApiService(this);
                apiService.sendQrData(result.getContents()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(MainActivity.this, "send success !"+response.body(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "incorrect!"+t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void getMember(){
        ApiService apiService=ApiService.ApiUtils.getApiService(MainActivity.this);
        apiService.getMember().enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                Member mem=response.body();
                mName.setText(mem.getFname().toString()+" "+mem.getLname().toString());
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {

            }
        });
        apiService.getBalance().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseBody;
                responseBody = response.body();
                mBalance.setText(responseBody);


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }



}