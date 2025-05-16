package org.example.controller;

import org.example.util.Messages;
import org.example.model.Card;
import org.example.model.Dealer;
import org.example.model.Player;
import org.example.model.enums.WinType;
import org.example.util.Utils;
import org.example.view.GameView;

import java.util.ArrayList;

public class ResultProcessor {

    private final Player player;
    private final Dealer dealer;
    private final GameView view;
    private int playerPoints;
    private int dealerPoints;

    public ResultProcessor(Player player, Dealer dealer, GameView view) {
        this.player = player;
        this.dealer = dealer;
        this.view = view;
    }

    public void determineWinner(WinType winType){
        view.displayMessage(Messages.RESULT);

        updatePoints();
        view.showTable();

        switch (winType){
            case BLACKJACK -> {
                view.displayMessage(Messages.BLACKJACK);
                player.addWinnings((int) (player.getCurrentBet() * 2.5));
            }
            case REGULAR -> {
                if (playerPoints > 21){
                    view.displayMessage(Messages.BUST_BY_PLAYER);
                } else if (dealerPoints > 21) {
                    view.displayMessage(Messages.BUST_BY_DEALER);
                    player.addWinnings(player.getCurrentBet() * 2);
                } else if (playerPoints > dealerPoints) {
                    view.displayMessage(Messages.WIN_BY_PLAYER);
                    player.addWinnings(player.getCurrentBet() * 2);
                } else if (dealerPoints > playerPoints) {
                    view.displayMessage(Messages.WIN_BY_DEALER);
                }
            }
            case PUSH -> {
                view.displayMessage(Messages.PUSH);
                player.addWinnings(player.getCurrentBet());
            }
        }
        player.resetBet();
    }

    public void determineSplitWinner(ArrayList<Card> hand, String handName) {
        view.displayMessage(Messages.RESULT);
        int playerPoints = player.calculateHandValue(hand);
        int dealerPoints = dealer.calculateHandValue();

        view.displayMessage(handName + ":");
        Utils.printHand(hand);
        view.displayMessage(Messages.currentPoints(playerPoints));

        if(playerPoints > 21){
            view.displayMessage(Messages.BUST_BY_PLAYER);
        } else if (dealerPoints > 21) {
            view.displayMessage(Messages.BUST_BY_DEALER);
            player.addWinnings(player.getCurrentBet() * 2);
        } else if (playerPoints > dealerPoints) {
            view.displayMessage(Messages.WIN_BY_PLAYER);
            player.addWinnings(player.getCurrentBet() * 2);
        } else if (dealerPoints > playerPoints) {
            view.displayMessage(Messages.WIN_BY_DEALER);
        } else {
            view.displayMessage(Messages.PUSH);
            player.addWinnings(player.getCurrentBet());
        }

    }

    public void updatePoints() {
        playerPoints = player.calculateHandValue(player.getHand());
        dealerPoints = dealer.calculateHandValue();
    }

    public void determineResult() {
        updatePoints();

        if (playerPoints == dealerPoints) {
            determineWinner(WinType.PUSH);
        } else {
            determineWinner(WinType.REGULAR);
        }
    }
}
