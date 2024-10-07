package org.example.drawer.shape;

import org.example.common.FloatPos;

public class Line {
    public enum LineStyle {
        Thin,
        Thick
    }

    public final FloatPos s, e;
    public final LineStyle lineStyle;

    public Line(FloatPos s, FloatPos e, LineStyle lineStyle) {
        this.s = s;
        this.e = e;
        this.lineStyle = lineStyle;
    }
}
