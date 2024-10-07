package org.example;

import org.example.common.Pos;
import org.example.common.TileToUpdate;

public class Equal {

    @FunctionalInterface
    public interface EqualFunction <T> {
        boolean equals(T a, T b);
    }

    public static EqualFunction<Pos> posEquals = (p1, p2) -> p1.x == p2.x && p1.y == p2.y;

    public static EqualFunction<TileToUpdate> tileToUpdateEquals = (t1, t2) ->
            posEquals.equals(t1.pos, t2.pos) && t1.kind == t2.kind;

}
