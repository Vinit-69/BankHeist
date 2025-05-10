package Graphics;

import Map.BankMapBuilder;
import Minigame.gui.FirewallMinigameGUI;
import Minigame.gui.LasersMinigameGUI;
import Minigame.gui.VaultMinigameGUI;
import Nodes.*;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.geometry.Pos;
import javafx.application.Platform;
import javafx.util.Duration;

public class GameController {
    private VBox root;
    private GridPane gridPane;
    private BankMapBuilder mapBuilder;
    private final int viewSize = 3;
    private boolean inMinigame = false;

    public GameController() {
        mapBuilder = new BankMapBuilder();
        root = new VBox();
        gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setAlignment(Pos.CENTER);
        root.getChildren().add(gridPane);

        root.setFocusTraversable(true);
    }

    public void startGame() {
        renderVisibleMap();
        root.setOnKeyPressed(this::handleMovement);
        root.requestFocus();
    }

    private void handleMovement(KeyEvent event) {
        if (inMinigame) return;

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
        if (inMinigame) return;

        int currentX = mapBuilder.getPlayerX();
        int currentY = mapBuilder.getPlayerY();
        int targetX = currentX + dx;
        int targetY = currentY + dy;

        System.out.println("Moving from [" + currentX + "," + currentY + "] to [" +
                targetX + "," + targetY + "]");

        if (mapBuilder.isInBounds(targetX, targetY) &&
                !(mapBuilder.getGrid()[targetX][targetY] instanceof WallNode)) {

            final int previousX = currentX;
            final int previousY = currentY;

            mapBuilder.movePlayer(dx, dy);
            renderVisibleMap();

            checkForTrapAndTriggerMinigame(targetX, targetY, previousX, previousY);
        }
    }

    private void checkForTrapAndTriggerMinigame(int x, int y, int previousX, int previousY) {
        SecurityNodes node = mapBuilder.getGrid()[x][y];

        if (node instanceof Firewall) {
            System.out.println("Firewall detected - Previous position: " + previousX + "," + previousY);
            triggerTrapMinigame(x, y, previousX, previousY, "Firewall");
        }
        else if (node instanceof Lasers) {
            System.out.println("Lasers detected - Previous position: " + previousX + "," + previousY);
            triggerTrapMinigame(x, y, previousX, previousY, "Lasers");
        } else if (node instanceof BankVault) {
            System.out.println("BankVault detected - Previous position: " + previousX + "," + previousY);
            triggerTrapMinigame(x, y, previousX, previousY, "Vault");
        }

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
                    System.out.println("Rolling back to [" + previousX + "," + previousY + "]");
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

    private void renderVisibleMap() {
        gridPane.getChildren().clear();
        int playerX = mapBuilder.getPlayerX();
        int playerY = mapBuilder.getPlayerY();
        int half = viewSize / 2;

        for (int i = -half; i <= half; i++) {
            for (int j = -half; j <= half; j++) {
                int x = playerX + i;
                int y = playerY + j;

                StackPane tile = new StackPane();
                tile.setPrefSize(50, 50);
                tile.setStyle("-fx-border-color: black;");

                if (mapBuilder.isInBounds(x, y)) {
                    char symbol = mapBuilder.getSymbolAt(x, y);
                    Label label = new Label(String.valueOf(symbol));
                    label.setFont(Font.font("Consolas", FontWeight.BOLD, 20));
                    tile.getChildren().add(label);

                    if (symbol == 'F') {
                        tile.setStyle("-fx-background-color: red;");
                    } else if (symbol == 'L') {
                        tile.setStyle("-fx-background-color: yellow;");
                    } else if (symbol == 'V') {
                        tile.setStyle("-fx-background-color: blue;");
                    } else {
                        tile.setStyle("-fx-background-color: lightgray;");
                    }
                } else {
                    tile.setStyle("-fx-background-color: black;");
                }

                gridPane.add(tile, j + half, i + half);
            }
        }
    }

    public VBox getRoot() {
        return root;
    }

}