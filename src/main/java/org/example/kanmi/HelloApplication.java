package org.example.kanmi;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import org.example.kanmi.arena.Arena;
import org.example.kanmi.player.Player;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {

        Game game = new Game();
        Player player = Player.getRegularPlayer();
        Arena arena = Arena.getRegularArena();
        game.setPlayer(player);
        game.setArena(arena);
        game.start();

        Scene scene = new Scene(game, 800, 500, true);
        scene.setCamera(player.getCamera());
        scene.addEventHandler(KeyEvent.ANY, game::onKeyEvent);
        scene.addEventHandler(MouseEvent.ANY, game::onMouseEvent);
        scene.setCursor(Cursor.NONE);
        stage.setTitle("Kanmi");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}