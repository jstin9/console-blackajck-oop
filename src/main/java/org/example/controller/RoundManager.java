package org.example.controller;

import org.example.util.Messages;
import org.example.model.Card;
import org.example.model.Dealer;
import org.example.model.Deck;
import org.example.model.Player;
import org.example.util.Utils;
import org.example.model.enums.WinType;
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
        Utils.sleep(500);

        boolean validBet = false;

        while (!validBet) {
            view.displayMessage(Messages.ENTER_BET);
            Utils.sleep(500);

            String input = inputHandler.readLine();

            if (input.isEmpty()) {
                view.displayMessage(Messages.ENTER_BET_INCORRECT);
                Utils.sleep(500);

                continue;
            }

            try {
                int bet = Integer.parseInt(input);

                if (bet <= 0) {
                    view.displayMessage(Messages.INCORRECT_BET);
                    Utils.sleep(500);

                    continue;
                }

                if (player.canPlaceBet(bet)) {
                    player.placeBet(bet);
                    view.displayMessage(Messages.BET_IS_PLACED);
                    Utils.sleep(500);

                    validBet = true;
                } else {
                    view.displayMessage(Messages.INSUFFICIENT_FUNDS);
                    Utils.sleep(500);

                }

            } catch (NumberFormatException e) {
                view.displayMessage(Messages.ENTER_BET_ERROR);
                Utils.sleep(500);

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
            Utils.sleep(500);

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
                Utils.sleep(500);

                Utils.printHand(hand);
                view.displayMessage(Messages.currentPoints(player.calculateHandValue(hand)));
                Utils.sleep(500);

                view.displayMessage(Messages.CHOICE_SPLIT);
                Utils.sleep(500);

            } else {
                view.showTable();
                Utils.sleep(500);

                view.displayMessage(Messages.CHOICE);
                Utils.sleep(500);

            }

            String input = inputHandler.readLine();

            if(input.isEmpty()){
                view.displayMessage(Messages.CHOICE_EMPTY);
                Utils.sleep(500);

                continue;
            }
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e){
                view.displayMessage(Messages.CHOICE_INCORRECT);
                Utils.sleep(500);

                continue;
            }
            try {
                switch (choice) {
                    case 1 -> { // Hit
                        hand.add(deck.drawCard());
                        if(!isSplit) resultProcessor.updatePoints();
                        if(player.calculateHandValue(player.getHand()) > 21){
                            view.displayMessage(Messages.BUST);
                            Utils.sleep(500);

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
                            Utils.sleep(500);

                        }
                    }
                    default ->{
                        view.displayMessage(Messages.INCORRECT_INPUT);
                        Utils.sleep(500);
                    }

                }
            }catch (InputMismatchException e){
                view.displayMessage(Messages.CHOICE_EXCEPTION);
                Utils.sleep(500);

            }

        }
    }

    public void playerTurn(){
        playTurnForHand(player.getHand(), "", false);
    }

    public void playHand(ArrayList<Card> hand, String handName) {
        playTurnForHand(hand, handName, true);
    }

    public void dealerPlayTurn(){
        view.displayMessage(Messages.DEALER_HAND);
        Utils.sleep(500);
        Utils.printHand(dealer.getHand());
        view.displayMessage(Messages.currentPoints(dealer.calculateHandValue()));
        Utils.sleep(500);

        while(Utils.calculateHandValue(dealer.getHand()) < 17){
            Card card = deck.drawCard();
            dealer.receiveCard(card);
            System.out.println(Messages.DEALER_TAKES_THE_CARD + card.getDisplay());
            Utils.sleep(500);

        }
        System.out.println(Messages.dealerEndsTurn(Utils.calculateHandValue(dealer.getHand())));
        Utils.sleep(500);

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
        String firstHandName = "First hand";
        String secondHandName = "Second hand";

        firstHand.add(playerHand.get(0));
        secondHand.add(playerHand.get(1));
        firstHand.add(deck.drawCard());
        secondHand.add(deck.drawCard());

        view.displayMessage(Messages.SPLIT_FIRST_HAND);
        Utils.sleep(500);

        Utils.printHand(firstHand);
        view.displayMessage(Messages.currentPoints(player.calculateHandValue(firstHand)));
        Utils.sleep(500);

        view.displayMessage(Messages.SPLIT_SECOND_HAND);
        Utils.sleep(500);

        Utils.printHand(secondHand);
        view.displayMessage(Messages.currentPoints(player.calculateHandValue(secondHand)));
        Utils.sleep(500);


        playHand(firstHand, firstHandName);
        playHand(secondHand, secondHandName);

        dealerPlayTurn();

        resultProcessor.determineSplitWinner(firstHand, firstHandName);
        resultProcessor.determineSplitWinner(secondHand, secondHandName);

        player.resetBet();
    }
}
