package com.example.nhatro.Controller.Room;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.nhatro.Controller.Bill_and_Search.Progress_Dialog;
import com.example.nhatro.R;
import com.example.nhatro.Model.Rooms;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Fragment_Add_Room extends Fragment {
    private View view;
    String[] item = {"Chưa có người thuê", "Đã có ngươi thuê"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItem;
    private EditText edt_ten_phong, edt_thu_tien, edt_gia_phong;
    private TextView tv_ngay_thue;
    private AppCompatButton btn_add_room;
    private ImageView img_back;
    private String tinh_trang;
    private Progress_Dialog dialog;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String idUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_room, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("Rooms");

        user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getUid();

        anhxa();
        autoCompleteTextView();
        click_event();


        return view;
    }
    private void anhxa(){
        autoCompleteTextView = view.findViewById(R.id.auto_complete_tv);
        edt_ten_phong = view.findViewById(R.id.edt_ten_phong);
        edt_thu_tien = view.findViewById(R.id.edt_thu_tien);
        edt_gia_phong = view.findViewById(R.id.edt_gia_phong);
        tv_ngay_thue = view.findViewById(R.id.tv_ngay_thue);
        btn_add_room = view.findViewById(R.id.btn_add_room);
        img_back = view.findViewById(R.id.img_back);
        dialog = new Progress_Dialog(view.getContext());

    }
    private void autoCompleteTextView(){
        adapterItem = new ArrayAdapter<String>(view.getContext(), R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItem);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tinh_trang = adapterView.getItemAtPosition(i).toString();
                if (tinh_trang.equals("Chưa có người thuê")) {
                    edt_gia_phong.setText("");
                    edt_thu_tien.setText("");
                    tv_ngay_thue.setText("");
                    tv_ngay_thue.setEnabled(false);
                    edt_gia_phong.setEnabled(false);
                    edt_thu_tien.setEnabled(false);
                } else {
                    edt_thu_tien.setEnabled(true);
                    edt_gia_phong.setEnabled(true);
                    tv_ngay_thue.setEnabled(true);
                }
            }
        });
    }
    private void click_event(){
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new Fragment_Home()).commit();
            }
        });
        tv_ngay_thue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_calendar();
            }
        });

        btn_add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themPhong();
            }
        });
    }
    // Interface để xử lý kết quả kiểm tra sự tồn tại của sản phẩm

    private void check_data_product(final ProductExistenceListener listener){
        String tp = edt_ten_phong.getText().toString().trim();
        Query query = databaseReference.orderByChild("ten_Phong").equalTo(tp);
        if (tp.isEmpty()){
            Toast.makeText(view.getContext(), "Tên phòng bỏ trống", Toast.LENGTH_SHORT).show();
        } else {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Nếu dataSnapshot không rỗng, tức là mã sản phẩm đã tồn tại
                    if (snapshot.exists()) {
                        // Kiểm tra xem tên phòng có thuộc về người dùng đang đăng nhập không
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String userIdFromDatabase = dataSnapshot.child("id_User").getValue(String.class);
                            if (userIdFromDatabase != null && userIdFromDatabase.equals(idUser)) {
                                // Tên phòng đã tồn tại và thuộc về người dùng đang đăng nhập
                                listener.onProductExists();
                            } else {
                                // Tên phòng đã tồn tại, nhưng không thuộc về người dùng đang đăng nhập
                                listener.onProductNotExists();
                            }
                        }
                    } else {
                        // Tên phòng chưa tồn tại
                        listener.onProductNotExists();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý khi có lỗi xảy ra trong quá trình truy vấn
                    listener.onError(error.getMessage());
                }
            });
        }
    }
    public interface ProductExistenceListener {
        void onProductExists();
        void onProductNotExists();
        void onError(String errorMessage);
    }


    // Interface để xử lý kết quả kiểm tra sự tồn tại của sản phẩm
    private interface CheckRooms{
        void Yes_Room();
        void No_Room();
        void Error_Room(String errorMessage);
    }
    private void themPhong(){
        String ten_phong = edt_ten_phong.getText().toString().trim();
        String ngay_thue = tv_ngay_thue.getText().toString();
        String ngay_thu_tien = edt_thu_tien.getText().toString();
        String gia_phong = edt_gia_phong.getText().toString();
        String tinhTrang = autoCompleteTextView.getText().toString();

        check_data_product(new ProductExistenceListener() {
            @Override
            public void onProductExists() {
                // Mã sản phẩm đã tồn tại, hiển thị thông báo
                Toast.makeText(view.getContext(), "Tên phòng đã tồn tại.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProductNotExists() {
                if (tinhTrang.isEmpty()){
                    Toast.makeText(view.getContext(), "Chưa chọn tình trạng phòng", Toast.LENGTH_SHORT).show();
                } else {
                    // Tạo node Rooms
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Rooms");
                    // Tạo khóa tự động và lấy giá trị của từng room con
                    String id_room = reference.push().getKey();

                    Rooms rooms = new Rooms();
                    rooms.setId_Phong(id_room);
                    rooms.setId_User(idUser);
                    rooms.setTen_Phong(ten_phong);
                    rooms.setTinh_Trang(tinhTrang);
                    rooms.setNgay_Thue(ngay_thue);
                    rooms.setNgay_thu_tien(ngay_thu_tien);
                    rooms.setGia_Phong(gia_phong);
                    dialog.ShowDilag("Đang thêm phòng");
                    reference.child(id_room).setValue(rooms).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.HideDialog();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, new Fragment_Home()).commit();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.HideDialog();
                            Toast.makeText(view.getContext(), "Thêm phòng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void open_calendar(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Xử lý ngày được chọn
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                tv_ngay_thue.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }
}
