package org.example.view;

import java.util.Scanner;

public class ConsoleInputHandler {
    Scanner scanner = new Scanner(System.in);

    public String readLine(){
        return scanner.nextLine().trim();
    }

    public int readIntInRange(int min, int max){
        while(true){
            try {
                int num = Integer.parseInt(readLine());
                if(num >= min && num <= max){
                    return num;
                }
                System.out.println("Enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }
}
