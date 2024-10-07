package org.example.common;

public class FloatPos {

    public final float x, y;

    public FloatPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public FloatPos(float xy) {
        this(xy, xy);
    }

    public FloatPos(Pos pos) {
        this(pos.x, pos.y);
    }

    public Pos getPos() {
        return new Pos(Math.round(x), Math.round(y));
    }

    public FloatPos add(FloatPos pos) {
            return new FloatPos(x + pos.x, y + pos.y);
    }

    public FloatPos mul(float interval) {
        return new FloatPos(x * interval, y * interval);
    }

    public FloatPos sub(FloatPos pos) {
        return new FloatPos(x - pos.x, y - pos.y);
    }

    public FloatPos div(int i) {
        return new FloatPos(x / i, y / i);
    }

    public FloatPos[] getMinMax(FloatPos pos) {
        FloatPos min = new FloatPos(Math.min(x, pos.x), Math.min(y, pos.y));
        FloatPos max = new FloatPos(Math.max(x, pos.x), Math.max(y, pos.y));
        return new FloatPos[]{min, max};
    }
}
