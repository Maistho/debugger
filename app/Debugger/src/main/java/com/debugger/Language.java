package com.debugger;

/**
 * Currently available languages
 * use Language.valueOf("PYTHON2") to get enumerable by name
 */
public enum Language {
    PYTHON2 ("Python 2.7", "2.7.6");
        /*
        PYTHON3 ("Python 3.3", "3.3.3"),
        JAVA ("Java 7", "1.7.45"),
        CPP ("C++11", "C++11"),
        RUBY ("Ruby 2.0", "2.0.0-p353");
        */

    private String name;
    private String version;

    Language(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
    //Warning: do not overload toString, used to implement Parcelable Bugs
}
