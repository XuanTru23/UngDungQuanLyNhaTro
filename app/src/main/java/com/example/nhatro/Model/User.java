package com.example.nhatro.Model;

public class User {
    String name_User;
    String id_User;
    String image_Url;
    String email_User;
    String gia_Nuoc;
    String gia_Dien;
    public User() {

    }

    public User(String name_User, String id_User, String image_Url, String email_User, String gia_Nuoc, String gia_Dien) {
        this.name_User = name_User;
        this.id_User = id_User;
        this.image_Url = image_Url;
        this.email_User = email_User;
        this.gia_Nuoc = gia_Nuoc;
        this.gia_Dien = gia_Dien;
    }

    public String getName_User() {
        return name_User;
    }

    public void setName_User(String name_User) {
        this.name_User = name_User;
    }

    public String getId_User() {
        return id_User;
    }

    public void setId_User(String id_User) {
        this.id_User = id_User;
    }

    public String getImage_Url() {
        return image_Url;
    }

    public void setImage_Url(String image_Url) {
        this.image_Url = image_Url;
    }

    public String getEmail_User() {
        return email_User;
    }

    public void setEmail_User(String email_User) {
        this.email_User = email_User;
    }

    public String getGia_Nuoc() {
        return gia_Nuoc;
    }

    public void setGia_Nuoc(String gia_Nuoc) {
        this.gia_Nuoc = gia_Nuoc;
    }

    public String getGia_Dien() {
        return gia_Dien;
    }

    public void setGia_Dien(String gia_Dien) {
        this.gia_Dien = gia_Dien;
    }
}
