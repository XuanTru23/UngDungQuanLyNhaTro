package com.example.nhatro.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bills {
    String id_Bill;
    String id_Phong;
    String id_User;
    String so_Dien;
    String so_Nuoc;
    String ngay_Thu;
    String tinh_Trang;
    String tong_Tien;

    public Bills(){}

    public Bills(String id_Bill, String id_Phong, String id_User, String so_Dien, String so_Nuoc, String ngay_Thu, String tinh_Trang, String tong_Tien) {
        this.id_Bill = id_Bill;
        this.id_Phong = id_Phong;
        this.id_User = id_User;
        this.so_Dien = so_Dien;
        this.so_Nuoc = so_Nuoc;
        this.ngay_Thu = ngay_Thu;
        this.tinh_Trang = tinh_Trang;
        this.tong_Tien = tong_Tien;
    }

    public String getId_Bill() {
        return id_Bill;
    }

    public void setId_Bill(String id_Bill) {
        this.id_Bill = id_Bill;
    }

    public String getId_Phong() {
        return id_Phong;
    }

    public void setId_Phong(String id_Phong) {
        this.id_Phong = id_Phong;
    }

    public String getSo_Dien() {
        return so_Dien;
    }

    public void setSo_Dien(String so_Dien) {
        this.so_Dien = so_Dien;
    }

    public String getSo_Nuoc() {
        return so_Nuoc;
    }

    public void setSo_Nuoc(String so_Nuoc) {
        this.so_Nuoc = so_Nuoc;
    }

    public String getNgay_Thu() {
        return ngay_Thu;
    }

    public void setNgay_Thu(String ngay_Thu) {
        this.ngay_Thu = ngay_Thu;
    }

    public String getTinh_Trang() {
        return tinh_Trang;
    }

    public void setTinh_Trang(String tinh_Trang) {
        this.tinh_Trang = tinh_Trang;
    }

    public String getTong_Tien() {
        return tong_Tien;
    }

    public void setTong_Tien(String tong_Tien) {
        this.tong_Tien = tong_Tien;
    }

    public String getId_User() {
        return id_User;
    }

    public void setId_User(String id_User) {
        this.id_User = id_User;
    }

    public String FormattedDate() {
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy");

            Date date = sdfInput.parse(ngay_Thu);
            // Lấy ngày, tháng và năm từ đối tượng Date
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

            String day = dayFormat.format(date);
            String month = monthFormat.format(date);
            String year = yearFormat.format(date);

            return "Hóa đơn Tháng " + month + " Năm " + year;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
