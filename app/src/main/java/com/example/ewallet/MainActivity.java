package com.example.ewallet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewallet.adapter.ContactsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import Entities.Member;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterContact;
    private RecyclerView recyclerViewConatact;
    private ConstraintLayout btn_Transaction;
    private ImageView getMoney,mtoOpenCard;
    private ImageView toDeposit;
    private TextView mName;
    TextView mBalance;
    private ConstraintLayout sendMoney;
    FloatingActionButton scanqr;
    LinearLayout mToProfile,btn_history;
    Gson gson = new Gson();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mName=findViewById(R.id.nameMember);
        mBalance=findViewById(R.id.textView);
        mtoOpenCard=findViewById(R.id.toOpencard);
        mtoOpenCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, opencard.class );
                startActivity(intent);
            }
        });
        //button history transaction
        btn_history=findViewById(R.id.btnHistory);
        btn_history.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,HistoryTransactionPage.class)));

        // Test Auth
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mName.setText(sharedPreferences.getString("fullName", ""));
        getMember();
        // Test ETH
        LinearLayout testETH = findViewById(R.id.testETH);
        testETH.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ETH_Wallet.class)));

        //Contact list
        initRecyclerView();
        toDeposit=findViewById(R.id.DepositImg);
       // di den trang nap tien
        toDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, TransactionPage.class );
                startActivity(intent);
            }
        });
        mToProfile=findViewById(R.id.toProfile);
        sendMoney = findViewById(R.id.imageSend);
        //di den trang giao dich
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
        // di den trang thong tin ca nhan
        mToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfilePage.class);
                startActivity(intent);
            }
        });
        getMoney.setOnClickListener(new View.OnClickListener() { // di den tran tao ma QR
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
    private void initRecyclerView() { // khoi tao danh sach nguoi dung goi y
        ArrayList<ContactsDomain> items=new ArrayList<>();
        items.add(new ContactsDomain("David","user_1"));
        items.add(new ContactsDomain("Alice","user_2"));
        items.add(new ContactsDomain("Rose","user_3"));
        items.add(new ContactsDomain("Sara","user_4"));
        recyclerViewConatact=findViewById(R.id.viewList);
        recyclerViewConatact.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapterContact=new ContactsAdapter(items);
        recyclerViewConatact.setAdapter(adapterContact);
    }



//    xu ly ket qua ma qr tai day
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Phương thức này được gọi khi kết quả từ hoạt động khác được trả về.
        // requestCode là mã định danh của hoạt động được gọi.
        // resultCode là mã kết quả của hoạt động (RESULT_OK hoặc RESULT_CANCELED).
        // data là Intent chứa dữ liệu được trả về từ hoạt động.
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // Giải nén kết quả từ Intent để lấy thông tin quét mã vạch.
        if (result != null) {
            // Nếu có kết quả trả về
            if (result.getContents() == null) {
                // Nếu không có nội dung quét được
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                // Nếu có nội dung quét được

                String[] parts = result.getContents().split("\n");
                // Tách nội dung quét thành các phần tử (tên, số điện thoại, số tiền)
                String name = parts[0];
                String phone = parts[1];
                String amountString = parts[2];
                // Gán các giá trị tương ứng vào biến
                Intent intent=new Intent(MainActivity.this, InforTranfer.class );
                // Tạo một Intent mới để chuyển sang hoạt động InforTranfer
                intent.putExtra("name", name);
                intent.putExtra("phone",phone);
                intent.putExtra("amountExtra",amountString);
                // Truyền các giá trị tương ứng vào Intent
                startActivity(intent);
                // Khởi chạy hoạt động InforTranfer

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void getMember(){ // nhan thong tin ca nhan
        ApiService apiService=ApiService.ApiUtils.getApiService(MainActivity.this);
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