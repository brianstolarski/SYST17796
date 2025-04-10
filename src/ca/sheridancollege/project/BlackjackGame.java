package ca.sheridancollege.project;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

/**
 *
 * @author Steve
 * @author AlexY
 * @author Brian
 */
// The main game class
public class BlackjackGame extends Game<BlackjackPlayer> {

    // Core game components
    private Deck deck; // Deck of cards for the game
    private Dealer dealer; // Dealer who plays against the players
    private boolean gameRunning; // Tracks if the game has been initialized

    // Constructor initializes the game with a new deck and dealer
    public BlackjackGame(String name) {
        super(name);
        this.deck = new Deck();
        this.dealer = new Dealer();
        this.gameRunning = false;
    }

    // Add player to the game
    public void addPlayer(BlackjackPlayer player) {
        getPlayers().add(player);
    }

    // Initialize game by collecting player details and bankrolls
    public void setupGame() {
        System.out.println("Welcome to " + getName());
        System.out.println("How many players? (1-7)");

        int numPlayers = Input.getInt(1, 7);

        // Create players with their names and starting bankrolls
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Enter name for Player " + (i + 1) + ":");
            String name = Input.sc.nextLine();

            System.out.println("Enter starting bankroll for " + name + ":");
            int bankroll = Input.getInt(100, 10000);

            BlackjackPlayer player = new BlackjackPlayer(name, bankroll);
            addPlayer(player);
        }

        gameRunning = true;
    }

    // Main game loop - handles rounds of play until players quit or run out of money
    @Override
    public void play() {
        // Run setup if game hasn't been initialized
        if (!gameRunning) {
            setupGame();
        }

        boolean playAnotherRound = true;

        while (playAnotherRound && !getPlayers().isEmpty()) {
            // Standard blackjack round sequence
            placeBets(); // 1. Collect bets from each player
            dealInitialCards(); // 2. Deal two cards to each player and dealer

            // 3. Check for dealer blackjack (round ends immediately if true)
            if (dealer.getHand().isBlackjack()) {
                System.out.println("Dealer has Blackjack!");
                showAllHands();
                settleBets();
            } else {
                // 4. Player turns - each player decides to hit or stand
                playerTurns();

                // 5. Dealer's turn (only if at least one player hasn't busted)
                boolean allBusted = true;
                for (BlackjackPlayer player : getPlayers()) {
                    if (!player.getHand().isBusted()) {
                        allBusted = false;
                        break;
                    }
                }

                if (!allBusted) {
                    dealerTurn();
                }

                // 6. Determine winners and settle bets
                showAllHands();
                settleBets();
            }

            // 7. Clean up and prepare for next round
            cleanUp();

            // Remove players with no money
            ArrayList<BlackjackPlayer> playersToRemove = new ArrayList<>();
            for (BlackjackPlayer player : getPlayers()) {
                if (player.getBankAmount().getBalance() <= 0) {
                    System.out.println(player.getName() + " is out of money and leaves the table.");
                    playersToRemove.add(player);
                }
            }

            for (BlackjackPlayer player : playersToRemove) {
                getPlayers().remove(player);
            }

            // Check if another round should be played
            if (getPlayers().isEmpty()) {
                System.out.println("All players are out of the game.");
                playAnotherRound = false;
            } else {
                System.out.println("Play another round? (Y/N)");
                String answer = Input.sc.nextLine().trim().toUpperCase();
                playAnotherRound = answer.equals("Y") || answer.equals("YES");
            }
        }

        declareWinner();
    }

    // Process betting for each player with validation
    private void placeBets() {
        // Use iterator to safely remove players who leave during betting
        Iterator<BlackjackPlayer> iterator = getPlayers().iterator();
        while (iterator.hasNext()) {
            BlackjackPlayer player = iterator.next();
            System.out.println(player.getName() + "'s turn to bet. " + player.getBankAmount());
            System.out.println("How much would you like to bet? (Enter 0 to leave the game)");

            int bet = -1;
            while (bet < 0) {
                try {
                    String input = Input.sc.nextLine();
                    bet = Integer.parseInt(input);
                    if (bet == 0) {
                        // Player wants to leave the game
                        System.out.println(player.getName() + " leaves the game.");
                        iterator.remove();
                        break;
                    } else if (bet < 0) {
                        System.out.println("Bet cannot be negative.");
                    } else if (bet > player.getBankAmount().getBalance()) {
                        System.out.println("Bet cannot exceed your balance of $" + player.getBankAmount().getBalance());
                        bet = -1;
                    } else {
                        // Valid bet, place it and continue
                        player.placeBet(bet);
                        System.out.println(player.getName() + " bets $" + bet);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
        }
    }

    // Deal initial two cards to players and dealer
    private void dealInitialCards() {
        // Clear hands from previous round
        for (BlackjackPlayer player : getPlayers()) {
            player.getHand().clear();
        }
        dealer.getHand().clear();

        // Check if deck needs to be reshuffled
        if (deck.getCards().size() < (getPlayers().size() + 1) * 4) {
            System.out.println("Reshuffling the deck...");
            deck.reset();
        }

        // Deal two cards to each player and dealer
        for (int i = 0; i < 2; i++) {
            for (BlackjackPlayer player : getPlayers()) {
                player.getHand().addCard(deck.dealCard());
            }

            // dealer second card is face down (hole card)
            Card dealerCard = deck.dealCard();
            if (i == 1 && dealerCard instanceof PlayingCard) {
                ((PlayingCard) dealerCard).setFaceUp(false);
            }
            dealer.getHand().addCard(dealerCard);
        }

        // Display initial hands and only show dealer's up card
        for (BlackjackPlayer player : getPlayers()) {
            System.out.println(player.getName() + "'s hand: " + player.getHand());
        }
        System.out.println("Dealer's up card: " + dealer.getHand().getCards().get(0));
    }

    // Process each player's turn (hit/stand decisions)
    private void playerTurns() {
        for (BlackjackPlayer player : getPlayers()) {
            // Players with blackjack skip their turn
            if (player.getHand().isBlackjack()) {
                System.out.println(player.getName() + " has Blackjack!");
                continue;
            }

            boolean playerDone = false;
            while (!playerDone) {
                System.out.println("\n" + player.getName() + "'s turn:");
                System.out.println("Hand: " + player.getHand());
                System.out.println("1. Hit");
                System.out.println("2. Stand");
                System.out.println("Choose an action (1-2):");

                int choice = Input.getInt(1, 2);
                switch (choice) {
                    case 1: // Hit = take another card
                        Card newCard = deck.dealCard();
                        System.out.println(player.getName() + " draws: " + newCard);
                        player.getHand().addCard(newCard);
                        System.out.println("New hand: " + player.getHand());
                        if (player.getHand().isBusted()) {
                            System.out.println(player.getName() + " busts!");
                            playerDone = true;
                        }
                        break;
                    case 2: // Stand = end turn with current hand
                        System.out.println(player.getName() + " stands with " + player.getHand().calculateScore());
                        playerDone = true;
                        break;
                }
            }
        }
    }

    // dealer plays their hand according to standard rules
    private void dealerTurn() {
        // Reveal dealer's face-down card
        for (Card card : dealer.getHand().getCards()) {
            if (card instanceof PlayingCard) {
                ((PlayingCard) card).setFaceUp(true);
            }
        }
        System.out.println("\nDealer's hand: " + dealer.getHand());

        // dealer must hit until reaching 17 or higher
        while (dealer.shouldHit()) {
            Card newCard = deck.dealCard();
            System.out.println("Dealer draws: " + newCard);
            dealer.getHand().addCard(newCard);
            System.out.println("Dealer's hand: " + dealer.getHand());
            if (dealer.getHand().isBusted()) {
                System.out.println("Dealer busts!");
                break;
            }
        }
    }

    // shows the final hands of all players and the dealer
    private void showAllHands() {
        System.out.println("\nFinal hands:");
        for (BlackjackPlayer player : getPlayers()) {
            System.out.println(player.getName() + ": " + player.getHand());
        }
        System.out.println("Dealer: " + dealer.getHand());
    }

    // Compare hands and determine winners and payouts
    private void settleBets() {
        int dealerScore = dealer.getHand().calculateScore();
        boolean dealerBlackjack = dealer.getHand().isBlackjack();
        boolean dealerBusted = dealer.getHand().isBusted();

        for (BlackjackPlayer player : getPlayers()) {
            int playerScore = player.getHand().calculateScore();
            boolean playerBlackjack = player.getHand().isBlackjack();
            boolean playerBusted = player.getHand().isBusted();

            System.out.print(player.getName() + " ($" + player.getCurrentBet() + "): ");

            // Determine winner for each player and process payouts
            if (playerBusted) {
                System.out.println("Busted - loses bet");
                player.loseBet();
            } else if (playerBlackjack && !dealerBlackjack) {
                System.out.println("Blackjack! Wins 3:2");
                player.winBet(true); // true means blackjack win (pays 3:2)
            } else if (dealerBusted) {
                System.out.println("Dealer busted - wins bet");
                player.winBet(false); // regular win (pays 1:1)
            } else if (playerScore > dealerScore) {
                System.out.println("Wins with higher score");
                player.winBet(false);
            } else if (playerScore < dealerScore) {
                System.out.println("Loses with lower score");
                player.loseBet();
            } else {
                // Push - same score
                System.out.println("Push - bet returned");
                player.pushBet();
            }

            System.out.println("New balance: $" + player.getBankAmount().getBalance());
        }
    }

    // Reset game state for next round
    private void cleanUp() {
        // Clear all hands
        for (BlackjackPlayer player : getPlayers()) {
            player.getHand().clear();
        }
        dealer.getHand().clear();
    }

    // Announce final results at the end of the game
    @Override
    public void declareWinner() {
        System.out.println("\nGame Over!");
        System.out.println("Final Bankrolls:");

        // Find player with highest balance
        BlackjackPlayer winner = null;
        int maxBalance = 0;

        for (BlackjackPlayer player : getPlayers()) {
            int balance = player.getBankAmount().getBalance();
            System.out.println(player.getName() + ": $" + balance);
            if (balance > maxBalance) {
                maxBalance = balance;
                winner = player;
            }
        }

        // Announce winner with final balance
        if (winner != null) {
            System.out.println("\nThe winner is " + winner.getName() + " with $" + maxBalance + "!");
        } else {
            System.out.println("\nNo winner! All players lost all their money.");
        }
    }
}