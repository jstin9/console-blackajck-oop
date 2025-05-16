package org.example.model;

import org.example.util.Messages;
import org.example.util.Utils;

import java.util.ArrayList;

public class Dealer {
    private ArrayList<Card> hand;

    public Dealer() {
        this.hand = new ArrayList<>();
    }

    public void playTurn(Deck deck){
        while(calculateHandValue() < 17){
            Card card = deck.drawCard();
            receiveCard(card);
            System.out.println(Messages.DEALER_TAKES_THE_CARD + card.getDisplay());
        }
        System.out.println(Messages.dealerEndsTurn(calculateHandValue()));
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
