package org.example.model;

import org.example.util.Utils;

import java.util.ArrayList;

public class Dealer {
    private final ArrayList<Card> hand;

    public Dealer() {
        this.hand = new ArrayList<>();
    }

    public int calculateHandValue() {
        return Utils.calculateHandValue(hand);
    }

    public void receiveCard(Card card){
        hand.add(card);
    }

    public void clearHand(){
        hand.clear();
    }

    public ArrayList<Card> getHand() {
        return hand;
    }
}
