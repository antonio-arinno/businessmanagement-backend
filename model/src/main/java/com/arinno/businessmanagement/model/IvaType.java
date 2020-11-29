package com.arinno.businessmanagement.model;

public enum IvaType {

    GENERAL("General", 21),
    REDUCED("Reduced", 10),
    SUPER_REDUCED("Super Reduced", 4);

    private final String value;
    private final int iva;

    IvaType(String value, int iva) {
        this.value = value;
        this.iva = iva;
    }

    public static IvaType GENERAL() {
        return GENERAL;
    }

    public static IvaType REDUCED() {
        return REDUCED;
    }

    public static IvaType SUPER_REDUCED() {
        return SUPER_REDUCED;
    }

    public String getValue() {
        return value;
    }

    public int getIva() {
        return iva;
    }
}
