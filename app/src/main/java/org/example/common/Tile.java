package org.example.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Tile {
    public final TileKind tileKind;
    public final Dir dir;

    public Tile(TileKind tileKind) {
        this.tileKind = tileKind;
        this.dir = null;
    }
}
