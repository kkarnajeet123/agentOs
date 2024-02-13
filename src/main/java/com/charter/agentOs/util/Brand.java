package com.charter.agentOs.util;

public enum Brand {
    IPHONE("Iphone"), SAMSUNG("Samsung"), GOOGLE("Google"), MOTOROLLA("Motorolla");
    private final String name;
    Brand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
