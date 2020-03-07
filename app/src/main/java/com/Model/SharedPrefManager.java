package com.Model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.LoginActivity;

/**
 * Created by Belal on 9/5/2017.
 */

//here for this class we are using a singleton pattern

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "mUser";
    private static final String KEY_NAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_ALAMAT = "keyalamat";
    private static final String KEY_IDUSER = "keyiduser";
    private static final String KEY_NOHP= "nohp";
    private static final String KEY_ROLEID = "roleid";
    private static final String KEY_ISACTIVE = "ISACTIVE";
    private static final String KEY_PASSWORD= "password";
    private static final String KEY_IMAGESM= "image kecil";
    private static final String KEY_IMAGELG = "image besar";
    private static final String KEY_TGLLAHIR = "172663636";
    private static final String KEY_ABOUT = "tentang saya";


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IDUSER, user.getIdUser());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_ALAMAT, user.getAlamat());
        editor.putString(KEY_NOHP,user.getNoHp());
        editor.putString(KEY_IMAGELG,user.getImageLg());
        editor.putString(KEY_IMAGESM,user.getImageSm());
        editor.putString(KEY_ROLEID,user.getRoleid());
        editor.putString(KEY_ISACTIVE,user.getIsActive());
        editor.putString(KEY_PASSWORD,user.getPassword());
        editor.putString(KEY_ABOUT,user.getAbout());
        editor.putString(KEY_TGLLAHIR,user.getTglLahir());
        editor.apply();
    }

    public void komentarActive(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
    }
    public  void setImg(String imgSm,String imgLg){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IMAGESM, imgSm);
        editor.putString(KEY_IMAGESM, imgLg);
        editor.apply();
    }
    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_IDUSER, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_IDUSER, null),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_ALAMAT, null),
                sharedPreferences.getString(KEY_NOHP, null),
                sharedPreferences.getString(KEY_ROLEID, null),
                sharedPreferences.getString(KEY_ISACTIVE, null),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getString(KEY_IMAGESM, null),
                sharedPreferences.getString(KEY_IMAGELG, null),
                sharedPreferences.getString(KEY_TGLLAHIR, null),
                sharedPreferences.getString(KEY_ABOUT, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}