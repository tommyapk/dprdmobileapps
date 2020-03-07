package com.adapter;

import android.widget.ImageView;

public class ContactListAdapter {

    String nama,komisi,isi;
//    ImageView img;

    public ContactListAdapter() {

    }

    public ContactListAdapter(String nama, String komisi, String isi) {
        this.nama = nama;
        this.komisi = komisi;
        this.isi = isi;
//        this.img = img;
    }


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKomisi() {
        return komisi;
    }

    public void setKomisi(String komisi) {
        this.komisi = komisi;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

//    public int getImg() {
//        return img;
//    }

//    public void setImg(ImageView img) {
//        this.img = img;
//    }
}

