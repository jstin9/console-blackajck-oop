package org.example.util;

import org.example.model.Player;

public class Messages {
    public static final String GREETING = "Добро пожаловать в Blackjack!";
    public static final String ENTER_NAME = "Введите ваше имя: ";
    public static final String ENTER_BALANCE = "Введите ваш стартовый баланс: ";
    public static final String ENTER_BALANCE_INCORRECT = "Пожалуйста, введите ваш стартовый баланс.";
    public static final String ENTER_BALANCE_ERROR = "Баланс должен быть положительным числом.";
    public static final String ENTER_BALANCE_EXCEPTION = "Пожалуйста введите корректное число.";
    public static final String NO_MONEY_GAME_OVER = "У вас закончились деньги. Игра окончена.";
    public static final String PLAY_AGAIN = "Хотите сыграть еще раз? (y/n): ";
    public static final String GOODBYE = "Спасибо за игру!";
    public static final String ENTER_BET = "Введите вашу ставку: ";
    public static final String ENTER_BET_INCORRECT = "Ставка не может быть пустой.";
    public static final String ENTER_BET_ERROR = "Пожалуйста введите корректную ставку.";
    public static final String INCORRECT_BET = "Неверная ставка!";
    public static final String BLACKJACK = "У вас Blackjack!";
    public static final String CHOICE = "Выберите действие:\n1. Hit\n2. Stand\n3. Double\n4. Split";
    public static final String CHOICE_EMPTY = "Введите номер действия.";
    public static final String CHOICE_INCORRECT = "Пожалуйста, введите число от 1 до 4.";
    public static final String CHOICE_EXCEPTION = "Пожалуйста укажите число для вашего действия.";
    public static final String CHOICE_SPLIT = "Выберите действие:\n1. Hit\n2. Stand";
    public static final String BUST = "Вы перебрали!";
    public static final String INCORRECT_INPUT = "Неверный ввод.";
    public static final String RESULT = "Результаты раунда:";
    public static final String BUST_BY_PLAYER = "Игрок перебрал. Побеждает дилер.";
    public static final String BUST_BY_DEALER = "Дилер перебрал. Побеждает игрок.";
    public static final String WIN_BY_PLAYER = "Игрок набрал больше очков. Побеждает игрок.";
    public static final String WIN_BY_DEALER = "Дилер набрал больше очков. Побеждает дилер.";
    public static final String PUSH = "Ничья. Ставка возвращена игроку.";
    public static final String PLAYER_HAND = "Ваши карты:";
    public static final String DEALER_HAND = "Карты дилера:";
    public static final String INSUFFICIENT_FUNDS = "Недостаточно средств.";
    public static final String BET_IS_PLACED = "Ставка сделана!";
    public static final String DEALER_TAKES_THE_CARD = "Дилер берет карту: ";
    public static final String PLAYER_CANT_SPLIT = "Вы не можете сплитовать сейчас!";
    public static final String SPLIT_FIRST_HAND = "Ваша первая рука: ";
    public static final String SPLIT_SECOND_HAND = "Ваша вторая рука: ";

    public static String dealerEndsTurn(int points) {
        return "Дилер завершает ход с " + points + " очками.";
    }

    public static String currentPoints(int points){
        return "Очки: " + points;
    }

    public static String getBalance(Player player){
        return "Ваш баланс: " + player.getBalance();
    }

    public static String newGame(Player player){
        return "Новая игра началась для игрока " + player.getName();
    }
}
