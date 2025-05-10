package Map;

import Minigame.Minigame;
import agents.Agent;
import Nodes.*;
import Custom.CustomHashMap;
import skills.PathfinderSkill;
import java.util.Stack;
import java.util.Scanner;
import Enemies.*;

public class BankMapBuilder {
    private final int rows = 5;
    private final int cols = 5;
    private SecurityNodes[][] grid = new SecurityNodes[rows][cols];
    private int playerX = 0;
    private int playerY = 0;
    private int startX = 0;
    private int startY = 0;
    private CustomHashMap<Integer, int[]> nodeCoords = new CustomHashMap<>();


    private Minigame minigame = new Minigame();
    private Enemy enemy;

    public BankMapBuilder() {
        buildMap();
        System.out.println("Using CustomHashMap to store node coordinates.");
    }

    private void buildMap() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new NormalNode();
                nodeCoords.put(i * cols + j, new int[]{i, j});
            }
        }


        grid[0][2] = new WallNode();
        grid[1][2] = new WallNode();
        grid[3][0] = new WallNode();
        grid[3][3] = new WallNode();


        grid[1][1] = new Firewall();
        grid[2][2] = new Lasers();


        grid[0][4] = new BankVault();


        setPlayerStartPosition();
    }




    private void randomizeWalls(int numberOfWalls) {
        int wallsPlaced = 0;
        while (wallsPlaced < numberOfWalls) {
            int x = (int) (Math.random() * rows);
            int y = (int) (Math.random() * cols);

            if (!(grid[x][y] instanceof WallNode) && !(grid[x][y] instanceof BankVault) && !(grid[x][y] instanceof Firewall) && !(grid[x][y] instanceof Lasers)) {
                if (isVaultReachable(x, y)) {
                    grid[x][y] = new WallNode();
                    nodeCoords.remove(x * cols + y);
                    wallsPlaced++;
                }
            }
        }
    }

    private boolean isVaultReachable(int startX, int startY) {

        boolean[][] visited = new boolean[rows][cols];

        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startX, startY});
        visited[startX][startY] = true;


        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int x = current[0];
            int y = current[1];


            if (grid[x][y] instanceof BankVault) {
                return true;
            }


            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                if (newX >= 0 && newY >= 0 && newX < rows && newY < cols &&
                        !visited[newX][newY] && !(grid[newX][newY] instanceof WallNode)) {
                    visited[newX][newY] = true;
                    stack.push(new int[]{newX, newY});
                }
            }
        }


        return false;
    }


    private void setPlayerStartPosition() {

        int vaultX = -1, vaultY = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] instanceof BankVault) {
                    vaultX = i;
                    vaultY = j;
                    break;
                }
            }
            if (vaultX != -1) break;
        }


        int maxDistance = -1;
        startX = 0;
        startY = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] instanceof NormalNode && (i != vaultX || j != vaultY)) {
                    int distance = manhattanDistance(i, j, vaultX, vaultY);
                    if (distance > maxDistance) {
                        maxDistance = distance;
                        startX = i;
                        startY = j;
                    }
                }
            }
        }

        playerX = startX;
        playerY = startY;
    }


    private int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private void displayMap() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == playerX && j == playerY) {
                    System.out.print("P ");
                } else if (enemy != null && enemy.getX() == i && enemy.getY() == j) {
                    System.out.print("E ");
                } else if (grid[i][j] instanceof Firewall) {
                    System.out.print("F ");
                } else if (grid[i][j] instanceof Lasers) {
                    System.out.print("L ");
                } else if (grid[i][j] instanceof BankVault) {
                    System.out.print("V ");
                } else if (grid[i][j] instanceof WallNode) {
                    System.out.print("# ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }


    public void simulateTurnBasedHeist(Agent player) {
        Scanner sc = new Scanner(System.in);
        int turnCounter = 0;

        while (true) {
            System.out.println("\n--- Bank Heist Simulation ---");
            displayMap();


            if (playerX == startX && playerY == startY && enemy != null) {
                System.out.println("You reached your starting position and escaped the enemy! You win!");
                break;
            }


            if (enemy != null && enemy.isAtPlayer(playerX, playerY)) {
                System.out.println("The enemy caught you! You lost the heist.");
                break;
            }

            System.out.println("Move Options: 1. Up | 2. Down | 3. Left | 4. Right | 5. Use Pathfinder Skill | 6. Exit");
            int move = sc.nextInt();

            if (move == 6) break;

            if (move == 5) {
                System.out.println("Pathfinder skill used.");
                continue;
            }

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

            if (newX < 0 || newY < 0 || newX >= rows || newY >= cols || grid[newX][newY] instanceof WallNode) {
                System.out.println("Can't move there!");
                continue;
            }


            playerX = newX;
            playerY = newY;

            SecurityNodes current = grid[playerX][playerY];

            boolean passedMinigame = true;
            boolean triggeredTrap = false;

            if (current instanceof Firewall) {
                triggeredTrap = true;
                passedMinigame = minigame.runFirewallMinigame();
                if(passedMinigame)  System.out.println("Passed Firewall");
            } else if (current instanceof Lasers) {
                triggeredTrap = true;
                passedMinigame = minigame.runLasersMinigame();
                if(passedMinigame)  System.out.println("Passed Lasers");
            } else if (current instanceof BankVault) {
                triggeredTrap = true;
                passedMinigame = minigame.runVaultMinigame();
            }

            if (triggeredTrap && !passedMinigame && enemy == null) {
                enemy = new Enemy(startX, startY);
                enemy.setTarget(playerX, playerY);
                System.out.println("Security system triggered! An enemy has started chasing you!");
            }

            if (enemy != null) {
                turnCounter++;


                if (turnCounter % 2 == 0) {
                    enemy.setTarget(playerX, playerY);
                    enemy.move(grid);
                    enemy.displayPosition();
                }


                if (enemy.isAtPlayer(playerX, playerY)) {
                    System.out.println("The enemy caught you! You lost the heist.");
                    break;
                }
            }
        }
    }


    private void startEnemyChase() {
        while (true) {
            enemy.move(grid);
            enemy.displayPosition();
            if (enemy.isAtPlayer(playerX, playerY)) {
                System.out.println("The enemy caught you! You lost the heist.");
                break;
            }

            if (playerX == startX && playerY == startY) {
                System.out.println("You Successfully Escaped");
                break;
            }
        }
    }
    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }
    public char getSymbolAt(int x, int y) {
        if (!isInBounds(x, y)) return ' ';
        SecurityNodes node = grid[x][y];
        if (x == playerX && y == playerY) return 'P';
        if (enemy != null && x == enemy.getX() && y == enemy.getY()) return 'E';
        if (node instanceof WallNode) return '#';
        if (node instanceof BankVault) return 'V';
        if (node instanceof Firewall) return 'F';
        if (node instanceof Lasers) return 'L';
        return '.';
    }
    public void movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;

        if (isInBounds(newX, newY) && !(grid[newX][newY] instanceof WallNode)) {
            playerX = newX;
            playerY = newY;
        }
    }

    public SecurityNodes[][] getGrid() {
        return grid;
    }

    public void setPlayerPosition(int x, int y) {
        if (isInBounds(x, y) && !(grid[x][y] instanceof WallNode)) {
            playerX = x;
            playerY = y;
            System.out.println("Player position set to " + x + "," + y);
        } else {
            System.out.println("Invalid position: " + x + "," + y);
        }
    }
}
