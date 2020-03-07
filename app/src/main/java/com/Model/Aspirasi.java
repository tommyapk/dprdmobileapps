package com.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Aspirasi implements Parcelable {
    private String idAspirasi;
    private String message;
    private String komisiId;
    private String userId;
    private String status;
    private String dateCreated;
    private String kecId;
    private String idPenanggun;
    private String image;
    private String userName;
    private String komisi,nmPenanggun;

    public Aspirasi(JSONObject objAspirasi) throws JSONException {
        this.idAspirasi = objAspirasi.getString("id_aspirasi");
        this.message = objAspirasi.getString("message");
        this.komisiId = objAspirasi.getString("komisi_id");
        this.userId = objAspirasi.getString("user_id");
        this.status = objAspirasi.getString("status");
        this.dateCreated = objAspirasi.getString("dateCreated");
        this.kecId = objAspirasi.getString("kec_id");
        this.idPenanggun = objAspirasi.getString("penanggun");
        this.image = objAspirasi.getString("image");
        this.userName = objAspirasi.getString("username");
        this.komisi = objAspirasi.getString("komisi");
        this.nmPenanggun = objAspirasi.getString("nmpenanggun");
    }

    protected Aspirasi(Parcel in) {
        idAspirasi = in.readString();
        message = in.readString();
        komisiId = in.readString();
        userId = in.readString();
        status = in.readString();
        dateCreated = in.readString();
        kecId = in.readString();
        idPenanggun = in.readString();
        image = in.readString();
        userName = in.readString();
        komisi = in.readString();
        nmPenanggun = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idAspirasi);
        dest.writeString(message);
        dest.writeString(komisiId);
        dest.writeString(userId);
        dest.writeString(status);
        dest.writeString(dateCreated);
        dest.writeString(kecId);
        dest.writeString(idPenanggun);
        dest.writeString(image);
        dest.writeString(userName);
        dest.writeString(komisi);
        dest.writeString(nmPenanggun);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Aspirasi> CREATOR = new Creator<Aspirasi>() {
        @Override
        public Aspirasi createFromParcel(Parcel in) {
            return new Aspirasi(in);
        }

        @Override
        public Aspirasi[] newArray(int size) {
            return new Aspirasi[size];
        }
    };

    public String getIdAspirasi() {
        return idAspirasi;
    }

    public String getMessage() {
        return message;
    }

    public String getKomisiId() {
        return komisiId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getKecId() {
        return kecId;
    }

    public String getIdPenanggun() {
        return idPenanggun;
    }

    public String getImage() {
        return image;
    }

    public String getUserName() {
        return userName;
    }

    public String getKomisi() {
        return komisi;
    }

    public String getNmPenanggun() {
        return nmPenanggun;
    }
}
