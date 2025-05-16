package org.example.view;

import org.example.model.Dealer;
import org.example.model.Player;

public interface GameView {
    void showTable();
    void updatePlayerInfo(Player player);
    void updateDealerInfo(Dealer dealer);
    void displayMessage(String message);
}
