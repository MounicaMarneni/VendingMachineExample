package com.collinson.vendingmachine.entity;

public enum Coin {
    PENNY("PENNY", 1), NICKLE("NICKLE", 5), DIME("DIME", 10), QUARTER("QUATER", 25);

    private int denomination;
    private String name;

    private Coin(String name, int denomination) {

        this.name = name;
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }

    public String getName() {
        return name;
    }

}
