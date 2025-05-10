package Graphics;

import Graphics.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameController controller = new GameController();

        Scene scene = new Scene(controller.getRoot(), 400, 400);
        primaryStage.setTitle("Bank Heist - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        controller.startGame();
    }
}
