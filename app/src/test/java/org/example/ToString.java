package org.example;

import org.example.common.Pos;
import org.example.common.TileToUpdate;

public class ToString {
    public static String pos2String(Pos p) {
        return "x: " + p.x + ", y: " + p.y;
    }

    public static String tileToUpdate2String(TileToUpdate tile) {
        if (tile == null) {
            return "null";
        }
        return pos2String(tile.pos) + ", tile: " + tile.kind;
    }
}
