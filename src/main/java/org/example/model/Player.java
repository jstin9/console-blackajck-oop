package org.example.model;

import org.example.util.Utils;

import java.util.ArrayList;

public class Player {
    private String name;
    private int balance;
    private final ArrayList<Card> hand;
    private int currentBet;



    public Player(String name, int balance) {
        this.name = name;
        this.balance = balance;
        this.hand = new ArrayList<>();
        this.currentBet = 0;
    }

    public boolean canPlaceBet(int amount) {
        return amount <= balance;
    }

    public void placeBet(int amount){
        if(amount > balance){
            return;
        }
        currentBet += amount;
        balance -= amount;
    }

    public int calculateHandValue(ArrayList<Card> hand) {
      return Utils.calculateHandValue(hand);
    }

    public void receiveCard(Card card){
        hand.add(card);
    }

    public void clearHand(){
        hand.clear();
    }

    public void addWinnings(int amount){
        balance += amount;
    }

    public void resetBet(){
        currentBet = 0;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setName(String name) {
        this.name = name;
    }
}
