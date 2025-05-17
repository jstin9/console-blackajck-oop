package org.example.util;

import org.example.model.Player;

public class Messages {
    public static final String GREETING = "Welcome to Blackjack!";
    public static final String ENTER_NAME = "Enter your name: ";
    public static final String ENTER_BALANCE = "Enter your starting balance: ";
    public static final String ENTER_BALANCE_INCORRECT = "Please enter your starting balance.";
    public static final String ENTER_BALANCE_ERROR = "Balance must be a positive number.";
    public static final String ENTER_BALANCE_EXCEPTION = "Please enter a valid number.";
    public static final String NO_MONEY_GAME_OVER = "You're out of money. Game over.";
    public static final String PLAY_AGAIN = "Do you want to play again? (y/n): ";
    public static final String GOODBYE = "Thanks for playing!";
    public static final String ENTER_BET = "Enter your bet: ";
    public static final String ENTER_BET_INCORRECT = "Bet cannot be empty.";
    public static final String ENTER_BET_ERROR = "Please enter a valid bet.";
    public static final String INCORRECT_BET = "Invalid bet!";
    public static final String BLACKJACK = "You have Blackjack!";
    public static final String CHOICE = "Choose an action:\n1. Hit\n2. Stand\n3. Double\n4. Split";
    public static final String CHOICE_EMPTY = "Enter the action number.";
    public static final String CHOICE_INCORRECT = "Please enter a number between 1 and 4.";
    public static final String CHOICE_EXCEPTION = "Please enter a number for your action.";
    public static final String CHOICE_SPLIT = "Choose an action:\n1. Hit\n2. Stand";
    public static final String BUST = "You busted!";
    public static final String INCORRECT_INPUT = "Invalid input.";
    public static final String RESULT = "Round results:";
    public static final String BUST_BY_PLAYER = "Player busted. Dealer wins.";
    public static final String BUST_BY_DEALER = "Dealer busted. Player wins.";
    public static final String WIN_BY_PLAYER = "Player has more points. Player wins.";
    public static final String WIN_BY_DEALER = "Dealer has more points. Dealer wins.";
    public static final String PUSH = "Push. Bet is returned to the player.";
    public static final String PLAYER_HAND = "Your hand:";
    public static final String DEALER_HAND = "Dealer's hand:";
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds.";
    public static final String BET_IS_PLACED = "Bet is placed!";
    public static final String DEALER_TAKES_THE_CARD = "Dealer draws a card: ";
    public static final String PLAYER_CANT_SPLIT = "You can't split now!";
    public static final String SPLIT_FIRST_HAND = "Your first hand: ";
    public static final String SPLIT_SECOND_HAND = "Your second hand: ";

    public static String dealerEndsTurn(int points) {
        return "Dealer ends turn with " + points + " points.";
    }

    public static String currentPoints(int points){
        return "Points: " + points;
    }

    public static String getBalance(Player player){
        return "Your balance: " + player.getBalance();
    }

    public static String newGame(Player player){
        return "New game started for player " + player.getName();
    }
}

