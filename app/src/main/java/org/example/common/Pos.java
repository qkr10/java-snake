package org.example.common;

public class Pos {

    public final int x, y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pos(Pos pos) {
        this(pos.x, pos.y);
    }

    public Pos(int xy) {
        this(xy, xy);
    }

    public Pos add(Pos pos) {
        return new Pos(x + pos.x, y + pos.y);
    }

    public Pos sub(Pos pos) {
        return new Pos(x - pos.x, y - pos.y);
    }

    public Pos div(int n) {
        return new Pos(x / n, y / n);
    }

    public Pos mul(int n) {
        return new Pos(x * n, y * n);
    }

    @Override
    public boolean equals(Object pos) {
        if (pos instanceof Pos) {
            Pos p = (Pos) pos;
            return x == p.x && y == p.y;
        }
        return false;
    }
}
