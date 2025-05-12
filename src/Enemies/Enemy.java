package Enemies;

import Nodes.SecurityNodes;
import Nodes.WallNode;

public class Enemy {
    private int x;
    private int y;
    private int targetX;
    private int targetY;
    private boolean active = true;  // Enemy stops if game is won or paused

    public Enemy(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void setTarget(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public void move(SecurityNodes[][] grid) {
        if (!active) return;

        int dx = Integer.compare(targetX, x); // -1, 0, or 1
        int dy = Integer.compare(targetY, y);

        int newX = x + dx;
        int newY = y + dy;

        // Preferred move (diagonal-style: prioritize both if clear)
        if (isValidMove(grid, newX, newY)) {
            x = newX;
            y = newY;
            return;
        }

        // Try alternate directions (x then y)
        if (dx != 0 && isValidMove(grid, x + dx, y)) {
            x += dx;
        } else if (dy != 0 && isValidMove(grid, x, y + dy)) {
            y += dy;
        } else {
            // Fully blocked - try orthogonal backup
            if (isValidMove(grid, x + 1, y)) x++;
            else if (isValidMove(grid, x - 1, y)) x--;
            else if (isValidMove(grid, x, y + 1)) y++;
            else if (isValidMove(grid, x, y - 1)) y--;
        }
    }

    private boolean isValidMove(SecurityNodes[][] grid, int x, int y) {
        return x >= 0 && y >= 0 &&
                x < grid.length && y < grid[0].length &&
                !(grid[x][y] instanceof WallNode);
    }

    public boolean isAtPlayer(int playerX, int playerY) {
        return x == playerX && y == playerY;
    }

    public void displayPosition() {
        System.out.println("Enemy is at (" + x + ", " + y + ")");
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
