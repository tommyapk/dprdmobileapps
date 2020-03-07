package com.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

//this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
public class User {

    private String id_user ,name, email, alamat,no_hp,role_id,is_active,password,image_sm,image_lg,about_me,tgl_lahir;

    public User(String id_user, String name, String email, String alamat, String no_hp, String role_id, String is_active, String password, String image_sm, String image_lg,String tglLahir,String about) {
        this.id_user = id_user;
        this.name = name;
        this.about_me = about;
        this.tgl_lahir = tglLahir;
        this.email = email;
        this.alamat = alamat;
        this.no_hp = no_hp;
        this.alamat=alamat;
        this.role_id = role_id;
        this.is_active= is_active;
        this.password= password;
        this.image_sm = image_sm;
        this.image_lg =image_lg;

    }

    public String getIdUser() {
        return id_user;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAlamat() {
        return alamat;
    }
    public String getNoHp() {
        return no_hp;
    }
    public String getRoleid() {
        return role_id;
    }
    public String getIsActive() {
        return is_active;
    }
    public String getImageSm() {
        return image_sm;
    }
    public String getImageLg() {
        return image_lg;
    }
    public String getPassword() {
        return password;
    }


    public String getAbout() {
        return about_me;
    }
    public  String getTglLahir(){
        return  tgl_lahir;
    }
}