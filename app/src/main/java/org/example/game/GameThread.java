package org.example.game;

public class GameThread implements Runnable {
    private Runnable game;

    public GameThread(Runnable game) {
        this.game = game;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(500);
                game.run();
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
