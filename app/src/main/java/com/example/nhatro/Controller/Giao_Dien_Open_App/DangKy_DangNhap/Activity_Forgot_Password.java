package com.example.nhatro.Controller.Giao_Dien_Open_App.DangKy_DangNhap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhatro.Controller.Bill_and_Search.Progress_Dialog;
import com.example.nhatro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Forgot_Password extends AppCompatActivity {

    private ImageView img_back;
    private TextView textView;
    private EditText edt_email;
    private AppCompatButton btn_send, btn_send_lai;
    private Progress_Dialog dialog;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        anhxa();
        click_event();
    }
    private void anhxa(){
        textView = findViewById(R.id.textView);
        img_back = findViewById(R.id.img_back);
        edt_email = findViewById(R.id.edt_email);
        btn_send = findViewById(R.id.btn_send);
        btn_send_lai = findViewById(R.id.btn_send_lai);
        dialog = new Progress_Dialog(this);
    }
    private void click_event(){
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Forgot_Password.this, Activity_Login.class));
                finishAffinity();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_link();
            }
        });
        btn_send_lai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_email.setVisibility(View.VISIBLE);
                btn_send.setVisibility(View.VISIBLE);
                btn_send_lai.setVisibility(View.GONE);
                textView.setText("Nhập Email mà tài khoản bạn đã đăng ký để nhận link đặt lại mật khẩu. Chú ý tin nhắn đặt lại mật khẩu có thể nằm ở mục Spam hoặc mục thư rác.");
            }
        });
    }
    private void send_link(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = edt_email.getText().toString();
        if (TextUtils.isEmpty(emailAddress)) {
            Toast.makeText(this, "Email không được để trống!", Toast.LENGTH_SHORT).show();
        } else if (!emailAddress.contains("@gmail.com")) {
            Toast.makeText(this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
        } else {
            dialog.ShowDilag("Đang gửi link đặt lại mật khẩu.");
            auth.sendPasswordResetEmail(emailAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    dialog.HideDialog();
                    textView.setText("Vui lòng kiểm tra Email " + emailAddress + ". Chú ý tin nhắn đặt lại mật khẩu có thể nằm ở mục Spam hoặc mục thư rác. Vui lòng nhấn phím mũi tên để trở lại màn hình đăng nhập.");
                    edt_email.setVisibility(View.GONE);
                    btn_send.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.HideDialog();
                    textView.setText("Gửi link đặt lại mật khẩu thất bại! Có thể Email " + emailAddress + " chưa được đăng ký.");
                    edt_email.setVisibility(View.GONE);
                    btn_send.setVisibility(View.GONE);
                    btn_send_lai.setVisibility(View.VISIBLE);
                }
            });
        }

    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Ấn thoát lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}