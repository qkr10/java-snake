package org.example;

import java.util.ArrayList;
import java.util.Random;
import org.example.drawer.Drawer;
import org.example.game.GameThread;
import org.example.common.TileToUpdate;
import org.example.input.KeyboardInput;
import org.example.game.Game;
import org.example.output.ScreenOutput;

public class App {

    public App() {
        var tileUpdateQueue = new ArrayList<TileToUpdate>();
        var random = new Random();

        var game = new Game(random, tileUpdateQueue);
        var gameThread = new GameThread(game);
        new Thread(gameThread).start();

        var keyboardInput = new KeyboardInput(game.getPlayer());

        var drawer = new Drawer(tileUpdateQueue);
        var screenOutput = new ScreenOutput(drawer);

        screenOutput.frame.addKeyListener(keyboardInput);
    }

    public static void main(String[] args) {
        App app = new App();
    }
}
