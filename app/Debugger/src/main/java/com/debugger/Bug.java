package com.debugger;

class Bug {
    private final String id;
    private final String originalCode;
    private String currentCode;

    public Bug(String id, String originalCode) {
        this.id = id;
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

    //don't camelCase id - it's ugly!
    public String get_id() { return id; }
    public String getOriginalCode() { return originalCode; }
    public String getCurrentCode() { return currentCode; }

    public void setCurrentCode(String code) { currentCode = code; }


    public String toString() {
        return id;
    }
}
