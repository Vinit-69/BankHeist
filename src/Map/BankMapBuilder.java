package Map;

import agents.Agent;
import Nodes.*;
import java.util.Scanner;

public class BankMapBuilder {
    private final int rows = 5;
    private final int cols = 5;
    private SecurityNodes[][] grid = new SecurityNodes[rows][cols];
    private int playerX = 0;
    private int playerY = 0;

    public BankMapBuilder() {
        buildMap();
    }

    private void buildMap() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new NormalNode();
            }
        }
        grid[1][2] = new Firewall();
        grid[2][3] = new Lasers();
        grid[4][4] = new BankVault();
    }

    private void displayMap() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == playerX && j == playerY) {
                    System.out.print("P ");
                } else if (grid[i][j] instanceof Firewall) {
                    System.out.print("F ");
                } else if (grid[i][j] instanceof Lasers) {
                    System.out.print("L ");
                } else if (grid[i][j] instanceof BankVault) {
                    System.out.print("V ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    public void simulateTurnBasedHeist(Agent player) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Bank Heist Simulation ---");
            displayMap();
            System.out.println("Move Options: 1. Up | 2. Down | 3. Left | 4. Right | 5. Exit");
            int move = sc.nextInt();

            if (move == 5) break;

            int newX = playerX;
            int newY = playerY;

            switch (move) {
                case 1 -> newX--;
                case 2 -> newX++;
                case 3 -> newY--;
                case 4 -> newY++;
                default -> {
                    System.out.println("Invalid input");
                    continue;
                }
            }

            if (newX < 0 || newY < 0 || newX >= rows || newY >= cols) {
                System.out.println("Can't move there!");
                continue;
            }

            playerX = newX;
            playerY = newY;

            SecurityNodes current = grid[playerX][playerY];
            boolean pass = current.check(player.getStats());

            if (current instanceof BankVault) {
                System.out.println("You've reached the vault. Heist successful!");
                break;
            } else if (pass) {
                System.out.println("Security node passed!");
            } else if (!(current instanceof NormalNode)) {
                System.out.println("Failed security check! Try another path.");
            }
        }
    }
}
