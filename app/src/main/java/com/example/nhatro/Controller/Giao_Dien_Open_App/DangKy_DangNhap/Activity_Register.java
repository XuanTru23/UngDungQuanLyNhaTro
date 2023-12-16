package com.example.nhatro.Controller.Giao_Dien_Open_App.DangKy_DangNhap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhatro.Controller.Bill_and_Search.Progress_Dialog;
import com.example.nhatro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Activity_Register extends AppCompatActivity {

    private EditText edt_name, edt_email, edt_pass;
    private AppCompatRadioButton radio_button;
    private TextView tv_login;
    private AppCompatButton btn_register;
    private Progress_Dialog dialog;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        anhxa();
        set_text();
        click_event();
    }
    private void anhxa(){
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
        radio_button = findViewById(R.id.radio_button);
        tv_login = findViewById(R.id.tv_login);
        btn_register = findViewById(R.id.btn_register);
        dialog = new Progress_Dialog(this);
    }
    private void set_text(){
        String fullText = "Tôi đồng ý với TXT và Chính sách quyền riêng tư.";
        Spannable spannable = new SpannableString(fullText);
        // Định nghĩa vị trí bắt đầu và kết thúc của phần cần thay đổi
        int start = fullText.indexOf("TXT và Chính sách quyền riêng tư.");
        int end = start + "TXT và Chính sách quyền riêng tư.".length();
        // Đặt màu đen cho phần văn bản được chọn
        ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.BLACK);
        spannable.setSpan(redColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Thêm UnderlineSpan để tạo gạch chân dưới cho phần văn bản được chọn
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannable.setSpan(underlineSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Đặt độ đậm cho phần văn bản được chọn
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannable.setSpan(boldSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Áp dụng Spannable vào radioButton
        radio_button.setText(spannable);


        // setText cho textview_login
        String fullText1 = "Đã là thành viên? Đăng nhập tại đây.";
        Spannable spannable1 = new SpannableString(fullText1);
        // Định nghĩa vị trí bắt đầu và kết thúc của phần cần thay đổi
        int start1 = fullText1.indexOf("Đăng nhập");
        int end1 = start1 + "Đăng nhập".length();
        // Đặt màu xanh cho phần văn bản được chọn
        ForegroundColorSpan redColorSpan1 = new ForegroundColorSpan(Color.parseColor("#EF4339"));
        spannable1.setSpan(redColorSpan1, start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Thêm UnderlineSpan để tạo gạch chân dưới cho phần văn bản được chọn
        UnderlineSpan underlineSpan1 = new UnderlineSpan();
        spannable1.setSpan(underlineSpan1, start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Áp dụng Spannable vào TextView
        tv_login.setText(spannable1);
    }
    private void click_event(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_account();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Register.this, Activity_Login.class));
                finish();
            }
        });
    }
    private void create_account(){
        String name = edt_name.getText().toString();
        String email = edt_email.getText().toString();
        String pass = edt_pass.getText().toString();


        if (TextUtils.isEmpty(name)){
            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Register.this);
            builder.setTitle("Đăng ký tài khoản thất bại");
            builder.setMessage("Họ và tên không được để trống! Vui lòng nhập lại.");

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

        } else if (TextUtils.isEmpty(email)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Register.this);
            builder.setTitle("Đăng ký tài khoản thất bại");
            builder.setMessage("Email không được để trống! Vui lòng nhập lại.");

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
            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Register.this);
            builder.setTitle("Đăng ký tài khoản thất bại");
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
            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Register.this);
            builder.setTitle("Đăng ký tài khoản thất bại");
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

            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Register.this);
            builder.setTitle("Đăng ký tài khoản thất bại");
            builder.setMessage("Mật khẩu phải từ 6 ký tự trở lên! Vui lòng nhập lại.");

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

        } else if (!radio_button.isChecked()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Register.this);
            builder.setTitle("Đăng ký tài khoản thất bại");
            builder.setMessage("Bạn cần đồng ý với TXT và Chính sách quyền riêng tư.");

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
            dialog.ShowDilag("Đang tạo tài khoản");
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = auth.getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
                    if (user != null){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id_User", user.getUid());
                        hashMap.put("name_User", name);
                        hashMap.put("email_User", email);
                        hashMap.put("gia_Nuoc", "0");
                        hashMap.put("gia_Dien", "0");
                        hashMap.put("image_Url", "default");
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.HideDialog();
                                    startActivity(new Intent(Activity_Register.this, Activity_Login.class));
                                    finish();
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.HideDialog();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Register.this);
                    builder.setTitle("Đăng ký tài khoản thất bại");
                    builder.setMessage("Tạo tài khoản thất bại! Tài khoản Email đã tồn tại.");

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