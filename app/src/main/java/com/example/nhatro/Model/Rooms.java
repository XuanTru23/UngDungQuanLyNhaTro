package com.example.nhatro.Model;

public class Rooms {
    String id_Phong;
    String id_User;
    String id_nguoithue;
    String ten_Phong;
    String tinh_Trang;
    String ngay_Thue;
    String gia_Phong;
    String ngay_thu_tien;

    public Rooms(){}

    public Rooms(String id_Phong, String id_User, String id_nguoithue, String ten_Phong, String tinh_Trang, String ngay_Thue, String gia_Phong, String ngay_thu_tien) {
        this.id_Phong = id_Phong;
        this.id_User = id_User;
        this.id_nguoithue = id_nguoithue;
        this.ten_Phong = ten_Phong;
        this.tinh_Trang = tinh_Trang;
        this.ngay_Thue = ngay_Thue;
        this.gia_Phong = gia_Phong;
        this.ngay_thu_tien = ngay_thu_tien;
    }

    public String getId_Phong() {
        return id_Phong;
    }

    public void setId_Phong(String id_Phong) {
        this.id_Phong = id_Phong;
    }

    public String getId_User() {
        return id_User;
    }

    public void setId_User(String id_User) {
        this.id_User = id_User;
    }

    public String getId_nguoithue() {
        return id_nguoithue;
    }

    public void setId_nguoithue(String id_nguoithue) {
        this.id_nguoithue = id_nguoithue;
    }

    public String getTen_Phong() {
        return ten_Phong;
    }

    public void setTen_Phong(String ten_Phong) {
        this.ten_Phong = ten_Phong;
    }

    public String getTinh_Trang() {
        return tinh_Trang;
    }

    public void setTinh_Trang(String tinh_Trang) {
        this.tinh_Trang = tinh_Trang;
    }

    public String getNgay_Thue() {
        return ngay_Thue;
    }

    public void setNgay_Thue(String ngay_Thue) {
        this.ngay_Thue = ngay_Thue;
    }

    public String getGia_Phong() {
        return gia_Phong;
    }

    public void setGia_Phong(String gia_Phong) {
        this.gia_Phong = gia_Phong;
    }

    public String getNgay_thu_tien() {
        return ngay_thu_tien;
    }

    public void setNgay_thu_tien(String ngay_thu_tien) {
        this.ngay_thu_tien = ngay_thu_tien;
    }
}
