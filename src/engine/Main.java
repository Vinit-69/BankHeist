package engine;

import HeistPlanner.*;
import agents.*;
import tools.*;
import java.io.IOException;
import java.util.Scanner;
import persistance.SaveData;
import Map.BankMapBuilder;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HeistPlanner planA = new HeistPlanner();
        Agent a1 = null;

        try {
            HeistPlanner.process();
        } catch (IOException e) {
            System.out.println("Failed to load saved characters: " + e.getMessage());
        }

        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Play");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
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
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    try {
                        a1 = planA.createCharacter(null);
                    } catch (IOException e) {
                        System.out.println("Error creating character: " + e.getMessage());
                    }
                    break;

                } else if (choice == 2) {
                    if (HeistPlanner.characterList.isEmpty()) {
                        System.out.println("No existing characters.");
                    } else {
                        try {
                            a1 = planA.displayCharacters();
                            if (a1 != null) break;
                        } catch (IOException e) {
                            System.out.println("Error loading characters: " + e.getMessage());
                        }
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

            BankMapBuilder map = new BankMapBuilder();
            map.simulateTurnBasedHeist(a1);

            while (true) {
                System.out.println("\n--- In-Game Menu ---");
                System.out.println("1. Display Character Profile");
                System.out.println("2. Exit to Main Menu");
                System.out.print("Enter your choice: ");
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
        sc.close();
    }
}