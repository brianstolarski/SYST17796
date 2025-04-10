package ca.sheridancollege.project;

/**
 *
 * @author Steve
 */
public class BlackjackPlayer extends Player {

    // the players current hand, money and amount currently betting
    private Hand hand;
    private PlayerBank playerBank;
    private int currentBet;


    // name = the players name
    // initial balance = starting money amount
    public BlackjackPlayer(String name, int initialBalance) {
        super(name);
        this.hand = new Hand();
        this.playerBank = new PlayerBank(initialBalance);
        this.currentBet = 0;
    }

    // returning players hand of cards
    public Hand getHand() {
        return hand;
    }

    // returns the players bank
    public PlayerBank getBankAmount() {
        return playerBank;
    }

    // returns the current bet amound
    public int getCurrentBet() {
        return currentBet;
    }

    // sets the players current bet amount
    public void setCurrentBet(int bet) {
        this.currentBet = bet;
    }


    // places a bet for the player
    // then deducts money from the bank and sets the current bet
    // (amount) being the amount to bet
    // returning true is the bet was successfull, false if not enough money in bank
    public boolean placeBet(int amount) {
        boolean success = playerBank.placeBet(amount);
        if (success) {
            currentBet = amount;
        }
        return success;
    }

    // this processes a winning bet
    public void winBet(boolean blackjack) {
        if (blackjack) {
            // Blackjack pays 3:2 (bet + 1.5*bet)
            playerBank.addMoney(currentBet * 5 / 2); 
        } else {
            // Regular win pays 1:1 (bet + bet)
            playerBank.addMoney(currentBet * 2);
        }
        currentBet = 0;
    }

    // processess a losing bet
    public void loseBet() {
        // Bet already removed from PlayerBank, just reset current bet
        currentBet = 0;
    }


    // processes a push (tie) the bet returns to the original player
    public void pushBet() {
        // Push (tie) returns the original bet
        playerBank.addMoney(currentBet);
        currentBet = 0;
    }

    @Override
    public void play() {
        // Implementation will be in the BlackjackGame class
    }
}
