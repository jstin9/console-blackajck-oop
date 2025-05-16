package org.example.controller;

import org.example.util.Messages;
import org.example.model.Card;
import org.example.model.Dealer;
import org.example.model.Deck;
import org.example.model.Player;
import org.example.model.enums.WinType;
import org.example.util.Utils;
import org.example.view.ConsoleInputHandler;
import org.example.view.GameView;

import java.util.ArrayList;
import java.util.InputMismatchException;

public class RoundManager {

    private final ConsoleInputHandler inputHandler;
    private final ResultProcessor resultProcessor;
    private final GameView view;
    private final Player player;
    private final Dealer dealer;
    private final Deck deck;

    public RoundManager(GameView view,
                        ConsoleInputHandler inputHandler,
                        ResultProcessor resultProcessor,
                        Player player,
                        Dealer dealer,
                        Deck deck) {
        this.view = view;
        this.inputHandler = inputHandler;
        this.resultProcessor = resultProcessor;
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
    }

    public void playRound() {
        showBalanceAndPlaceBet();
        dealInitialCards();
        if(checkForBlackjack()){
            return;
        }
        playerTurn();
    }

    public void showBalanceAndPlaceBet() {
        view.displayMessage(Messages.getBalance(player));
        boolean validBet = false;

        while (!validBet) {
            view.displayMessage(Messages.ENTER_BET);
            String input = inputHandler.readLine();

            if (input.isEmpty()) {
                view.displayMessage(Messages.ENTER_BET_INCORRECT);
                continue;
            }

            try {
                int bet = Integer.parseInt(input);

                if (bet <= 0) {
                    view.displayMessage(Messages.INCORRECT_BET);
                    continue;
                }

                if (player.canPlaceBet(bet)) {
                    player.placeBet(bet);
                    view.displayMessage(Messages.BET_IS_PLACED);
                    validBet = true;
                } else {
                    view.displayMessage(Messages.INSUFFICIENT_FUNDS);
                }

            } catch (NumberFormatException e) {
                view.displayMessage(Messages.ENTER_BET_ERROR);
            }
        }
    }

    public void dealInitialCards(){
        player.clearHand();
        dealer.clearHand();

        player.receiveCard(deck.drawCard());
        player.receiveCard(deck.drawCard());
        dealer.receiveCard(deck.drawCard());
    }

    public boolean checkForBlackjack(){
        if(player.calculateHandValue(player.getHand()) == 21) {
            view.displayMessage(Messages.BLACKJACK);
            dealerPlayTurn();
            resultProcessor.determineWinner(WinType.BLACKJACK);
            return true;
        }
        return false;
    }

    public void playTurnForHand(ArrayList<Card> hand, String handName, boolean isSplit){

        boolean turnOver = false;

        while(!turnOver){
            view.displayMessage("");
            if(isSplit){
                view.displayMessage(handName + ":");
                Utils.printHand(hand);
                view.displayMessage(Messages.currentPoints(player.calculateHandValue(hand)));
                view.displayMessage(Messages.CHOICE_SPLIT);
            } else {
                view.showTable();
                view.displayMessage(Messages.CHOICE);
            }

            String input = inputHandler.readLine();

            if(input.isEmpty()){
                view.displayMessage(Messages.CHOICE_EMPTY);
                continue;
            }
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e){
                view.displayMessage(Messages.CHOICE_INCORRECT);
                continue;
            }
            try {
                switch (choice) {
                    case 1 -> { // Hit
                        hand.add(deck.drawCard());
                        if(!isSplit) resultProcessor.updatePoints();
                        if(player.calculateHandValue(player.getHand()) > 21){
                            view.displayMessage(Messages.BUST);
                            turnOver = true;
                            if(!isSplit) resultProcessor.determineWinner(WinType.REGULAR);
                        } else if (player.calculateHandValue(player.getHand()) == 21 && !isSplit) {
                            turnOver = true;
                            dealerPlayTurn();
                            resultProcessor.determineWinner(WinType.REGULAR);
                        }
                    }
                    case 2 -> { // Stand
                        turnOver = true;
                        if(!isSplit){
                            playerStand();
                        }
                    }
                    case 3 -> { // Double
                        if(!isSplit) {
                            playerDouble();
                            turnOver = true;
                        }
                    }
                    case 4 -> { // Split
                        if(!isSplit && Utils.isSplit(player.getHand()) && player.getBalance() >= player.getCurrentBet()) {
                            player.placeBet(player.getCurrentBet());
                            playerSplit();
                            turnOver = true;
                        } else {
                            view.displayMessage(Messages.PLAYER_CANT_SPLIT);
                        }
                    }
                    default -> view.displayMessage(Messages.INCORRECT_INPUT);
                }
            }catch (InputMismatchException e){
                view.displayMessage(Messages.CHOICE_EXCEPTION);
            }

        }
    }

    public void playerTurn(){
        playTurnForHand(player.getHand(), "", false);
    }

    public void playHand(ArrayList<Card> hand, String handName) {
        playTurnForHand(hand, handName, true);
    }

    public boolean placeBet(int amount){
        if(amount > 0) {
            return player.placeBet(amount);
        }
        return false;
    }

    public void dealerPlayTurn(){
        dealer.playTurn(deck);
    }

    public void playerHit(){
        player.receiveCard(deck.drawCard());
    }

    public void playerStand(){
        dealerPlayTurn();
        resultProcessor.determineResult();
    }

    public void playerDouble(){
        player.placeBet(player.getCurrentBet());
        playerHit();
        playerStand();
    }

    public void playerSplit(){
        ArrayList<Card> playerHand = player.getHand();
        ArrayList<Card> firstHand = new ArrayList<>();
        ArrayList<Card> secondHand = new ArrayList<>();
        String firstHandName = "Первая рука";
        String secondHandName = "Вторая рука";

        firstHand.add(playerHand.get(0));
        secondHand.add(playerHand.get(1));
        firstHand.add(deck.drawCard());
        secondHand.add(deck.drawCard());

        view.displayMessage(Messages.SPLIT_FIRST_HAND);
        Utils.printHand(firstHand);
        view.displayMessage(Messages.currentPoints(player.calculateHandValue(firstHand)));
        view.displayMessage(Messages.SPLIT_SECOND_HAND);
        Utils.printHand(secondHand);
        view.displayMessage(Messages.currentPoints(player.calculateHandValue(secondHand)));

        playHand(firstHand, firstHandName);
        playHand(secondHand, secondHandName);

        dealerPlayTurn();

        resultProcessor.determineSplitWinner(firstHand, firstHandName);
        resultProcessor.determineSplitWinner(secondHand, secondHandName);

        player.resetBet();
    }
}
