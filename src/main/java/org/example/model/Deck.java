package org.example.model;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private final String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private final String[] suits = {"♣", "♠", "♥", "♦"};
    private final ArrayList<Card> cards = new ArrayList<>();
    private final Random random = new Random();

    public Deck(){
        generateDeck();
        generateDeck();
        shuffle();
    }

    private void generateDeck(){
        for (String value : values){
            for (String suit : suits){
                cards.add(new Card(value, suit));
            }
        }
    }

    public void shuffle() {
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Card temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }

    public int remainingCards(){
        return cards.size();
    }

    public Card drawCard(){
        return cards.remove(0);
    }
}