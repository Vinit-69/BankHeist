package skills;

import Nodes.BankVault;
import Nodes.SecurityNodes;
import Nodes.WallNode;
import java.util.LinkedList;
import java.util.Queue;

public class PathfinderSkill {
    private int uses = 0;
    private final int maxUses = 3;

    public String suggestDirection(int startX, int startY, SecurityNodes[][] grid) {
        if (uses >= maxUses) {
            return "Skill exhausted (F)";
        }

        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        String[] dirNames = {"Up","Down","Left","Right"};

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1];

            // Found vault
            if (grid[x][y] instanceof BankVault) {
                uses++;
                return "Go " + findInitialDirection(startX, startY, x, y) +
                        " (" + uses + "/" + maxUses + " uses)";
            }

            // Explore neighbors
            for (int i = 0; i < 4; i++) {
                int nx = x + directions[i][0];
                int ny = y + directions[i][1];

                if (nx >= 0 && ny >= 0 && nx < rows && ny < cols &&
                        !visited[nx][ny] && !(grid[nx][ny] instanceof WallNode)) {
                    visited[nx][ny] = true;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        uses++;
        return "No path found (" + uses + "/" + maxUses + " uses)";
    }

    private String findInitialDirection(int startX, int startY, int targetX, int targetY) {
        if (targetX < startX) return "Up";
        if (targetX > startX) return "Down";
        if (targetY < startY) return "Left";
        if (targetY > startY) return "Right";
        return "Stay";
    }

    public boolean isExhausted() {
        return uses >= maxUses;
    }

    public int getUses() {
        return uses;
    }
}