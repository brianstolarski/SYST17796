package ca.sheridancollege.project;

/**
 *
 * @author Steve
 */

 // this class represents the players bank value
 // handles all bank operations (placing bets, adding money)
public class PlayerBank {
    private int balance; // initial bank balance
    

    // 
    public PlayerBank(int initialBalance) {
        this.balance = initialBalance;
    }
    
    // returns the current balance
    public int getBalance() {
        return balance;
    }
    
    // adds money to the players balance
    // used for winning bets/payouts
    public void addMoney(double amount) {
        balance += amount; // the amount to add
    }
    

    // places a bet by removing money from the balance
    public boolean placeBet(double amount) {
        if (amount <= balance && amount > 0) {
            balance -= amount;
            return true;
        }
        return false;
    }
    
    // oouput showing the current balance of the bank
    @Override
    public String toString() {
        return "Balance: $" + balance;
    }
}
