package com.siewe.inventorymanagementsystem.model.enumeration;

public enum TypePaiement {

    Comptant("Comptant"),
    Credit("Credit");

    private final String value;

    TypePaiement(String value) {
        this.value = value;
    }

    public static TypePaiement fromValue(String value) {
        if (value != null) {
            for (TypePaiement paymentMode : values()) {
                if (paymentMode.value.equals(value)) {
                    return paymentMode;
                }
            }
        }

        // you may return a default value
        return getDefault();
        // or throw an exception
        // throw new IllegalArgumentException("Invalid paymentMode: " + value);
    }

    public String toValue() {
        return value;
    }

    public static TypePaiement getDefault() {
        return Comptant;
    }
}
