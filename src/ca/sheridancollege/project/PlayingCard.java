package ca.sheridancollege.project;

/**
 *
 * @author Steve
 */
// New playingCard class extending Card
public class PlayingCard extends Card {

    // Standard card suits
    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }
    // Standard card vlues (aces through king)
    public enum Value {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
    }

    // card attributes
    private final Suit suit; // immutable
    private final Value value; // immutable
    private boolean faceUp; // whether or not the card is visible to the player

    // constructor setting initial values and defaults to face up
    public PlayingCard(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
        this.faceUp = true; // cards are face up by default (true)
    }

    // getter + returns the suit of the card
    public Suit getSuit() {
        return suit;
    }

    // getter + returns the value
    public Value getValue() {
        return value;
    }

    // checks if card is face up and visible to the player
    public boolean isFaceUp() {
        return faceUp;
    }

    // sets the status of the card (face-up) used for dealer hole card
    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }


    // calculates the blackjack point value for the card
    public int getPointValue() {
        switch (value) {
            case ACE:
                return 11; // Will be handled as 1 or 11 in Hand class
            case TWO:
                return 2;
            case THREE:
                return 3;
            case FOUR:
                return 4;
            case FIVE:
                return 5;
            case SIX:
                return 6;
            case SEVEN:
                return 7;
            case EIGHT:
                return 8;
            case NINE:
                return 9;
            case TEN:
            case JACK:
            case QUEEN:
            case KING:
                return 10; // all face cards are worth 10 points
            default:
                return 0; // this should never happen
        }
    }


    // output of the card
    // shows value and suit if the card is face up. otherwise it lets you know its face down
    @Override
    public String toString() {
        return faceUp ? value + " of " + suit : "Card is face down";
    }
}
