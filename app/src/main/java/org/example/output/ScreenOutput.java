package org.example.output;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.example.common.Pos;
import org.example.drawer.Drawer;
import org.example.drawer.shape.Line;
import org.example.drawer.shape.Line.LineStyle;
import org.example.drawer.shape.Square;
import org.example.drawer.shape.Square.SquareColor;

public class ScreenOutput extends JPanel implements ActionListener {

    public final Pos drawableSize;
    public final JFrame frame;

    private Drawer drawer;
    private Stroke thin, thick;
    private Image gridBuffer, squareBuffer;
    private Graphics2D gridGraphics, squareGraphics;

    public ScreenOutput(Drawer drawer) {
        this.drawer = drawer;

        // 창 생성
        frame = new JFrame("Simple 2D Game");

        frame.add(this);
        frame.pack();
        var insets = frame.getInsets();
        frame.setSize(
                Drawer.drawableSize.x + insets.left + insets.right,
                Drawer.drawableSize.y + insets.top + insets.bottom
        );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        drawableSize = new Pos(Drawer.drawableSize.x, Drawer.drawableSize.y);

        Timer timer = new Timer(1000 / 60, this);
        timer.start();

        paintInit();
        paintLinesToBuffer(gridGraphics);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        paintSquares(squareGraphics);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(squareBuffer, 0, 0, null);
        g2d.drawImage(gridBuffer, 0, 0, null);
    }

    private void paintLinesToBuffer(Graphics2D g2d) {
        g2d.setColor(Color.black);
        for (Line line : drawer.gridLines) {
            g2d.setStroke(getPaint(line.lineStyle));
            Pos s = line.s.getPos(), e = line.e.getPos();
            g2d.drawLine(s.x, s.y, e.x, e.y);
        }
    }

    private void paintSquares(Graphics2D g2d) {
        drawer.squareDraw();

        for (int i = 0; i < drawer.squares.size(); i++) {
            Square square = drawer.squares.get(i);
            drawer.squares.addAll(square.children);

            Color color = getPaint(square.squareColor);
            Pos leftTop = square.leftTop.getPos();
            Pos rightBottom = square.rightBottom.getPos();
            Pos size = rightBottom.sub(leftTop);

            if (color == null) {
                g2d.setComposite(AlphaComposite.Clear);
                g2d.fillRect(leftTop.x, leftTop.y, size.x, size.y);
                g2d.setComposite(AlphaComposite.SrcOver);
            }
            if (color != null) {
                g2d.setColor(color);
                g2d.fillRect(leftTop.x, leftTop.y, size.x, size.y);
            }
        }

        drawer.squares.clear();
    }

    private void paintInit() {
        squareBuffer = new BufferedImage(drawableSize.x, drawableSize.y, BufferedImage.TYPE_INT_ARGB);
        squareGraphics = (Graphics2D) squareBuffer.getGraphics();
        squareGraphics.setColor(Color.white);
        squareGraphics.fillRect(0, 0, drawableSize.x, drawableSize.y);

        gridBuffer = new BufferedImage(drawableSize.x, drawableSize.y, BufferedImage.TYPE_INT_ARGB);
        gridGraphics = (Graphics2D) gridBuffer.getGraphics();

        var g2d = gridGraphics;
        g2d.setStroke(new BasicStroke(1));
        thin = g2d.getStroke();
        g2d.setStroke(new BasicStroke(4));
        thick = g2d.getStroke();
    }

    private Color getPaint(SquareColor color) {
        return switch (color) {
            case Red -> Color.red;
            case Blue -> Color.blue;
            case Orange -> Color.orange;
            case Gray -> Color.gray;
            case White -> Color.white;
        };
    }

    private Stroke getPaint(LineStyle style) {
        return switch (style) {
            case Thin -> thin;
            case Thick -> thick;
        };
    }
}
