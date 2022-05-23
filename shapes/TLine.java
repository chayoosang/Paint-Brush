package shapes;

import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class TLine extends TShape {

    private int x1, y1;

    public TLine() {
        this.shape = new Line2D.Double();
    }

    @Override
    public void prepareDrawing(int x, int y) {
        this.x1 = x;
        this.y1 = y;
    }


    @Override
    public void resize(int x, int y) {
        Line2D.Double line = (Line2D.Double) this.shape;
        line.setLine(this.x1, this.y1, x, y);
    }


    @Override
    public TShape clone() {
        return new TLine();
    }





}
