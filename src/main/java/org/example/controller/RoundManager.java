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
    private static final int DELAY = 500;
    
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
        showPlayerBalance();
        placeBet();
        dealInitialCards();
        
        if(checkForBlackjack()) return;

        playerTurn();
    }

    private void showPlayerBalance() {
        displayMessageWithDelay(Messages.getBalance(player));
    }
    
    private void placeBet() {
        boolean validBet = false;

        while (!validBet) {
            displayMessageWithDelay(Messages.ENTER_BET);
            String input = inputHandler.readLine();

            if (input.isEmpty()) {
                displayMessageWithDelay(Messages.ENTER_BET_INCORRECT);
                continue;
            }

            validBet = processPlayerBet(input);
        }
    }
    
    private boolean processPlayerBet(String input) {
        try {
            int bet = Integer.parseInt(input);
            
            if (bet <= 0) {
                displayMessageWithDelay(Messages.INCORRECT_BET);
                return false;
            }

            if (player.canPlaceBet(bet)) {
                player.placeBet(bet);
                displayMessageWithDelay(Messages.BET_IS_PLACED);
                return true;
            } else {
                displayMessageWithDelay(Messages.INSUFFICIENT_FUNDS);
                return false;
            }
        } catch (NumberFormatException e) {
            displayMessageWithDelay(Messages.ENTER_BET_ERROR);
            return false;
        }
    }

    public void dealInitialCards() {
        player.clearHand();
        dealer.clearHand();

        player.receiveCard(deck.drawCard());
        player.receiveCard(deck.drawCard());
        dealer.receiveCard(deck.drawCard());
    }

    public boolean checkForBlackjack() {
        if(player.calculateHandValue(player.getHand()) == 21) {
            displayPlayerHandInfo();
            displayMessageWithDelay(Messages.BLACKJACK);
            dealerPlayTurn();
            resultProcessor.determineWinner(WinType.BLACKJACK);
            return true;
        }
        return false;
    }

    public void playerTurn() {
        playTurnForHand(player.getHand(), "", false);
    }
    
    public void playHand(ArrayList<Card> hand, String handName) {
        playTurnForHand(hand, handName, true);
    }
    
    public void playTurnForHand(ArrayList<Card> hand, String handName, boolean isSplit) {
        boolean turnOver = false;

        while(!turnOver) {
            displayHandInformation(hand, handName, isSplit);
            
            String input = inputHandler.readLine();
            
            if (input.isEmpty()) {
                displayMessageWithDelay(Messages.CHOICE_EMPTY);
                continue;
            }
            
            turnOver = processPlayerChoice(input, hand, isSplit);
        }
    }
    
    private void displayHandInformation(ArrayList<Card> hand, String handName, boolean isSplit) {
        view.displayMessage("");
        
        if (isSplit) {
            displaySplitHandInfo(hand, handName);
        } else {
            displayRegularHandInfo();
        }
    }
    
    private void displaySplitHandInfo(ArrayList<Card> hand, String handName) {
        displayMessageWithDelay(handName + ":");
        Utils.printHand(hand);
        displayMessageWithDelay(Messages.currentPoints(player.calculateHandValue(hand)));
        displayMessageWithDelay(Messages.CHOICE_SPLIT);
    }
    
    private void displayRegularHandInfo() {
        view.showTable();
        delay();
        displayMessageWithDelay(Messages.CHOICE);
    }
    
    private boolean processPlayerChoice(String input, ArrayList<Card> hand, boolean isSplit) {
        int choice;
        
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            displayMessageWithDelay(Messages.CHOICE_INCORRECT);
            return false;
        }
        
        try {
            return executePlayerAction(choice, hand, isSplit);
        } catch (InputMismatchException e) {
            displayMessageWithDelay(Messages.CHOICE_EXCEPTION);
            return false;
        }
    }
    
    private boolean executePlayerAction(int choice, ArrayList<Card> hand, boolean isSplit) {
        switch (choice) {
            case 1 -> { // Hit
                return handlePlayerHit(hand, isSplit);
            }
            case 2 -> { // Stand
                return handlePlayerStand(isSplit);
            }
            case 3 -> { // Double
                return handlePlayerDouble(isSplit);
            }
            case 4 -> { // Split
                return handlePlayerSplit(isSplit);
            }
            default -> {
                displayMessageWithDelay(Messages.INCORRECT_INPUT);
                return false;
            }
        }
    }
    
    private boolean handlePlayerHit(ArrayList<Card> hand, boolean isSplit) {
        hand.add(deck.drawCard());
        
        if (!isSplit) {
            resultProcessor.updatePoints();
        }
        
        int handValue = player.calculateHandValue(hand);
        
        if (handValue > 21) {
            displayMessageWithDelay(Messages.BUST);
            if (!isSplit) {
                resultProcessor.determineWinner(WinType.REGULAR);
            }
            return true;
        } else if (handValue == 21 && !isSplit) {
            displayPlayerHandInfo();
            dealerPlayTurn();
            resultProcessor.determineWinner(WinType.REGULAR);
            return true;
        }
        
        return false;
    }
    
    private boolean handlePlayerStand(boolean isSplit) {
        if (!isSplit) {
            playerStand();
        }
        return true;
    }
    
    private boolean handlePlayerDouble(boolean isSplit) {
        if (!isSplit) {
            playerDouble();
            return true;
        }
        return false;
    }
    
    private boolean handlePlayerSplit(boolean isSplit) {
        if (!isSplit && Utils.isSplit(player.getHand()) && player.getBalance() >= player.getCurrentBet()) {
            player.placeBet(player.getCurrentBet());
            playerSplit();
            return true;
        } else {
            displayMessageWithDelay(Messages.PLAYER_CANT_SPLIT);
            return false;
        }
    }

    public void playerHit() {
        player.receiveCard(deck.drawCard());
    }

    public void playerStand() {
        dealerPlayTurn();
        resultProcessor.determineResult();
    }

    public void playerDouble() {
        player.placeBet(player.getCurrentBet());
        playerHit();
        displayPlayerHandInfo();
        playerStand();
    }

    public void playerSplit() {
        ArrayList<Card> playerHand = player.getHand();
        ArrayList<Card> firstHand = createSplitHand(playerHand.get(0));
        ArrayList<Card> secondHand = createSplitHand(playerHand.get(1));
        
        displaySplitHandsInfo(firstHand, secondHand);
        
        processSplitHands(firstHand, secondHand);
    }
    
    private ArrayList<Card> createSplitHand(Card initialCard) {
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(initialCard);
        hand.add(deck.drawCard());
        return hand;
    }
    
    private void displaySplitHandsInfo(ArrayList<Card> firstHand, ArrayList<Card> secondHand) {
        displayMessageWithDelay(Messages.SPLIT_FIRST_HAND);
        Utils.printHand(firstHand);
        displayMessageWithDelay(Messages.currentPoints(player.calculateHandValue(firstHand)));
        
        displayMessageWithDelay(Messages.SPLIT_SECOND_HAND);
        Utils.printHand(secondHand);
        displayMessageWithDelay(Messages.currentPoints(player.calculateHandValue(secondHand)));
    }
    
    private void processSplitHands(ArrayList<Card> firstHand, ArrayList<Card> secondHand) {
        String firstHandName = "First hand";
        String secondHandName = "Second hand";
        
        boolean firstHandBusted = playHandAndCheckBust(firstHand, firstHandName);
        boolean secondHandBusted = playHandAndCheckBust(secondHand, secondHandName);
        
        if (!firstHandBusted || !secondHandBusted) {
            dealerPlayTurn();
        }
        
        determineSplitResults(firstHand, secondHand, firstHandName, secondHandName, firstHandBusted, secondHandBusted);
        
        player.resetBet();
    }
    
    private boolean playHandAndCheckBust(ArrayList<Card> hand, String handName) {
        playHand(hand, handName);
        return player.calculateHandValue(hand) > 21;
    }
    
    private void determineSplitResults(ArrayList<Card> firstHand, ArrayList<Card> secondHand, 
                                    String firstHandName, String secondHandName,
                                    boolean firstHandBusted, boolean secondHandBusted) {
        if (firstHandBusted) {
            displayMessageWithDelay(firstHandName + ": " + Messages.BUST);
        } else {
            resultProcessor.determineSplitWinner(firstHand, firstHandName);
        }
        
        if (secondHandBusted) {
            displayMessageWithDelay(secondHandName + ": " + Messages.BUST);
        } else {
            resultProcessor.determineSplitWinner(secondHand, secondHandName);
        }
    }

    public void dealerPlayTurn() {
        displayDealerHandInfo();
        playDealerCards();
    }
    
    private void displayDealerHandInfo() {
        displayMessageWithDelay(Messages.DEALER_HAND);
        Utils.printHand(dealer.getHand());
        displayMessageWithDelay(Messages.currentPoints(dealer.calculateHandValue()));
    }

    private void displayPlayerHandInfo() {
        displayMessageWithDelay(Messages.PLAYER_HAND);
        Utils.printHand(player.getHand());
        displayMessageWithDelay(Messages.currentPoints(player.calculateHandValue(player.getHand())));
    }
    
    private void playDealerCards() {
        while(Utils.calculateHandValue(dealer.getHand()) < 17) {
            Card card = deck.drawCard();
            dealer.receiveCard(card);
            System.out.println(Messages.DEALER_TAKES_THE_CARD + card.getDisplay());
            delay();
        }
        
        System.out.println(Messages.dealerEndsTurn(Utils.calculateHandValue(dealer.getHand())));
        delay();
    }
    
    private void displayMessageWithDelay(String message) {
        view.displayMessage(message);
        delay();
    }
    
    private void delay() {
        Utils.sleep(DELAY);
    }
}
