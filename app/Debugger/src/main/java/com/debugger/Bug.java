package com.debugger;

import android.os.Parcel;
import android.os.Parcelable;

class Bug implements Parcelable {
    private final String id;
    private final String originalCode;
    private String currentCode;
    private Language language;

    public Bug(String id, Language language, String originalCode) {
        this.id = id;
        this.language = language;
        //TODO: placeholder code
        //this.originalCode = originalCode;
        this.originalCode = "#! /usr/bin/env python3.3\n" +
                "\n" +
                "def alpha(n):\n" +
                "    n = \"c\"+n[0][2]\n" +
                "    c = [[],[],[]]\n" +
                "    c[0][1].append(n)\n" +
                "    return c\n" +
                "\n" +
                "a = alpha([\"shave\"])\n" +
                "#desired output is [[[],[[],'cave']]]\n" +
                "print(a)";
        currentCode = originalCode;
    }

    public void setCurrentCode(String code) { currentCode = code; }

    public String get_id() { return id; }
    public String getLanguage() { return language.toString(); }
    public String getOriginalCode() { return originalCode; }
    public String getCurrentCode() { return currentCode; }


    public String toString() {
        return language.asString() + ": " + id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(originalCode);
        out.writeString(currentCode);
        out.writeString(language.toString());
    }

    public static final Creator<Bug> CREATOR = new Creator<Bug>() {
        public Bug createFromParcel(Parcel in) {
            return new Bug(in);
        }
        public Bug[] newArray(int size) {
            return new Bug[size];
        }
    };

    private Bug(Parcel in) {
        id = in.readString();
        originalCode = in.readString();
        currentCode = in.readString();
        language = Language.valueOf(in.readString());
    }
}
