package com.debugger;

/**
 * ${FILENAME} created by maistho on 11/17/13.
 */
class Bug {
    private final int id;
    private final String originalCode;

    public Bug(int id) {
        this.id = id;
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
    }

    public int getId() {
        return id;
    }

    public String getOriginalCode() {
        return originalCode;
    }
}
