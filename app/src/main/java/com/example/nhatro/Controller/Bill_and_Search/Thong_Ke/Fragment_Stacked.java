package com.example.nhatro.Controller.Bill_and_Search.Thong_Ke;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nhatro.Model.Bills;
import com.example.nhatro.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Fragment_Stacked extends Fragment {
    private View view;
    private BarChart barChart;
    private Spinner yearSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stacked, container, false);
        anhxa();
        setupYearSpinner();

        // Lắng nghe sự kiện chọn năm từ Spinner
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = yearSpinner.getSelectedItem().toString();
                loadAndDisplayData(selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có năm nào được chọn
            }
        });

        // Mặc định hiển thị dữ liệu cho năm hiện tại
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearSpinner.setSelection(getYearPosition(currentYear));

        return view;
    }
    private void anhxa(){
        barChart = view.findViewById(R.id.barChart);
        yearSpinner = view.findViewById(R.id.yearSpinner);
    }

    // Cấu hình Spinner để hiển thị danh sách năm
    private void setupYearSpinner() {
        List<String> years = new ArrayList<>();

        // Thêm các năm vào danh sách (có thể tùy chỉnh cho dự án của bạn)
        for (int year = 2020; year <= 2030; year++) {
            years.add(String.valueOf(year));
        }

        // Tạo ArrayAdapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Đặt ArrayAdapter cho Spinner
        yearSpinner.setAdapter(adapter);
    }

    // Lấy vị trí của năm trong Spinner
    private int getYearPosition(int year) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) yearSpinner.getAdapter();
        return adapter.getPosition(String.valueOf(year));
    }

    private void loadAndDisplayData(String selectedYear){
        // Cấu hình biểu đồ
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(12);

        // Lấy ID người dùng
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = user.getUid();

        // Kết nối đến Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bills");
        databaseReference.orderByChild("id_User").equalTo(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Xử lý dữ liệu Bills dựa trên năm đã chọn và cập nhật BarChart
                List<BarEntry> barEntries = new ArrayList<>();
                for (int month = 1; month <= 12; month++) {
                    float totalAmount = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Bills bill = snapshot.getValue(Bills.class);
                        if (bill != null && bill.getNgay_Thu().contains(selectedYear) && extractMonthFromDate(bill.getNgay_Thu()) == month && bill.getTinh_Trang().equals("Đã thu tiền")) {
                            String tong_tien = bill.getTong_Tien();
                            float tongTien = Float.parseFloat(tong_tien);
                            totalAmount += tongTien;
                        }
                    }
                    // Thêm dữ liệu vào danh sách BarEntry
                    barEntries.add(new BarEntry(month, totalAmount));
                }
                // Tạo BarDataSet và cập nhật BarChart
                BarDataSet dataSet = new BarDataSet(barEntries, "Doanh thu theo tháng");
                dataSet.setColor(getResources().getColor(R.color.custom_color)); // Màu sắc của cột
                dataSet.setValueTextColor(getResources().getColor(android.R.color.black)); // Màu sắc của giá trị trên cột

                BarData barData = new BarData(dataSet);
                barChart.setData(barData);

                // Cập nhật biểu đồ
                barChart.invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private int extractMonthFromDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date parsedDate = dateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            return calendar.get(Calendar.MONTH) + 1; // +1 vì tháng bắt đầu từ 0
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
