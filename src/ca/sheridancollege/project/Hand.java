package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 *
 * @author Steve
 */

 // shows the hand of cards for the player and or dealer
 // handles the specific hand calculations and logic for blackjack
public class Hand {


    private ArrayList<Card> cards; // cards in the hand

    // creating a new empty hand
    public Hand() {
        this.cards = new ArrayList<>();
    }

    // adds a card to this hand
    public void addCard(Card card) {
        cards.add(card);
    }

    // returns all the card values/types in this hand
    public ArrayList<Card> getCards() {
        return cards;
    }

    // removes all cards from this hand
    public void clear() {
        cards.clear();
    }

    // calculates the blackjack score of this hand
    // handles aces as 1 or 11 to avoid creating a bust (failed hand)
    public int calculateScore() {
        int score = 0;
        int aceCount = 0;

        // adds up all of the card values
        for (Card card : cards) {
            if (card instanceof PlayingCard) {
                PlayingCard playingCard = (PlayingCard) card;
                if (playingCard.getValue() == PlayingCard.Value.ACE) {
                    aceCount++;
                }
                score += playingCard.getPointValue();
            }
        }

        // Adjust for aces if needed
        while (score > 21 && aceCount > 0) {
            score -= 10; // Convert Ace from 11 to 1
            aceCount--;
        }

        return score;
    }

    // checks if the hand is a blackjack (21 with 2 cards)
    public boolean isBlackjack() {
        return cards.size() == 2 && calculateScore() == 21;
    }

    // checks if the hand is over 21 (busted)
    public boolean isBusted() {
        return calculateScore() > 21;
    }

    // string output of the current hand with the point value of the hand
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card).append(", ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2); // Remove the last ", "
        }
        sb.append(" (").append(calculateScore()).append(" points)");
        return sb.toString();
    }
}
