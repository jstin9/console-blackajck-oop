package org.example.model;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private final String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private final String[] suits = {"♣", "♠", "♥", "♦"};
    private final ArrayList<Card> cards = new ArrayList<>();
    private final Random random = new Random();
    private int countDecks;

    public Deck(int countDecks){
        this.countDecks = countDecks;
        generateDeck(countDecks);
        shuffle();
    }


    private void generateDeck(int countDecks){
        for (int i = 0; i < countDecks; i++){
            for (String value : values){
                for (String suit : suits){
                    cards.add(new Card(value, suit));
                }
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

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void reshuffle() {
        generateDeck(countDecks);
        shuffle();
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty. Please reshuffle.");
        }
        return cards.remove(0);
    }

    public int getCountDecks() {
        return countDecks;
    }

    public void setCountDecks(int countDecks) {
        this.countDecks = countDecks;
    }
}