package com.example.ewallet;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import java.util.ArrayList;

public class AuthenticationPage extends AppCompatActivity {


    View view1, view2, view3, view4, view5, view6;
    ConstraintLayout btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_X;

    ArrayList<String> numbers_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_page);
        initializeComponent();
    }

    private void initializeComponent() {

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        view6 = findViewById(R.id.view6);

        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_7 = findViewById(R.id.btn_7);
        btn_8 = findViewById(R.id.btn_8);
        btn_9 = findViewById(R.id.btn_9);
        btn_0 = findViewById(R.id.btn_0);
        btn_X = findViewById(R.id.btn_X);

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
                    view1.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 2:
                    view2.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 3:
                    view3.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 4:
                    view4.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 5:
                    view5.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
                case 6:
                    view6.setBackgroundResource(R.drawable.bg_view_digits);
                    break;
            }
        }
    }
}