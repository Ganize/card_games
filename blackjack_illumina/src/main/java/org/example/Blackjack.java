package org.example;

import java.util.Scanner;

public class Blackjack {

    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;
    private Scanner scanner;
    private int playerPoints;

    public Blackjack() {
        deck = new Deck();
        scanner = new Scanner(System.in);
    }

    private void initializeRound() {
        deck.shuffle();
        playerHand = new Hand();
        dealerHand = new Hand();
    }

    private void checkDeckReshuffle() {
        if (deck.cardsRemaining() <= 15) {
            deck = new Deck();
            deck.shuffle();
            System.out.println("Deck reshuffled.");
        }
    }

    private void dealInitialCards() {
        dealCard(playerHand, false);
        dealCard(playerHand, false);
        dealCard(dealerHand, false);
        dealCard(dealerHand, false);

    }

    private boolean playPlayerTurn() {
        boolean playerBusted = false;
        System.out.println("Initialise hand: " + playerHand.toString());
        System.out.println("Initialise hand value: " + playerHand.getValue());

        while (true) {

            System.out.println("Hit or Stay? (Enter H or S): ");
            String playerChoice = scanner.nextLine();

            if (playerChoice.equalsIgnoreCase("H")) {
                dealCard(playerHand, true);

                System.out.println("Your updated hand: " + playerHand.toString());
                System.out.println("Your updated hand value: " + playerHand.getValue());

                if (playerHand.getValue() > 21) {
                    playerBusted = true;
                    playerPoints -= 10;
                    System.out.println("You busted! You lose 10 points.");
                    break;
                }
            } else if (playerChoice.equalsIgnoreCase("S")) {
                break;
            }
        }
        return playerBusted;
    }

    private void playDealerTurn() {
        System.out.println("Dealer's revealed hand: " + dealerHand);
        while (dealerHand.getValue() < 17) {
            dealCard(dealerHand, true);
            if (dealerHand.getValue() > 21) {
                System.out.println("Dealer busts! Dealer's hand value: " + dealerHand.getValue());
                return;
            }
        }
        System.out.println("Updated Dealer's hand: " + dealerHand);
        System.out.println("Dealer's hand value: " + dealerHand.getValue() +
                " vs Player's hand value: " + playerHand.getValue());
    }

    private void updatePoints(boolean playerBusted) {
        if (playerBusted) {
            return;
        }

        int playerScore = playerHand.getValue();
        int dealerScore = dealerHand.getValue();
        boolean playerGotBlackjack = (playerScore == 21) && (playerHand.getCardCount() == 2);

        if (playerGotBlackjack && dealerScore != 21) {
            playerPoints += 15;
            System.out.println("Blackjack! You earn 15 points.");
        } else if (dealerScore > 21 || playerScore > dealerScore) {
            playerPoints += 10; // Player wins
            System.out.println("You beat the dealer! You earn 10 points.");
        } else if (playerScore < dealerScore) {
            // Dealer wins, no points awarded or deducted
            System.out.println("Dealer wins!");
        } else {
            // Push, no points added or subtracted
            System.out.println("It's a push!");
        }
    }



    public void play() {
        while (true) {
            initializeRound();
            checkDeckReshuffle();
            dealInitialCards();

            boolean playerBusted = playPlayerTurn();
            if (!playerBusted) {
                playDealerTurn();
            }

            updatePoints(playerBusted);
            System.out.println("Your total points: " + playerPoints);

            if (!askToPlayAgain()) {
                System.out.println("Thank you for playing! Your total points: " + playerPoints);
                break;
            }
        }
    }

    private boolean askToPlayAgain() {
        System.out.println("Play another round? (Y/N): ");
        String playAgain = scanner.nextLine().trim();
        return playAgain.equalsIgnoreCase("Y");
    }

    private void dealCard(Hand hand, boolean displayInfo) {
        if (deck.cardsRemaining() <= 15) {
            deck = new Deck();
            deck.shuffle();
            System.out.println("Deck reshuffled.");
        }

        Card newCard = deck.deal();
        hand.addCard(newCard);

        if (displayInfo) {
            System.out.println("==================================================");
            System.out.println("Cards remaining in deck: " + deck.cardsRemaining());
            System.out.println("==================================================");
        }
    }


    public static void main(String[] args) {
        Blackjack game = new Blackjack();
        game.play();
    }
}
