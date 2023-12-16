package com.example.nhatro.Controller.Giao_Dien_Open_App.DangKy_DangNhap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhatro.Controller.Bill_and_Search.Activity_Home;
import com.example.nhatro.Controller.Bill_and_Search.Progress_Dialog;
import com.example.nhatro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Login extends AppCompatActivity {

    private EditText edt_email, edt_pass;
    private TextView tv_forgot_password, tv_register;
    private AppCompatButton btn_login;
    private Progress_Dialog dialog;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhxa();
        set_text();
        click_event();
    }

    private void anhxa(){
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
        dialog = new Progress_Dialog(this);
    }

    private void set_text(){
        // setText cho textview_register
        String fullText1 = "Chưa có tài khoản? Đăng ký ở đây.";
        Spannable spannable1 = new SpannableString(fullText1);
        // Định nghĩa vị trí bắt đầu và kết thúc của phần cần thay đổi
        int start1 = fullText1.indexOf("Đăng ký");
        int end1 = start1 + "Đăng ký".length();
        // Đặt màu xanh cho phần văn bản được chọn
        ForegroundColorSpan redColorSpan1 = new ForegroundColorSpan(Color.parseColor("#EF4339"));
        spannable1.setSpan(redColorSpan1, start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Thêm UnderlineSpan để tạo gạch chân dưới cho phần văn bản được chọn
        UnderlineSpan underlineSpan1 = new UnderlineSpan();
        spannable1.setSpan(underlineSpan1, start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Áp dụng Spannable vào TextView
        tv_register.setText(spannable1);
    }

    private void click_event(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log_in();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Login.this, Activity_Register.class));
                finish();
            }
        });
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Login.this, Activity_Forgot_Password.class));
                finish();
            }
        });
    }

    private void log_in(){
        String email = edt_email.getText().toString();
        String pass = edt_pass.getText().toString();
        if (TextUtils.isEmpty(email)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Đăng nhập thất bại");
            builder.setMessage("Bỏ trống Email! Vui lòng nhập lại.");

            // Thêm nút "Đóng" vào thông báo
            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            // Hiển thị thông báo
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (!email.contains("@gmail.com")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Đăng nhập thất bại");
            builder.setMessage("Email không đúng định dạng! Vui lòng nhập lại.");

            // Thêm nút "Đóng" vào thông báo
            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            // Hiển thị thông báo
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (TextUtils.isEmpty(pass)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Đăng nhập thất bại");
            builder.setMessage("Mật khẩu không được để trống! Vui lòng nhập lại.");

            // Thêm nút "Đóng" vào thông báo
            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            // Hiển thị thông báo
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (pass.length() < 6) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Đăng nhập thất bại");
            builder.setMessage("Mật khẩu phải từ 6 ký tự trở lên!");

            // Thêm nút "Đóng" vào thông báo
            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            // Hiển thị thông báo
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else {
            dialog.ShowDilag("Đang đăng nhập vào ứng dụng");
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    dialog.HideDialog();
                    Intent intent = new Intent(Activity_Login.this, Activity_Home.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.HideDialog();
                    // If sign in fails, display a message to the user.
                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Login.this);
                    builder.setTitle("Đăng nhập thất bại");
                    builder.setMessage("Email hoặc mật khẩu không đúng! Vui lòng đăng nhập lại.");

                    // Thêm nút "Đóng" vào thông báo
                    builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // Hiển thị thông báo
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

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