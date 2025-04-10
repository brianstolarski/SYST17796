package ca.sheridancollege.project;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Steve
 */
public class BlackjackMain {
    public static void main(String[] args) {
        BlackjackGame game = new BlackjackGame("Blackjack");
        game.play();
        Input.sc.close();
    }
}
