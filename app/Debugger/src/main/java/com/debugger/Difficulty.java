package com.debugger;

enum Difficulty {
    EASY ("Easy"),
    MEDIUM ("Medium"),
    HARD ("Hard");

    private final String name;
    private Difficulty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
