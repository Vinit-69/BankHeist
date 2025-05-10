package Minigame.gui;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Duration;

public class LasersMinigameGUI {

    private long goTime;
    private boolean clicked = false;

    public void start(Stage ownerStage, Runnable onSuccess, Runnable onFailure) {
        Stage dialog = new Stage();
        dialog.initOwner(ownerStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Lasers Minigame");

        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Label instructionLabel = new Label("Get ready... Click the button when you see 'GO!'");
        Button actionButton = new Button("Wait...");

        actionButton.setDisable(true); // disabled until GO
        layout.getChildren().addAll(instructionLabel, actionButton);

        Scene scene = new Scene(layout, 350, 150);
        dialog.setScene(scene);
        dialog.show();

        int delayMillis = 2000 + (int) (Math.random() * 3000);
        PauseTransition wait = new PauseTransition(Duration.millis(delayMillis));
        wait.setOnFinished(e -> {
            instructionLabel.setText("GO!");
            actionButton.setText("CLICK!");
            actionButton.setDisable(false);
            goTime = System.currentTimeMillis();
        });
        wait.play();

        actionButton.setOnAction(e -> {
            if (!clicked) {
                clicked = true;
                long reactionTime = System.currentTimeMillis() - goTime;
                dialog.close();

                if (reactionTime <= 500) {
                    System.out.println("Success! Reaction time: " + reactionTime + " ms");
                    onSuccess.run();
                } else {
                    System.out.println("Too slow! Reaction time: " + reactionTime + " ms");
                    onFailure.run();
                }
            }
        });
    }
}
