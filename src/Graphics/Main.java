package Graphics;

import agents.Agent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.HeistPlannerGUI;

public class Main extends Application {

    private final HeistPlannerGUI planner = new HeistPlannerGUI();

    @Override
    public void start(Stage primaryStage) {
        try {
            HeistPlannerGUI.process(); // Load saved characters
        } catch (Exception e) {
            showError("Failed to load characters: " + e.getMessage());
        }

        // Main menu buttons
        Button createBtn = new Button("Create Character");
        Button chooseBtn = new Button("Choose Character");
        Button deleteBtn = new Button("Delete Character");
        Button exitBtn = new Button("Exit");

        createBtn.setOnAction(e -> {
            try {
                Agent created = planner.createCharacterGUI();
                if (created != null) {
                    showInfo("Character created: " + created.getAgentName());
                }
            } catch (Exception ex) {
                showError("Error creating character: " + ex.getMessage());
            }
        });

        chooseBtn.setOnAction(e -> {
            Agent selected = planner.displayCharactersGUI();
            if (selected != null) {
                GameController game = new GameController(selected);
                game.startGame(); // attaches key listener to root

                Scene gameScene = new Scene(game.getRoot(), 600, 600);
                primaryStage.setScene(gameScene);
                primaryStage.setTitle("Bank Heist");

                // Ensure focus is requested AFTER scene is shown
                Platform.runLater(() -> {
                    game.getRoot().requestFocus();
                });
            }
        });




        deleteBtn.setOnAction(e -> planner.deleteCharactersGUI());

        exitBtn.setOnAction(e -> System.exit(0));

        VBox root = new VBox(15, createBtn, chooseBtn, deleteBtn, exitBtn);
        root.setStyle("-fx-padding: 40; -fx-alignment: center; -fx-background-color: #1e1e1e;");
        root.getChildren().forEach(btn -> btn.setStyle("-fx-font-size: 16px; -fx-pref-width: 200px;"));

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Heist Planner - Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
