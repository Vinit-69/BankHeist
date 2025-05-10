package Minigame.gui;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.Duration;

import java.util.Random;

public class VaultMinigameGUI {
    private int secretCode;
    private int attemptsLeft = 3;

    public VaultMinigameGUI() {
        secretCode = 1 + new Random().nextInt(10);
    }

    public void start(Stage ownerStage, Runnable onSuccess, Runnable onFailure) {
        Stage dialog = new Stage();
        dialog.initOwner(ownerStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Vault Minigame");

        Label prompt = new Label("Guess the number between 1 and 10. You have 3 tries.");
        TextField inputField = new TextField();
        inputField.setPromptText("Enter your guess");

        Label feedback = new Label();
        Button submit = new Button("Submit");

        VBox layout = new VBox(10, prompt, inputField, submit, feedback);
        layout.setStyle("-fx-padding: 20;");
        layout.setPrefSize(300, 200);
        Scene minigameScene = new Scene(layout);

        submit.setOnAction(e -> {
            String input = inputField.getText().trim();
            try {
                int guess = Integer.parseInt(input);
                attemptsLeft--;

                if (guess == secretCode) {
                    dialog.close();
                    showWinScreen(ownerStage, onSuccess);
                } else if (attemptsLeft > 0) {
                    feedback.setText(guess < secretCode ? "Too low! Try again." : "Too high! Try again.");
                } else {
                    feedback.setText("Failed to crack the vault.");
                    dialog.close();
                    onFailure.run();
                }

                inputField.clear();
            } catch (NumberFormatException ex) {
                feedback.setText("Enter a valid number!");
            }
        });

        dialog.setScene(minigameScene);
        dialog.show();
    }

    private void showWinScreen(Stage ownerStage, Runnable onSuccess) {
        Stage winStage = new Stage();
        winStage.initOwner(ownerStage);
        winStage.initModality(Modality.APPLICATION_MODAL);
        winStage.setTitle("Victory!");

        Label victoryLabel = new Label("VAULT UNLOCKED\nYOU WIN!");
        victoryLabel.setTextAlignment(TextAlignment.CENTER);
        victoryLabel.setFont(Font.font("Consolas", FontWeight.EXTRA_BOLD, 40));
        victoryLabel.setTextFill(Color.GOLD);
        victoryLabel.setEffect(new DropShadow(20, Color.ORANGE));


        ScaleTransition scale = new ScaleTransition(Duration.seconds(1.2), victoryLabel);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setAutoReverse(true);
        scale.setCycleCount(Animation.INDEFINITE);

        FadeTransition fade = new FadeTransition(Duration.seconds(2), victoryLabel);
        fade.setFromValue(0.2);
        fade.setToValue(1.0);
        fade.setAutoReverse(true);
        fade.setCycleCount(Animation.INDEFINITE);

        ParallelTransition animation = new ParallelTransition(scale, fade);
        animation.play();

        Button exitButton = new Button("End Game");
        exitButton.setStyle("-fx-font-size: 16; -fx-background-color: crimson; -fx-text-fill: white;");
        exitButton.setOnAction(e -> {
            winStage.close();
            Platform.exit();
        });
        VBox winLayout = new VBox(30, victoryLabel, exitButton);
        winLayout.setStyle("-fx-background-color: black; -fx-padding: 40;");
        winLayout.setAlignment(Pos.CENTER);

        Scene winScene = new Scene(winLayout, 500, 300);
        winStage.setScene(winScene);
        winStage.show();
    }
}
