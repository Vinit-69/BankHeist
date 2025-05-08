package engine;

import HeistPlanner.*;
import agents.*;
import tools.*;
import java.io.*;
import java.util.*;
import persistance.*;
<<<<<<< HEAD
import Map.BankMapBuilder;
=======
>>>>>>> 41e4d33c4d05e1abbe9dfda4d1d531c321348288

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        HeistPlanner planA = new HeistPlanner();
        Agent a1 = null;

        HeistPlanner.process();

        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Play");
            System.out.println("2. Exit");
            int choice = sc.nextInt();

            if (choice == 2) break;
            else if (choice != 1) {
                System.out.println("Invalid Choice");
                continue;
            }

            while (true) {
                System.out.println("\n--- Character Menu ---");
                System.out.println("1. Create New Character");
                System.out.println("2. Choose Existing Character");
                System.out.println("3. Delete Character");
                System.out.println("4. Back to Main Menu");
                choice = sc.nextInt();

                if (choice == 1) {
                    a1 = planA.createCharacter(null);
                    break;
                } else if (choice == 2) {
                    if (HeistPlanner.characterList.isEmpty()) {
                        System.out.println("No existing Characters");
                    } else {
                        a1 = planA.displayCharacters();
                        if (a1 != null) break;
                    }
                } else if (choice == 3) {
                    HeistPlanner.delete();
                } else if (choice == 4) {
                    a1 = null;
                    break;
                } else {
                    System.out.println("Invalid Choice");
                }
            }

            if (a1 == null) continue;

<<<<<<< HEAD
            BankMapBuilder map = new BankMapBuilder();
            map.simulateTurnBasedHeist(a1);

=======
>>>>>>> 41e4d33c4d05e1abbe9dfda4d1d531c321348288
            while (true) {
                System.out.println("\n--- In-Game Menu ---");
                System.out.println("1. Display Character Profile");
                System.out.println("2. Exit to Main Menu");
                choice = sc.nextInt();

                if (choice == 1) {
                    a1.agentProfile();
                } else if (choice == 2) {
                    break;
                } else {
                    System.out.println("Invalid Choice");
                }
            }
        }

        System.out.println("Game exited. Thank you for playing!");
    }
}
