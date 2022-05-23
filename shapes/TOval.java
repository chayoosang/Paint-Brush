package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class TOval extends TShape {

    private int preX, preY;

    public TOval() {
        this.shape = new Ellipse2D.Double();
    }

    @Override
    public void prepareDrawing(int x, int y){
        Ellipse2D.Double oval = (Ellipse2D.Double) this.shape;
        oval.setFrame(x, y, 0, 0);

        this.preX = x;
        this.preY = y;
    }

    @Override
    public void resize(int x, int y) {
        Ellipse2D.Double oval = (Ellipse2D.Double) this.shape;
        if (this.preX > x || this.preY > y) {
            if (this.preX > x && this.preY <= y) {
                oval.setFrame(x, oval.getY(), Math.abs(x - preX), y - oval.getY());
            } else if (this.preX <= x && this.preY > y) {
                oval.setFrame( oval.getX(), y, x - oval.getX(), Math.abs(y - preY));
            } else {
                oval.setFrame(x, y, Math.abs(x - preX), Math.abs(y - preY));
            }
        } else {
            oval.setFrame(oval.getX(), oval .getY(), x - oval.getX(), y - oval.getY());
        }
    }


    @Override
    public TShape clone() {
        return new TOval();
    }







}
