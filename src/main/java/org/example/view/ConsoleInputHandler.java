package org.example.view;

import java.util.Scanner;

public class ConsoleInputHandler {
    Scanner scanner = new Scanner(System.in);

    public String readLine(){
        return scanner.nextLine().trim();
    }
}
