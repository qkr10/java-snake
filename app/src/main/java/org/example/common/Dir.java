package org.example.common;

public enum Dir {
    Up,
    Down,
    Left,
    Right;

    public Pos getPos() {
        return switch (this) {
            case Up -> new Pos(0, -1);
            case Down -> new Pos(0, 1);
            case Left -> new Pos(-1, 0);
            case Right -> new Pos(1, 0);
        };
    }
}
