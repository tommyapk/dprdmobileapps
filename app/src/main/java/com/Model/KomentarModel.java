package com.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class KomentarModel implements Parcelable {
    String image;
    String userName;
    String idKomentar;
    String komentar;
    String aspirasiId;
    String userId;
    String parent;
    String dateCreated;

    public KomentarModel(JSONObject komentar) throws JSONException {
        this.image = komentar.getString("image");
        this.userName = komentar.getString("username");
        this.idKomentar = komentar.getString("id_komentar");
        this.komentar = komentar.getString("komentar");
        this.aspirasiId = komentar.getString("aspirasi_id");
        this.userId = komentar.getString("user_id");
        this.parent = komentar.getString("parent");
        this.dateCreated = komentar.getString("dateCreated");
    }

    protected KomentarModel(Parcel in) {
        image = in.readString();
        userName = in.readString();
        idKomentar = in.readString();
        komentar = in.readString();
        aspirasiId = in.readString();
        userId = in.readString();
        parent = in.readString();
        dateCreated = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(userName);
        dest.writeString(idKomentar);
        dest.writeString(komentar);
        dest.writeString(aspirasiId);
        dest.writeString(userId);
        dest.writeString(parent);
        dest.writeString(dateCreated);
    }
    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<KomentarModel> CREATOR = new Creator<KomentarModel>() {
        @Override
        public KomentarModel createFromParcel(Parcel in) {
            return new KomentarModel(in);
        }

        @Override
        public KomentarModel[] newArray(int size) {
            return new KomentarModel[size];
        }
    };

    public String getImage() {
        return image;
    }

    public String getUserName() {
        return userName;
    }

    public String getIdKomentar() {
        return idKomentar;
    }

    public String getKomentar() {
        return komentar;
    }

    public String getAspirasiId() {
        return aspirasiId;
    }

    public String getUserId() {
        return userId;
    }

    public String getParent() {
        return parent;
    }

    public String getDateCreated() {
        return dateCreated;
    }


}
