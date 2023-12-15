package org.example;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        if (card != null) {
            cards.add(card);
        }
    }

    public int getCardCount() {
        return cards.size();
    }

    public int getValue() {
        int value = 0;
        int aces = 0;

        for (Card card : cards) {
            if (card.getValue().equalsIgnoreCase("Ace")) {
                aces++;
                value += 11;
            } else if (card.getValue().equalsIgnoreCase("Jack") || card.getValue().equalsIgnoreCase("Queen") || card.getValue().equalsIgnoreCase("King")) {
                value += 10;
            } else {
                value += Integer.parseInt(card.getValue());
            }
        }

        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }

        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) { // Assuming 'cards' is the collection of Card objects in Hand
            sb.append(card.toString()).append(" "); // Assuming Card class has a meaningful toString()
        }
        return sb.toString().trim();
    }
}
