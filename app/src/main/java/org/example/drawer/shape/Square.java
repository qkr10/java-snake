package org.example.drawer.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.example.common.FloatPos;
import org.example.common.TileKind;
import org.example.common.Dir;
import org.example.common.TileToUpdate;

public class Square {
    public enum SquareColor {
        Red,    // player
        Orange, // tail
        Blue,   // food
        White,  // ground, eyes
        Gray,   // wall
    }

    public final FloatPos leftTop, rightBottom, rightTop, leftBottom;
    public final SquareColor squareColor;
    public final List<Square> children = new ArrayList<>();

    public Square(FloatPos leftTop, FloatPos rightBottom, SquareColor squareColor) {
        FloatPos[] minMax = leftTop.getMinMax(rightBottom);
        leftTop = new FloatPos(minMax[0].x, minMax[0].y);
        rightBottom = new FloatPos(minMax[1].x, minMax[1].y);

        this.leftTop = leftTop;
        this.rightBottom = rightBottom;
        this.rightTop = new FloatPos(rightBottom.x, leftTop.y);
        this.leftBottom = new FloatPos(leftTop.x, rightBottom.y);
        this.squareColor = squareColor;
    }

    public Square(Square grid, float interval, TileToUpdate tile, Dir dir) {
        squareColor = switch (tile.kind) {
            case food -> SquareColor.Blue;
            case ground -> SquareColor.White;
            case wall -> SquareColor.Gray;
            case tail -> SquareColor.Orange;
            case player -> SquareColor.Red;
        };

        FloatPos tileFloatPos = new FloatPos(tile.pos);
        FloatPos leftTopFloatPos = tileFloatPos.mul(interval).add(grid.leftTop);
        leftTop = leftTopFloatPos;
        rightBottom = leftTopFloatPos.add(new FloatPos(interval));
        rightTop = leftTopFloatPos.add(new FloatPos(interval, 0));
        leftBottom = leftTopFloatPos.add(new FloatPos(0, interval));
        
        drawChildren(interval, dir, tile.kind);
    }

    public Square flip(Function<FloatPos, FloatPos> method) {
        return new Square(method.apply(leftTop), method.apply(rightBottom), squareColor);
    }

    public Square add(FloatPos pos) {
        return new Square(leftTop.add(pos), rightBottom.add(pos), squareColor);
    }

    //하위 도형들의 좌표를 계산.
    private void drawChildren(float interval, Dir dir, TileKind kind) {
        FloatPos center = new FloatPos(interval / 2).add(leftTop);

        //1. 위를 바라보는 상태 기준으로 계산. 2. 원점(0,0)을 현재 타일의 중앙으로 계산.
        Square[] childs = switch (kind) {
            case player -> drawEyes(interval);
            case tail -> drawTailDir(interval);
            default -> new Square[]{};
        };

        //1. 바라보는 방향에 맞게 좌표를 회전시킴. 2. 원점(0,0)을 타일 중앙에서 화면 좌상단으로 수정.
        for (int i = 0; i < childs.length; i++) {
            childs[i] = switch (dir) {
                case Up -> childs[i];
                case Down -> childs[i].flip(p -> new FloatPos(p.x, -p.y));
                case Left -> childs[i].flip(p -> new FloatPos(p.y, p.x));
                case Right -> childs[i].flip(p -> new FloatPos(-p.y, p.x));
            };

            childs[i] = childs[i].add(center);
        }

        children.addAll(Arrays.stream(childs).toList());
    }

    private Square[] drawTailDir(float interval) {
        FloatPos leftTop = new FloatPos(-interval / 8 * 1, -interval / 2);
        FloatPos rightBottom = new FloatPos(interval / 8 * 1, 0);

        return new Square[]{
                new Square(leftTop, rightBottom, SquareColor.White)
        };
    }

    private Square[] drawEyes(float interval) {
        float topAbs = interval / 8 * 3;
        float bottomAbs = interval / 8;
        float[] col = new float[]{-topAbs, -bottomAbs, bottomAbs, topAbs};

        FloatPos[] eyesPos = new FloatPos[]{
                new FloatPos(col[0], -topAbs),
                new FloatPos(col[1], -bottomAbs),
                new FloatPos(col[2], -topAbs),
                new FloatPos(col[3], -bottomAbs)
        };

        return new Square[]{
                new Square(eyesPos[0], eyesPos[1], SquareColor.White),
                new Square(eyesPos[2], eyesPos[3], SquareColor.White)
        };
    }
}
