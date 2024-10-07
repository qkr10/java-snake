package org.example.common;

public class TileToUpdate {

    public final Pos pos;
    public final TileKind kind;
    public final Dir dir;

    public TileToUpdate(Pos pos, TileKind kind, Dir dir) {
        this.pos = pos;
        this.kind = kind;
        this.dir = dir;
    }

    public TileToUpdate(Pos pos, TileKind kind) {
        this(pos, kind, null);
    }
}
