package org.example.view;

import org.example.util.Messages;
import org.example.model.Dealer;
import org.example.model.Player;
import org.example.util.Utils;

public class ConsoleUI implements GameView{

    private  Player player;
    private Dealer dealer;
    private final ConsoleOutputHandler outputHandler;

    public ConsoleUI(ConsoleOutputHandler outputHandler) {
        this.outputHandler = outputHandler;
    }


    public void showTable() {
        if (player == null || dealer == null) return;

        outputHandler.println(Messages.PLAYER_HAND);
        Utils.printHand(player.getHand());
        outputHandler.println(Messages.currentPoints(player.calculateHandValue(player.getHand())));

        outputHandler.println(Messages.DEALER_HAND);
        Utils.printHand(dealer.getHand());
        outputHandler.println(Messages.currentPoints(dealer.calculateHandValue()));
    }

    @Override
    public void updatePlayerInfo(Player player) {
        this.player = player;
    }

    @Override
    public void updateDealerInfo(Dealer dealer) {
        this.dealer = dealer;
    }

    @Override
    public void displayMessage(String message) {
        outputHandler.println(message);
    }
}
