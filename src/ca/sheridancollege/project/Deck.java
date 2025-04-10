package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 *
 * @author Steve
 */

// represents a standard deck of 52 cards
// handles the creation, shuffling, and dealing of the deck
// extend groupOfCards to create a deck
public class Deck extends GroupOfCards {


    // creating a new deck
    public Deck() {
        super(52); // Standard 52-card deck
        ArrayList<Card> cards = new ArrayList<>();

        // Create all combinations of suits and values
        for (PlayingCard.Suit suit : PlayingCard.Suit.values()) {
            for (PlayingCard.Value value : PlayingCard.Value.values()) {
                cards.add(new PlayingCard(suit, value));
            }
        }

        setCards(cards);
        shuffle(); // Shuffle the deck when created
    }

    public void setCards(ArrayList<Card> cards) {
        super.setCards(cards);
    }

    // deal a card from the top of the deck
    // return the card from the top, null if the deck is empty
    public Card dealCard() {
        if (getCards().isEmpty()) {
            return null; // Or throw an exception
        }
        return getCards().remove(0); // Remove and return the top card
    }


    // resets the deck to a full 52 card and shuffles the deck
    // used when deck is starting to run low on cards
    public void reset() {
        // Recreate and shuffle the deck
        ArrayList<Card> cards = new ArrayList<>();

        for (PlayingCard.Suit suit : PlayingCard.Suit.values()) {
            for (PlayingCard.Value value : PlayingCard.Value.values()) {
                cards.add(new PlayingCard(suit, value));
            }
        }

        setCards(cards);
        shuffle();
    }
}