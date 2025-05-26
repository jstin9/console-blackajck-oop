package org.example.controller;

import org.example.model.Deck;
import org.example.model.Player;
import org.example.util.GameStateManager;
import org.example.util.Messages;
import org.example.util.Utils;
import org.example.view.ConsoleInputHandler;
import org.example.view.GameView;

import java.util.List;

public class MenuManager {
    private static final int DELAY = 500;
    
    private final ConsoleInputHandler inputHandler;
    private final GameView view;
    private final BlackjackGame game;
    
    public MenuManager(ConsoleInputHandler inputHandler, GameView view, BlackjackGame game) {
        this.inputHandler = inputHandler;
        this.view = view;
        this.game = game;
    }
    
    public void showMainMenu() {
        view.displayMessage(Messages.GREETING);
        Utils.sleep(DELAY);

        boolean exit = false;
        
        while(!exit) {
            view.displayMessage(Messages.CHOICE_MAIN_MENU);
            Utils.sleep(DELAY);
            int input = inputHandler.readIntInRange(1, 5);
            
            switch (input) {
                case 1 -> setupAndStartGame();
                case 2 -> loadGame();
                case 3 -> openSettings();
                case 4 -> saveGame();
                case 5 -> exit = true;
                default -> view.displayMessage(Messages.INCORRECT_INPUT);
            }
        }
    }
    
    private void setupAndStartGame() {
        String name = requestPlayerName();
        int balance = requestPlayerBalance();
        int countDecks = requestDeckCount();
        
        game.startNewGame(name, balance, countDecks);
        game.playGameLoop();
    }
    
    private void loadGame() {
        List<String> savedPlayerNames = GameStateManager.getSavedPlayerNames();
        
        if (savedPlayerNames.isEmpty()) {
            view.displayMessage(Messages.NO_SAVED_GAMES);
            Utils.sleep(DELAY);
            return;
        }
        
        view.displayMessage(Messages.SELECT_SAVED_GAME);
        for (int i = 0; i < savedPlayerNames.size(); i++) {
            view.displayMessage((i + 1) + ". " + savedPlayerNames.get(i));
        }
        view.displayMessage((savedPlayerNames.size() + 1) + ". " + Messages.BACK_TO_MAIN_MENU);
        
        int selection = inputHandler.readIntInRange(1, savedPlayerNames.size() + 1);
        
        if (selection == savedPlayerNames.size() + 1) {
            return;
        }
        
        String playerName = savedPlayerNames.get(selection - 1);
        Player loadedPlayer = GameStateManager.loadPlayer(playerName);
        
        if (loadedPlayer != null) {
            int countDecks = requestDeckCount();
            game.startNewGame(loadedPlayer.getName(), loadedPlayer.getBalance(), countDecks);
            view.displayMessage(Messages.GAME_LOADED + loadedPlayer.getName() + Messages.WITH_BALANCE + loadedPlayer.getBalance());
            Utils.sleep(DELAY);
            game.playGameLoop();
        } else {
            view.displayMessage(Messages.ERROR_LOADING_GAME);
            Utils.sleep(DELAY);
        }
    }
    
    private void saveGame() {
        Player player = game.getPlayer();
        if (player == null) {
            view.displayMessage(Messages.NO_ACTIVE_PLAYER);
            return;
        }
        
        boolean success = GameStateManager.savePlayer(player);
        if (success) {
            view.displayMessage(Messages.GAME_SAVED_SUCCESS + player.getName());
        } else {
            view.displayMessage(Messages.ERROR_SAVING_GAME);
        }
        Utils.sleep(DELAY);
    }
    
    private void openSettings() {
        boolean exit = false;
        while(!exit) {
            view.displayMessage(Messages.CHOICE_SETTINGS);
            Utils.sleep(DELAY);
            int input = inputHandler.readIntInRange(1, 4);
            
            switch (input) {
                case 1 -> changePlayerName();
                case 2 -> changeStartingBalance();
                case 3 -> changeDeckCount();
                case 4 -> exit = true;
                default -> view.displayMessage(Messages.INCORRECT_INPUT);
            }
        }
    }
    
    private void changePlayerName() {
        Player player = game.getPlayer();
        if (player == null) {
            view.displayMessage(Messages.NO_ACTIVE_PLAYER);
            return;
        }
        view.displayMessage(Messages.CURRENT_PLAYER_NAME + player.getName());
        String newName = requestPlayerName();
        player.setName(newName);
        view.updatePlayerInfo(player);
        view.displayMessage(Messages.PLAYER_NAME_CHANGED + newName);
        Utils.sleep(DELAY);
    }
    
    private void changeStartingBalance() {
        Player player = game.getPlayer();
        if (player == null) {
            view.displayMessage(Messages.NO_ACTIVE_PLAYER);
            return;
        }
        view.displayMessage(Messages.CURRENT_PLAYER_BALANCE + player.getBalance());
        int newBalance = requestPlayerBalance();
        player.setBalance(newBalance);
        view.updatePlayerInfo(player);
        view.displayMessage(Messages.PLAYER_BALANCE_CHANGED + newBalance);
        Utils.sleep(DELAY);
    }
    
    private void changeDeckCount() {
        Deck deck = game.getDeck();
        if (deck == null) {
            view.displayMessage(Messages.NO_ACTIVE_DECK);
            return;
        }
        view.displayMessage(Messages.CURRENT_DECK_COUNT + deck.getCountDecks());
        int newDeckCount = requestDeckCount();
        deck.setCountDecks(newDeckCount);
        deck.reshuffle();
        view.displayMessage(Messages.DECK_SHUFFLED);
        view.displayMessage(Messages.DECK_COUNT_CHANGED + newDeckCount);
        Utils.sleep(DELAY);
    }
    
    private String requestPlayerName() {
        String name = "";
        boolean validName = false;
        while(!validName) {
            view.displayMessage(Messages.ENTER_NAME);
            Utils.sleep(DELAY);
            
            name = inputHandler.readLine();
            if(!name.isEmpty() && !name.isBlank()) {
                validName = true;
            }
        }
        return name;
    }
    
    private int requestPlayerBalance() {
        int balance = 0;
        boolean validBalance = false;
        while(!validBalance) {
            view.displayMessage(Messages.ENTER_BALANCE);
            Utils.sleep(DELAY);
            
            String input = inputHandler.readLine();
            
            if(input.isEmpty()) {
                view.displayMessage(Messages.ENTER_BALANCE_INCORRECT);
                continue;
            }
            
            try {
                balance = Integer.parseInt(input);
                if(balance > 0) {
                    validBalance = true;
                } else {
                    view.displayMessage(Messages.ENTER_BALANCE_ERROR);
                }
            } catch (NumberFormatException e) {
                view.displayMessage(Messages.ENTER_BALANCE_EXCEPTION);
            }
        }
        return balance;
    }
    
    private int requestDeckCount() {
        int deckCount = 0;
        boolean validCount = false;
        while(!validCount) {
            view.displayMessage(Messages.ENTER_DECK_COUNT);
            Utils.sleep(DELAY);
            
            String input = inputHandler.readLine();
            
            if(input.isEmpty()) {
                view.displayMessage(Messages.DECK_COUNT_INPUT_ERROR);
                continue;
            }
            
            try {
                deckCount = Integer.parseInt(input);
                if(deckCount >= 1 && deckCount <= 8) {
                    validCount = true;
                } else {
                    view.displayMessage(Messages.DECK_COUNT_RANGE_ERROR);
                }
            } catch (NumberFormatException e) {
                view.displayMessage(Messages.ENTER_BALANCE_EXCEPTION);
            }
        }
        return deckCount;
    }

    public boolean showPlayAgainOrMainMenu() {
        view.displayMessage(Messages.PLAY_AGAIN_OR_BACK_TO_MENU);
        Utils.sleep(DELAY);
        
        int input = inputHandler.readIntInRange(1, 2);
        
        switch (input) {
            case 1 -> {
                if(game.getDeck().remainingCards() < game.getDeck().getCountDecks() * 52 * 0.25) {
                    game.getDeck().reshuffle();
                    view.displayMessage(Messages.DECK_SHUFFLED);
                }
                return true;
            }
            case 2 -> {
                return false;
            }
            default -> {
                view.displayMessage(Messages.INCORRECT_INPUT);
                return false;
            }
        }
    }
}
