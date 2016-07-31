package com.example.owner.fortune;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.*;

/**
 * Created by Dung Trinh on 6/20/2016.
 */
public class Quote implements Parcelable {
    /** The main content of the quote*/
    private String content;

    public Quote (String content)
    {
        this.content = content;
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            Quote joke = new Quote(in.readString());
            return joke;
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    public String toString()
    {
        return this.content;
    }

    public boolean equals(Objects obj)
    {
        if (obj == null || !this.content.equals(obj.toString()))
        {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(content);
    }
}
