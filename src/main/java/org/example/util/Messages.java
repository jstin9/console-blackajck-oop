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
    public static final String ENTER_DECK_COUNT = "Enter count of decks (1-8): ";
    public static final String DECK_SHUFFLED = "-----The deck was shuffled-----";
    public static final String CHOICE_MAIN_MENU = "Choose an action:\n1. New game\n2. Load game\n3. Settings\n4. Save game\n5. Exit";
    public static final String PLAY_AGAIN_OR_BACK_TO_MENU = "1. Play again\n2. Return to main menu";
    public static final String CHOICE_SETTINGS = "1. Change name\n2. Change balance\n3. Change deck count\n4. Return to main menu";
    
    // from MenuManager
    public static final String NO_SAVED_GAMES = "No saved games found.";
    public static final String SELECT_SAVED_GAME = "Select a saved game to load:";
    public static final String BACK_TO_MAIN_MENU = "Back to main menu";
    public static final String NO_ACTIVE_PLAYER = "No active player. Please start a new game first.";
    public static final String NO_ACTIVE_DECK = "No active deck. Please start a new game first.";
    public static final String ERROR_LOADING_GAME = "Error loading game. The save file might be corrupted.";
    public static final String GAME_SAVED_SUCCESS = "Game saved successfully for player ";
    public static final String ERROR_SAVING_GAME = "Error saving the game.";
    public static final String CURRENT_PLAYER_NAME = "Current player name: ";
    public static final String PLAYER_NAME_CHANGED = "Player name has been changed to: ";
    public static final String CURRENT_PLAYER_BALANCE = "Current player balance: ";
    public static final String PLAYER_BALANCE_CHANGED = "Player balance has been changed to: ";
    public static final String CURRENT_DECK_COUNT = "Current deck count: ";
    public static final String DECK_COUNT_CHANGED = "Deck count has been changed to: ";
    public static final String DECK_COUNT_RANGE_ERROR = "Number of decks must be between 1 and 8.";
    public static final String DECK_COUNT_INPUT_ERROR = "Please enter a number between 1 and 8.";
    public static final String GAME_LOADED = "Game loaded for player ";
    public static final String WITH_BALANCE = " with balance ";
    
    // from GameStateManager
    public static final String ERROR_CREATING_SAVE_DIR = "Error creating save directory: ";
    public static final String ERROR_SAVING_PLAYER = "Error saving player: ";
    public static final String ERROR_LOADING_PLAYER = "Error loading player: ";

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
