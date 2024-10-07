package org.example.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.example.common.Dir;
import org.example.game.player.Player;

public class KeyboardInput implements KeyListener {

    private final Player player;

    public KeyboardInput(Player player) {
        this.player = player;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                player.rotate(Dir.Up);
                break;
            case KeyEvent.VK_DOWN:
                player.rotate(Dir.Down);
                break;
            case KeyEvent.VK_LEFT:
                player.rotate(Dir.Left);
                break;
            case KeyEvent.VK_RIGHT:
                player.rotate(Dir.Right);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
