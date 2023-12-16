package com.example.nhatro.Controller.Bill_and_Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nhatro.Controller.Room.Fragment_Home;
import com.example.nhatro.Controller.Giao_Dien_Open_App.Tai_Khoan.Fragment_Setting;
import com.example.nhatro.Controller.Bill_and_Search.Thong_Ke.Fragment_Stacked;
import com.example.nhatro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_Home extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        anhxa();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new Fragment_Home());
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                int i = item.getItemId();
                if (item.getItemId() == R.id.item_home){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, new Fragment_Home())
                            .commit();
                    return true;
                } else if (item.getItemId() == R.id.item_stacked){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, new Fragment_Stacked())
                            .commit();
                    return true;
                } else if (item.getItemId() == R.id.item_person){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, new Fragment_Setting())
                            .commit();
                    return true;
                }
                return false;
            }
        });

    }

    private void anhxa(){
        bottomNavigationView = findViewById(R.id.bottom_nav_home);
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