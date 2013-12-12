package com.debugger;

import android.os.Parcel;
import android.os.Parcelable;

class Bug implements Parcelable {
    private final String solutionId;
    private final String bugId;
    private final String originalCode;
    private String currentCode;
    private Language language;

    public Bug(String solutionId, String bugId, Language language, String originalCode) {
        this.solutionId = solutionId;
        this.bugId = bugId;
        this.language = language;
        this.originalCode = originalCode;
        currentCode = this.originalCode;
    }

    public void setCurrentCode(String code) { currentCode = code; }

    public String getSolutionId() { return solutionId; }
    public String getBugId() { return bugId; }
    public Language getLanguage() { return language; }
    public String getOriginalCode() { return originalCode; }
    public String getCurrentCode() { return currentCode; }


    public String toString() {
        return language.getName() + ": " + bugId;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(solutionId);
        out.writeString(bugId);
        out.writeString(language.name());
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
        solutionId = in.readString();
        bugId = in.readString();
        language = Language.valueOf(in.readString());
        originalCode = in.readString();
        currentCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
