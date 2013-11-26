package com.debugger;

class Bug {
    private final int id;
    private final String originalCode;
    private String currentCode;

    public Bug(int id) {
        this.id = id;
        originalCode = "#! /usr/bin/env python3.3\n" +
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

    public int getId() {
        return id;
    }

    public String getOriginalCode() {
        return originalCode;
    }
    public String getCurrentCode() { return currentCode; }
    public void setCurrentCode(String code) {
        currentCode = code;
    }
}
