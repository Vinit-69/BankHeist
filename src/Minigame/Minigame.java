package Minigame;

import java.util.Random;
import java.util.Scanner;

public class Minigame {
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();

    public boolean runFirewallMinigame() {
        String pattern = generateRandomPattern(3);
        System.out.println("Memorize this pattern: " + pattern);
        try { Thread.sleep(2000); } catch (InterruptedException e) { }

        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("Enter the pattern:");
        String input = scanner.nextLine().trim().toUpperCase();
        return pattern.equals(input);
    }

    private String generateRandomPattern(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder pattern = new StringBuilder();
        for (int i = 0; i < length; i++) {
            pattern.append(chars.charAt(random.nextInt(chars.length())));
        }
        return pattern.toString();
    }

    public boolean runLasersMinigame() {
        System.out.println("Get ready... Press Enter when you see 'GO!'");
        int delay = 2000 + random.nextInt(3000);
        try { Thread.sleep(delay); } catch (InterruptedException e) { }

        long startTime = System.currentTimeMillis();
        System.out.println("GO!");
        scanner.nextLine();
        long reactionTime = System.currentTimeMillis() - startTime;

        System.out.println("Your reaction time: " + reactionTime + " ms");
        return reactionTime <= 1000;
    }

    public boolean runVaultMinigame() {
        int code = 1 + random.nextInt(10);
        int attempts = 3;
        System.out.println("Guess the number between 1 and 10. You have " + attempts + " tries.");

        while (attempts-- > 0) {
            System.out.print("Enter your guess: ");
            int guess = scanner.nextInt();

            if (guess == code) {
                System.out.println("Vault Unlocked! Escape");
                return true;
            } else if (guess < code) {
                System.out.println("The code is higher. Try again.");
            } else {
                System.out.println("The code is lower. Try again.");
            }
        }

        System.out.println("You failed to crack the vault!");
        return false;
    }
}
