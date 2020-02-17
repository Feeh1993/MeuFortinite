package com.example.meufortinite.MODEL;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Usuario implements Parcelable
{

    public String id;

    public static final String TABLE_NAME = "usuario";
    public static final String COLUMN_ID = "id";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + ")";

    public Usuario(String id)
    {
        this.id = id;
    }

    public Usuario() {
    }

    protected Usuario(Parcel in)
    {
        id = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }

}
