package com.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Kecamatan_model {
    private  String id_kecamatan;
    private  String kecamatan;
    private  String id_dapil;

    public Kecamatan_model(JSONObject objKec) throws JSONException {
        this.id_kecamatan = objKec.getString("id_kec");
        this.kecamatan = objKec.getString("kecamatan");
        this.id_dapil = objKec.getString("dapil_id");
    }

    public String getId_kecamatan() {
        return id_kecamatan;
    }

    public void setId_kecamatan(String id_kecamatan) {
        this.id_kecamatan = id_kecamatan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getId_dapil() {
        return id_dapil;
    }

    public void setId_dapil(String id_dapil) {
        this.id_dapil = id_dapil;
    }
}

