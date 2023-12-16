package com.example.nhatro.Model;

public class Tenants {
    String id_Nguoi_Thue;
    String id_Phong;
    String ten_Nguoi_Thue;
    String CCCD;
    String SDT;
    String dia_chi;
    public Tenants(){}
    public Tenants(String id_Nguoi_Thue, String id_Phong, String ten_Nguoi_Thue, String CCCD, String SDT, String dia_chi) {
        this.id_Nguoi_Thue = id_Nguoi_Thue;
        this.id_Phong = id_Phong;
        this.ten_Nguoi_Thue = ten_Nguoi_Thue;
        this.CCCD = CCCD;
        this.SDT = SDT;
        this.dia_chi = dia_chi;
    }

    public String getId_Nguoi_Thue() {
        return id_Nguoi_Thue;
    }

    public void setId_Nguoi_Thue(String id_Nguoi_Thue) {
        this.id_Nguoi_Thue = id_Nguoi_Thue;
    }

    public String getId_Phong() {
        return id_Phong;
    }

    public void setId_Phong(String id_Phong) {
        this.id_Phong = id_Phong;
    }

    public String getTen_Nguoi_Thue() {
        return ten_Nguoi_Thue;
    }

    public void setTen_Nguoi_Thue(String ten_Nguoi_Thue) {
        this.ten_Nguoi_Thue = ten_Nguoi_Thue;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
    }
}
