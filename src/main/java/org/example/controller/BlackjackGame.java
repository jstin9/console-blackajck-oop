package org.example.controller;

import org.example.util.Messages;
import org.example.model.Dealer;
import org.example.model.Deck;
import org.example.model.Player;
import org.example.util.Utils;
import org.example.view.ConsoleInputHandler;
import org.example.view.ConsoleOutputHandler;
import org.example.view.ConsoleUI;
import org.example.view.GameView;

public class BlackjackGame {
    private static final int DELAY = 500;

    private Player player;
    private Deck deck;
    private final ConsoleInputHandler inputHandler;
    private final GameView view;
    private RoundManager roundManager;
    private final MenuManager menuManager;

    public BlackjackGame(ConsoleInputHandler inputHandler, ConsoleOutputHandler outputHandler) {
        this.inputHandler = inputHandler;
        this.view = new ConsoleUI(outputHandler);
        this.menuManager = new MenuManager(inputHandler, view, this);
    }
    
    public void start() {
        menuManager.showMainMenu();
        view.displayMessage(Messages.GOODBYE);
    }

    public void startNewGame(String playerName, int startingBalance, int countDecks) {
        this.player = new Player(playerName, startingBalance);
        Dealer dealer = new Dealer();
        this.deck = new Deck(countDecks);

        view.updatePlayerInfo(player);
        view.updateDealerInfo(dealer);

        ResultProcessor resultProcessor = new ResultProcessor(player, dealer, view);

        this.roundManager = new RoundManager(view, inputHandler, resultProcessor, player, dealer, deck);

        view.displayMessage(Messages.newGame(player));
    }

    public void playGameLoop() {
        boolean continueGame = true;
        
        while(continueGame && player.getBalance() > 0) {
            roundManager.playRound();

            if(player.getBalance() <= 0) {
                view.displayMessage(Messages.NO_MONEY_GAME_OVER);
                Utils.sleep(DELAY);
                return;
            }
            
            continueGame = menuManager.showPlayAgainOrMainMenu();
        }
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Deck getDeck() {
        return deck;
    }
}