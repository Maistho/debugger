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
        currentCode = this.originalCode;
    }

    public void setCurrentCode(String code) { currentCode = code; }

    public String getId() { return id; }
    public Language getLanguage() { return language; }
    public String getOriginalCode() { return originalCode; }
    public String getCurrentCode() { return currentCode; }


    public String toString() {
        return language.getName() + ": " + id;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(language.toString());
        out.writeString(originalCode);
        out.writeString(currentCode);
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
        language = Language.valueOf(in.readString());
        originalCode = in.readString();
        currentCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
