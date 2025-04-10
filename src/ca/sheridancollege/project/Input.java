/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.sheridancollege.project;

// class which handles user input with validation
// provides a method for getting valid integer input within the specified range

/**
 *
 * @author AlexY
 */

import java.util.Scanner;

// gets an integer input from the user that is within the specified range
// handles the validation and re-checks until valid input is entered
// returns the valid integer within the range
// min = minimum valid value
// max = maximum valid value
public class Input {
    public static Scanner sc = new Scanner(System.in);

    public static int getInt(int min, int max) {
        int result = 0;
        while (result == 0) {
            try {
                int input = Integer.parseInt(sc.nextLine());
                if (input < min || input > max) {
                    System.out.println("Please enter an integer between " + min + " and " + max);
                } else {
                    result = input;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return result;
    }
}
