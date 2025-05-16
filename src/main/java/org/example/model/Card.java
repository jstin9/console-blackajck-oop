package org.example.model;

public class Card {
    private String value;
    private String suit;

    public Card(String value, String suit){
        this.value = value;
        this.suit = suit;
    }

    public String getDisplay(){
        return value + suit;
    }

    public String getValue() {
        return value;
    }
}
