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

    private Player player;
    private Deck deck;
    private final ConsoleInputHandler inputHandler;
    private final GameView view;
    private RoundManager roundManager;


    public BlackjackGame(ConsoleInputHandler inputHandler, ConsoleOutputHandler outputHandler) {
        this.inputHandler = inputHandler;
        this.view = new ConsoleUI(outputHandler);
    }

    public void start(){
        view.displayMessage(Messages.GREETING);
        Utils.sleep(500);

        String name = requestPlayerName();
        int balance = requestPlayerBalance();

        startNewGame(name, balance);

        playGameLoop();
    }

    public void startNewGame(String playerName, int startingBalance){
        this.player = new Player(playerName, startingBalance);
        Dealer dealer = new Dealer();
        this.deck = new Deck();

        view.updatePlayerInfo(player);
        view.updateDealerInfo(dealer);

        ResultProcessor resultProcessor = new ResultProcessor(player, dealer, view);

        this.roundManager = new RoundManager(view, inputHandler, resultProcessor, player, dealer, deck);

        view.displayMessage(Messages.newGame(player));
    }

    private void playGameLoop() {
        boolean continuePlaying = true;

        while(continuePlaying && player.getBalance() > 0){
            roundManager.playRound();

            if(player.getBalance() <= 0){
                view.displayMessage(Messages.NO_MONEY_GAME_OVER);
                Utils.sleep(500);

                break;
            }

            view.displayMessage(Messages.PLAY_AGAIN);
            Utils.sleep(500);

            String answer = inputHandler.readLine().toLowerCase();

            if(!answer.equals("y")){
                continuePlaying = false;
            } else {
                if(deck.remainingCards() < 17){
                    deck = new Deck();
                    view.displayMessage("-----The deck was shuffled-----");
                }
            }
        }
        view.displayMessage(Messages.GOODBYE);
    }

    private String requestPlayerName() {
        String name = "";
        boolean validName = false;
        while(!validName){
            view.displayMessage(Messages.ENTER_NAME);
            Utils.sleep(500);

            name = inputHandler.readLine();
            if(!name.isEmpty() && !name.isBlank()){
                validName = true;
            }
        }
        return name;
    }

    private int requestPlayerBalance() {
        int balance = 0;
        boolean validBalance = false;
        while(!validBalance){
            view.displayMessage(Messages.ENTER_BALANCE);
            Utils.sleep(500);

            String input = inputHandler.readLine();

            if(input.isEmpty()) {
                view.displayMessage(Messages.ENTER_BALANCE_INCORRECT);
                continue;
            }

            try {
                balance = Integer.parseInt(input);
                if(balance > 0){
                    validBalance = true;
                } else {
                    view.displayMessage(Messages.ENTER_BALANCE_ERROR);
                }
            } catch (NumberFormatException e){
                view.displayMessage(Messages.ENTER_BALANCE_EXCEPTION);
            }
        }
        return balance;
    }
}