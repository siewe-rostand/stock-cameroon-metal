package com.siewe.inventorymanagementsystem.model.enumeration;

public enum TypeClient {
    Entreprise("Entreprise"),
    Individuel("Individuel");

    private final String value;

    TypeClient(String value) {
        this.value = value;
    }

    public static TypeClient fromValue(String value) {
        if (value != null) {
            for (TypeClient paymentMode : values()) {
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

    public static TypeClient getDefault() {
        return Individuel;
    }
}
