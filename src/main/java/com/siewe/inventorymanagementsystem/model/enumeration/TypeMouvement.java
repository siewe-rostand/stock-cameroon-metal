package com.siewe.inventorymanagementsystem.model.enumeration;

public enum TypeMouvement {
    Entree("Entree"),
    Sortie("Sortie");

    private final String value;

    TypeMouvement(String value) {
        this.value = value;
    }

    public static TypeMouvement fromValue(String value) {
        if (value != null) {
            for (TypeMouvement typeMouvement : values()) {
                if (typeMouvement.value.equals(value)) {
                    return typeMouvement;
                }
            }
        }

        // you may return a default value
        return getDefault();
        // or throw an exception
        // throw new IllegalArgumentException("Invalid typeMouvement: " + value);
    }

    public String toValue() {
        return value;
    }

    public static TypeMouvement getDefault() {
        return Sortie;
    }
}
