package Minigame.gui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.Pos;
import javafx.animation.*;
import javafx.util.Duration;
import java.util.*;

public class FirewallMinigameGUI {
    private String pattern;
    private final Random random = new Random();

    public void start(Stage ownerStage, Runnable onSuccess, Runnable onFailure) {
        Stage dialog = new Stage();
        dialog.initOwner(ownerStage);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Firewall Minigame");
        dialog.setAlwaysOnTop(true);

        pattern = generateRandomPattern(3);

        Label instructionLabel = new Label("Memorize this pattern: " + pattern);
        instructionLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField inputField = new TextField();
        inputField.setPromptText("Enter pattern here...");
        inputField.setVisible(false);

        Button submitButton = new Button("Submit");
        submitButton.setVisible(false);
        submitButton.setOnAction(e -> {
            String input = inputField.getText().trim().toUpperCase();
            if (input.equals(pattern)) {
                onSuccess.run();
            } else {
                onFailure.run();
            }
            dialog.close();
        });


        inputField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                submitButton.fire();
            }
        });

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            instructionLabel.setText("Enter the pattern you saw:");
            inputField.setVisible(true);
            submitButton.setVisible(true);
            inputField.requestFocus();
        });
        pause.play();

        VBox layout = new VBox(20, instructionLabel, inputField, submitButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(layout, 400, 200);
        dialog.setScene(scene);

        dialog.setMinWidth(400);
        dialog.setMinHeight(200);

        dialog.setOnCloseRequest(e -> {
            onFailure.run();
        });

        dialog.showAndWait();
    }

    private String generateRandomPattern(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder pattern = new StringBuilder();
        for (int i = 0; i < length; i++) {
            pattern.append(chars.charAt(random.nextInt(chars.length())));
        }
        return pattern.toString();
    }
}