package Graphics;

import Enemies.Enemy;
import Map.BankMapBuilder;
import Minigame.gui.FirewallMinigameGUI;
import Minigame.gui.LasersMinigameGUI;
import Minigame.gui.VaultMinigameGUI;
import Nodes.*;
import agents.Agent;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.geometry.Pos;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import skills.PathfinderSkill;
import javafx.event.ActionEvent;

public class GameController {
    private VBox root;
    private BankMapBuilder mapBuilder;
    private final int viewSize = 9;
    private boolean inMinigame = false;
    private Enemy enemy;
    private int startX, startY;
    private PathfinderSkill pathfinderSkill = new PathfinderSkill();
    private Label skillStatusLabel;
    private Canvas gameCanvas;
    private GraphicsContext gc;
    private int tileSize = 60;
    private boolean skillActive = false;

    public GameController(Agent selected) {
        mapBuilder = new BankMapBuilder();
        startX = mapBuilder.getPlayerX();
        startY = mapBuilder.getPlayerY();
        root = new VBox();

        // Initialize Canvas
        gameCanvas = new Canvas(viewSize * tileSize, viewSize * tileSize);
        gc = gameCanvas.getGraphicsContext2D();

        // Create skill UI
        createSkillUI();
        root.getChildren().add(gameCanvas);
        root.setFocusTraversable(true);
    }

    private void createSkillUI() {
        skillStatusLabel = new Label("Pathfinder: 0/3 uses");
        skillStatusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        skillStatusLabel.setTextFill(Color.DARKBLUE);
        skillStatusLabel.setPadding(new Insets(5));

        HBox skillBox = new HBox(skillStatusLabel);
        skillBox.setAlignment(Pos.CENTER_LEFT);
        skillBox.setPadding(new Insets(10));
        root.getChildren().add(skillBox);
    }


    public void startGame() {
        renderVisibleMap();
        root.setOnKeyPressed(this::handleMovement);
        root.setFocusTraversable(true);
        root.requestFocus();
    }

    private void handleMovement(KeyEvent event) {
        if (inMinigame || skillActive) return; // Don't move during minigame or skill animation

        if (event.getCode() == KeyCode.F) {
            showPathfinderDirection();
            return;
        }

        int dx = 0, dy = 0;
        KeyCode key = event.getCode();

        if (key == KeyCode.UP) dx = -1;
        else if (key == KeyCode.DOWN) dx = 1;
        else if (key == KeyCode.LEFT) dy = -1;
        else if (key == KeyCode.RIGHT) dy = 1;

        if (dx != 0 || dy != 0) {
            animatePlayerMove(dx, dy);
        }
    }

    private void animatePlayerMove(int dx, int dy) {
        int currentX = mapBuilder.getPlayerX();
        int currentY = mapBuilder.getPlayerY();
        int targetX = currentX + dx;
        int targetY = currentY + dy;

        if (mapBuilder.isInBounds(targetX, targetY) &&
                !(mapBuilder.getGrid()[targetX][targetY] instanceof WallNode)) {

            mapBuilder.movePlayer(dx, dy);
            renderVisibleMap();

            // If trap, minigame starts. Otherwise, move enemy.
            if (!checkForTrapAndTriggerMinigame(targetX, targetY, currentX, currentY)) {
                moveEnemyOnce(); // Enemy takes turn after player
            }
        }
    }


    private boolean checkForTrapAndTriggerMinigame(int x, int y, int previousX, int previousY) {
        SecurityNodes node = mapBuilder.getGrid()[x][y];

        if (node instanceof Firewall) {
            triggerTrapMinigame(x, y, previousX, previousY, "Firewall");
            return true;
        } else if (node instanceof Lasers) {
            triggerTrapMinigame(x, y, previousX, previousY, "Lasers");
            return true;
        } else if (node instanceof BankVault) {
            triggerTrapMinigame(x, y, previousX, previousY, "Vault");
            return true;
        }
        return false;
    }


    private void triggerTrapMinigame(int x, int y, int previousX, int previousY, String trapType) {
        inMinigame = true;

        Platform.runLater(() -> {
            Stage stage = (Stage) root.getScene().getWindow();

            Runnable onSuccess = () -> {
                inMinigame = false;
                Platform.runLater(() -> {
                    mapBuilder.setPlayerPosition(x, y);
                    renderVisibleMap();
                    root.requestFocus();
                });
            };

            Runnable onFailure = () -> {
                inMinigame = false;
                Platform.runLater(() -> {
                    if (enemy == null) {
                        enemy = new Enemy(startX, startY);
                    }
                    enemy.setTarget(mapBuilder.getPlayerX(), mapBuilder.getPlayerY());
                    mapBuilder.setPlayerPosition(previousX, previousY);
                    renderVisibleMap();
                    root.requestFocus();
                });
            };

            switch (trapType) {
                case "Firewall":
                    new FirewallMinigameGUI().start(stage, onSuccess, onFailure);
                    break;
                case "Lasers":
                    new LasersMinigameGUI().start(stage, onSuccess, onFailure);
                    break;
                case "Vault":
                    new VaultMinigameGUI().start(stage, onSuccess, onFailure);
                    break;
            }
        });
    }

    private void moveEnemyOnce() {
        // Prevent enemy from moving after win
        if (mapBuilder.getPlayerX() == startX && mapBuilder.getPlayerY() == startY) {
            return;
        }

        if (enemy != null) {
            enemy.setTarget(mapBuilder.getPlayerX(), mapBuilder.getPlayerY());
            enemy.move(mapBuilder.getGrid());

            renderVisibleMap();

            // If enemy reaches player, show game over
            if (enemy.isAtPlayer(mapBuilder.getPlayerX(), mapBuilder.getPlayerY())) {
                showGameOver();
            }
        }
    }


    private void renderVisibleMap() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        int playerX = mapBuilder.getPlayerX();
        int playerY = mapBuilder.getPlayerY();
        int halfView = viewSize / 2;

        // Draw grid background
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // Draw tiles
        for (int i = -halfView; i <= halfView; i++) {
            for (int j = -halfView; j <= halfView; j++) {
                int worldX = playerX + i;
                int worldY = playerY + j;
                int screenX = (j + halfView) * tileSize;
                int screenY = (i + halfView) * tileSize;

                if (mapBuilder.isInBounds(worldX, worldY)) {
                    SecurityNodes node = mapBuilder.getGrid()[worldX][worldY];

                    // Tile colors
                    if (node instanceof Firewall) {
                        gc.setFill(Color.RED);
                    } else if (node instanceof Lasers) {
                        gc.setFill(Color.YELLOW);
                    } else if (node instanceof BankVault) {
                        gc.setFill(Color.BLUE);
                    } else if (node instanceof WallNode) {
                        gc.setFill(Color.DARKGRAY);
                    } else {
                        gc.setFill(Color.LIGHTGRAY);
                    }

                    gc.fillRect(screenX, screenY, tileSize, tileSize);
                    gc.setStroke(Color.BLACK);
                    gc.strokeRect(screenX, screenY, tileSize, tileSize);

                    // Draw symbols
                    gc.setFill(Color.BLACK);
                    gc.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
                    if (worldX == playerX && worldY == playerY) {
                        gc.fillText("P", screenX + 25, screenY + 35);
                    } else {
                        char symbol = mapBuilder.getSymbolAt(worldX, worldY);
                        if (symbol != ' ' && symbol != '.') {
                            gc.fillText(String.valueOf(symbol), screenX + 25, screenY + 35);
                        }
                    }
                } else {
                    // Out of bounds
                    gc.setFill(Color.DARKGRAY);
                    gc.fillRect(screenX, screenY, tileSize, tileSize);
                }
            }
        }

        // Draw enemy
        if (enemy != null) {
            int enemyX = enemy.getX();
            int enemyY = enemy.getY();

            if (Math.abs(enemyX - playerX) <= halfView &&
                    Math.abs(enemyY - playerY) <= halfView) {

                int screenX = (enemyY - playerY + halfView) * tileSize;
                int screenY = (enemyX - playerX + halfView) * tileSize;

                gc.setFill(Color.rgb(200, 0, 0, 0.8));
                gc.fillOval(screenX + 5, screenY + 5, tileSize - 10, tileSize - 10);
                gc.setFill(Color.WHITE);
                gc.fillText("E", screenX + 25, screenY + 35);
            }
        }

        // Draw "Press F" hint in corner
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRoundRect(10, 10, 120, 30, 10, 10);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.fillText("Press F for path", 20, 30);

        // Draw escape message
        if (enemy != null && playerX == startX && playerY == startY) {
            gc.setFill(Color.GREEN);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            gc.fillText("ESCAPE!", tileSize, tileSize);
        }
    }

    private void showPathfinderDirection() {
        String direction = pathfinderSkill.suggestDirection(
                mapBuilder.getPlayerX(),
                mapBuilder.getPlayerY(),
                mapBuilder.getGrid()
        );

        // Use JavaFX Text node to measure string
        Text text = new Text(direction);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        double textWidth = text.getLayoutBounds().getWidth();
        double textHeight = text.getLayoutBounds().getHeight();

        double padding = 10;
        double boxWidth = textWidth + 2 * padding;
        double boxHeight = textHeight + 2 * padding;

        double boxX = gameCanvas.getWidth() / 2 - boxWidth / 2;
        double boxY = gameCanvas.getHeight() / 2 - boxHeight / 2;

        // Draw background
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

        // Draw text
        gc.setFill(Color.WHITE);
        gc.setFont(text.getFont());
        gc.fillText(direction, boxX + padding, boxY + padding + textHeight / 2);

        // Clear after 1 second
        PauseTransition clear = new PauseTransition(Duration.seconds(1));
        clear.setOnFinished(e -> renderVisibleMap());
        clear.play();
    }


    private void showGameOver() {
        Platform.runLater(() -> {
            Stage gameOverStage = new Stage();
            gameOverStage.initModality(Modality.APPLICATION_MODAL);
            gameOverStage.setTitle("Game Over");

            VBox layout = new VBox(20);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20));
            layout.setStyle("-fx-background-color: #2b2b2b;");

            Label title = new Label("GAME OVER");
            title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
            title.setTextFill(Color.RED);

            Label message = new Label("The enemy caught you!");
            message.setFont(Font.font(18));
            message.setTextFill(Color.WHITE);

            Button restartButton = new Button("Try Again");
            restartButton.setStyle("-fx-base: #ff5555; -fx-font-size: 16px;");
            restartButton.setOnAction(e -> {
                resetGame();
                gameOverStage.close();
            });

            Button exitButton = new Button("Exit");
            exitButton.setStyle("-fx-font-size: 16px;");
            exitButton.setOnAction(e -> {
                Platform.exit();
                System.exit(0);
            });

            HBox buttons = new HBox(15, restartButton, exitButton);
            buttons.setAlignment(Pos.CENTER);

            layout.getChildren().addAll(title, message, buttons);

            Scene scene = new Scene(layout, 400, 250);
            gameOverStage.setScene(scene);
            gameOverStage.showAndWait();
        });
    }

    private void resetGame() {
        mapBuilder = new BankMapBuilder();
        enemy = null;
        inMinigame = false;
        pathfinderSkill = new PathfinderSkill(); // Reset skill uses
        updateSkillStatus();
        renderVisibleMap();
        Platform.runLater(() -> root.requestFocus());
    }




    private void handleSkillUse(ActionEvent event) {
        if (pathfinderSkill.isExhausted() || skillActive) return;

        skillActive = true;
        String suggestion = pathfinderSkill.suggestDirection(
                mapBuilder.getPlayerX(),
                mapBuilder.getPlayerY(),
                mapBuilder.getGrid()
        );

        updateSkillStatus();
        showSkillSuggestion(suggestion);
        highlightSuggestedDirection(suggestion);

        // Re-enable movement after skill animation
        PauseTransition enableMovement = new PauseTransition(Duration.seconds(2));
        enableMovement.setOnFinished(e -> skillActive = false);
        enableMovement.play();
    }

    private void updateSkillStatus() {
        String status = pathfinderSkill.isExhausted() ?
                "Pathfinder: 3/3 (Exhausted)" :
                String.format("Pathfinder: %d/3 uses", pathfinderSkill.getUses());

        skillStatusLabel.setText(status);
        skillStatusLabel.setTextFill(pathfinderSkill.isExhausted() ? Color.GRAY : Color.DARKBLUE);
    }

    private void showSkillSuggestion(String suggestion) {
        int centerX = (viewSize / 2) * tileSize + tileSize/2;
        int centerY = (viewSize / 2) * tileSize + tileSize/2;

        // Draw directly on canvas
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRoundRect(centerX - 120, centerY - 25, 240, 50, 20, 20);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 18)); // Larger font
        gc.fillText(suggestion, centerX - 110, centerY + 8);

        // Auto-clear after delay
        PauseTransition clearSuggestion = new PauseTransition(Duration.seconds(2));
        clearSuggestion.setOnFinished(e -> renderVisibleMap());
        clearSuggestion.play();
    }

    private void highlightSuggestedDirection(String suggestion) {
        // Extract direction from suggestion
        String direction = suggestion.replace("Suggested move: ", "")
                .replaceAll(" \\(\\d/\\d uses\\).*", "")
                .trim();

        int centerRow = viewSize / 2;
        int centerCol = viewSize / 2;
        int targetRow = centerRow;
        int targetCol = centerCol;

        switch (direction.toLowerCase()) {
            case "up" -> targetRow--;
            case "down" -> targetRow++;
            case "left" -> targetCol--;
            case "right" -> targetCol++;
            default -> { return; }
        }

        // Calculate screen position
        int x = targetCol * tileSize;
        int y = targetRow * tileSize;

        // Draw highlight
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(4); // Thicker highlight
        gc.strokeRect(x + 4, y + 4, tileSize - 8, tileSize - 8);

        // Pulse animation
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.5), gameCanvas);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.01);
        pulse.setToY(1.01);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(4);
        pulse.setOnFinished(e -> renderVisibleMap());
        pulse.play();
    }

    public VBox getRoot() {
        return root;
    }
}