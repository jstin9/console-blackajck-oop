package org.example.util;

import org.example.model.Card;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static int calculateHandValue(ArrayList<Card> hand) {
        int sum = 0;
        int aceCount = 0;
        for (Card card : hand) {
            String value = card.getValue();
            if ("JQK".contains(value)) {
                sum += 10;
            } else if (value.equals("A")) {
                sum += 11;
                aceCount++;
            } else {
                sum += Integer.parseInt(value);
            }
        }

        while (sum > 21 && aceCount > 0) {
            sum -= 10;
            aceCount--;
        }

        return sum;
    }

    public static boolean isSplit(ArrayList<Card> hand){

        String firstValue = hand.get(0).getValue();
        String secondValue = hand.get(1).getValue();

        if(firstValue.equals(secondValue)){
            return true;
        }

        List<String> tens = List.of("10", "J", "Q", "K");
        return tens.contains(firstValue) && tens.contains(secondValue);

    }


    public static void printHand(ArrayList<Card> hand) {
        for (Card card : hand){
            System.out.println(card.getDisplay());
        }
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
