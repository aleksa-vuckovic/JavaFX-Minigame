package org.example.kanmi;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.kanmi.arena.Arena;
import org.example.kanmi.player.Player;
import org.example.kanmi.ui.Button;
import org.example.kanmi.ui.Column;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {

        Game game = new Game();
        Player player = Player.getRegularPlayer();
        Arena arena = Arena.getRegularArena();
        game.setPlayer(player);
        game.setArena(arena);
        game.start();

        //game.setCursor(Cursor.NONE);
        stage.setTitle("Kanmi");
        stage.setScene(game);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}