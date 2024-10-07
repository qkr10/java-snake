package org.example.common;

public enum TileKind {
    ground,
    wall,
    tail,
    player,
    food;

    public Tile getTile() {
        return new Tile(this);
    }
}