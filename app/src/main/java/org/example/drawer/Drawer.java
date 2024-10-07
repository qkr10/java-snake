package org.example.drawer;

import java.util.ArrayList;
import java.util.List;
import org.example.common.FloatPos;
import org.example.drawer.shape.Line;
import org.example.drawer.shape.Square;
import org.example.game.Game;
import org.example.common.Pos;
import org.example.drawer.shape.Line.LineStyle;
import org.example.drawer.shape.Square.SquareColor;
import org.example.common.TileToUpdate;

/**
 * 화면을 그리는데 필요한 도형들을 전부 계산해서 gridLines 변수와 squares 변수에 저장한다.
 * 실제 출력은 하지 않는다.
 */
public class Drawer {

    public static final Pos drawableSize = new Pos(900, 700);
    public static final int padding = 10;

    public List<Line> gridLines = new ArrayList<>();
    public List<Square> squares = new ArrayList<>();

    private final List<TileToUpdate> tileUpdateQueue;

    private FloatPos center, gridLeftTop, gridRightBottom;
    private float smallSideLength, gridSize;
    private float gridInterval;
    private Square grid;

    public Drawer(List<TileToUpdate> tileUpdateQueue) {
        this.tileUpdateQueue = tileUpdateQueue;

        drawerInit();
        gridDraw();
    }

    public void squareDraw() {
        for (TileToUpdate tile : tileUpdateQueue) {
            var square = new Square(grid, gridInterval, tile, tile.dir);
            squares.add(square);
        }

        tileUpdateQueue.clear();
    }

    private void drawerInit() {
        center = new FloatPos(drawableSize).div(2);
        smallSideLength = Math.min(drawableSize.x, drawableSize.y);
        gridSize = smallSideLength - padding * 2;
        gridLeftTop = center.sub(new FloatPos(gridSize / 2));
        gridRightBottom = new FloatPos(gridSize).add(gridLeftTop);
        grid = new Square(gridLeftTop, gridRightBottom, SquareColor.White);
        gridInterval = gridSize / Game.mapSize;
    }

    private void gridDraw() {
        gridLines.clear();

        gridLines.add(new Line(gridLeftTop, grid.rightTop, LineStyle.Thick));
        gridLines.add(new Line(gridLeftTop, grid.leftBottom, LineStyle.Thick));
        gridLines.add(new Line(gridRightBottom, grid.rightTop, LineStyle.Thick));
        gridLines.add(new Line(gridRightBottom, grid.leftBottom, LineStyle.Thick));

        for (int i = 1; i < Game.mapSize; i++) {
            float interval = i * gridInterval;

            FloatPos colStart = gridLeftTop.add(new FloatPos(interval, 0));
            FloatPos colEnd = gridLeftTop.add(new FloatPos(interval, gridSize));
            gridLines.add(new Line(colStart, colEnd, LineStyle.Thin));

            FloatPos rowStart = gridLeftTop.add(new FloatPos(0, interval));
            FloatPos rowEnd = gridLeftTop.add(new FloatPos(gridSize, interval));
            gridLines.add(new Line(rowStart, rowEnd, LineStyle.Thin));
        }
    }
}
