package skills;

import Nodes.*;

import java.util.*;

public class PathfinderSkill {
    private int uses = 0;
    private final int maxUses = 3;

    public String suggestDirection(int startX, int startY, SecurityNodes[][] grid) {
        if (uses >= maxUses) {
            return "Pathfinder skill exhausted. (3/3 uses)";
        }

        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        String[] dirNames = {"Up", "Down", "Left", "Right"};

        Queue<int[]> queue = new LinkedList<>();
        Map<String, String> pathMap = new HashMap<>();

        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0];
            int cy = current[1];

            if (grid[cx][cy] instanceof BankVault) {
                // Backtrack to find the first step
                String key = cx + "," + cy;
                while (pathMap.containsKey(key) && !pathMap.get(key).equals(startX + "," + startY)) {
                    key = pathMap.get(key);
                }
                String[] parts = key.split(",");
                int nextX = Integer.parseInt(parts[0]);
                int nextY = Integer.parseInt(parts[1]);

                String direction = getDirection(startX, startY, nextX, nextY);
                uses++;
                return "Suggested move: " + direction + " (" + uses + "/" + maxUses + " uses)";
            }

            for (int d = 0; d < 4; d++) {
                int nx = cx + directions[d][0];
                int ny = cy + directions[d][1];

                if (nx >= 0 && ny >= 0 && nx < rows && ny < cols && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    queue.add(new int[]{nx, ny});
                    pathMap.put(nx + "," + ny, cx + "," + cy);
                }
            }
        }

        return "No path to vault found.";
    }

    private String getDirection(int x1, int y1, int x2, int y2) {
        if (x2 == x1 - 1 && y2 == y1) return "Up";
        if (x2 == x1 + 1 && y2 == y1) return "Down";
        if (x2 == x1 && y2 == y1 - 1) return "Left";
        if (x2 == x1 && y2 == y1 + 1) return "Right";
        return "Stay";
    }

    public boolean isExhausted() {
        return uses >= maxUses;
    }
}
