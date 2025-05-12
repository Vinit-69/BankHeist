package Graphics;

import agents.Agent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameController controller = new GameController(null);

        Scene scene = new Scene(controller.getRoot(), 400, 400);
        primaryStage.setTitle("Bank Heist - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        controller.startGame();
    }
}
