package com.siewe.inventorymanagementsystem.model.enumeration;

public enum Unit {

    Pieces("Pieces"),
    Kg("Kg"),
    Grammes("Grammes"),
    Litres("Litres");

    private final String value;

    Unit(String value) {
        this.value = value;
    }

    public static Unit fromValue(String value) {
        if (value != null) {
            for (Unit unit : values()) {
                if (unit.value.equals(value)) {
                    return unit;
                }
            }
        }

        // you may return a default value
        return getDefault();
        // or throw an exception
        // throw new IllegalArgumentException("Invalid unit: " + value);
    }

    public String toValue() {
        return value;
    }

    public static Unit getDefault() {
        return Pieces;
    }
}
