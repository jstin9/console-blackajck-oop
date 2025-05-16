package org.example.controller;

import org.example.view.ConsoleInputHandler;
import org.example.view.ConsoleOutputHandler;

public class GameInitializer {
    public void startGame(){
        ConsoleOutputHandler outputHandler = new ConsoleOutputHandler();
        ConsoleInputHandler inputHandler = new ConsoleInputHandler();
        BlackjackGame blackjackGame = new BlackjackGame(inputHandler, outputHandler);
        blackjackGame.start();
    }
}
