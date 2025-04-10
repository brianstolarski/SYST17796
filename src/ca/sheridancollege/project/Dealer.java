package ca.sheridancollege.project;

/**
 *
 * @author Steve
 */
public class Dealer extends Player {
    private Hand hand; // dealers current cards
    

    // creates a new dealer named "dealer"
    public Dealer() {
        super("Dealer");
        this.hand = new Hand();
    }
    
    // returns the dealers hand of cards
    public Hand getHand() {
        return hand;
    }
    
    @Override
    public void play() {
        // Implementation will be in the BlackjackGame class
    }
    

    // this will determine if the dealer should take another card
    // it follows the standard blackjack rules: hit on 16 or less, stand on 17 or more
    // returns true if dealer should hit, false otherwise
    public boolean shouldHit() {
        // Dealer must hit on 16 or less, stand on 17 or more
        return hand.calculateScore() < 17;
    }
}
