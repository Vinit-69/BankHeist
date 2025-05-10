package Enemies;

import Nodes.SecurityNodes;
import Nodes.WallNode;

public class Enemy {
    private int x;
    private int y;
    private int targetX;
    private int targetY;

    public Enemy(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void setTarget(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void move(SecurityNodes[][] grid) {

        int newX = x;
        int newY = y;

        if (x < targetX) newX++;
        else if (x > targetX) newX--;

        if (y < targetY) newY++;
        else if (y > targetY) newY--;

        if (newX >= 0 && newY >= 0 && newX < grid.length && newY < grid[0].length) {

            if (!(grid[newX][newY] instanceof WallNode)) {
                x = newX;
                y = newY;
            } else {

                System.out.println("Enemy cannot move through the wall at (" + newX + ", " + newY + ")");
            }
        }
    }


    public boolean isAtPlayer(int playerX, int playerY) {
        return x == playerX && y == playerY;
    }

    public void displayPosition() {
        System.out.println("Enemy is at (" + x + ", " + y + ")");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
