package com.example.nhatro.Controller.Giao_Dien_Open_App.Onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhatro.Controller.Bill_and_Search.Activity_Home;
import com.example.nhatro.Controller.Giao_Dien_Open_App.DangKy_DangNhap.Activity_Login;
import com.example.nhatro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.relex.circleindicator.CircleIndicator;

public class Activity_Onboarding extends AppCompatActivity {

    private TextView tv_skip;
    private ViewPager viewPager;
    
    private AppCompatButton btn_next;
    private CircleIndicator circleIndicator;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        anhxa();
        CheckLogin();

        // Khởi tạo OnboardinViewPagergAdapter
        OnboardingViewPagergAdapter viewPagergAdapter = new OnboardingViewPagergAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // Đặt viewPagergAdapter đã tạo vào viewPager. Nó liên kết adapter với ViewPager, cho phép quản lý và hiển thị các fragment.
        viewPager.setAdapter(viewPagergAdapter);

        // Dòng này liên kết CircleIndicator với ViewPager Indicator được sử dụng để hiển thị dấu hiệu hình ảnh cho người dùng về số lượng trang và trang hiện tại đang được chọn trong ViewPager.
        circleIndicator.setViewPager(viewPager);

        // Bắt sự kiện cho textview Skip
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // khi nhấn skip thì chuyển đến viewPager với vị trí là 2
                viewPager.setCurrentItem(2);
            }
        });

        // Bắt sự kiện cho button next
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // nếu vị trí các viewPager nhỏ hơn 2 thì tăng vị trí lên một và nếu vị trí bằng 2 thì khi click button next sẽ chuyển màn hình
                if (viewPager.getCurrentItem() < 2){
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {
                    startActivity(new Intent(Activity_Onboarding.this, Activity_Login.class));
                    finishAffinity();
                }
            }
        });

        // Lăng nghe sự kiện khi viewPager thay đổi
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // Phương thức onPageSelected nếu vị trí ViewPager thay đổi = 2 thì tv_skip sẽ thay đổi từ chữ "Bỏ qua" thành chữ "Xong" và button next cũng thay đổi thành "Bắt đầu"
            @Override
            public void onPageSelected(int position) {
                if (position == 2){
                    tv_skip.setText("Xong");
                    btn_next.setText("Bắt đầu");
                }
                else {
                    tv_skip.setText("Bỏ qua");
                    btn_next.setText("Tiếp theo");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void anhxa(){
        tv_skip = findViewById(R.id.tv_skip);
        viewPager = findViewById(R.id.view_pager_intro);
        circleIndicator = findViewById(R.id.circleIndicator);
        btn_next = findViewById(R.id.btn_next);
        tv_skip.setText("Bỏ qua");
        btn_next.setText("Tiếp theo");
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

    private void CheckLogin(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Người dùng đã đăng nhập
            Intent intent = new Intent(Activity_Onboarding.this, Activity_Home.class);
            startActivity(intent);
            finish();
        }
    }
}