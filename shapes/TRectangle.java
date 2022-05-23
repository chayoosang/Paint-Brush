package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class TRectangle extends TShape {

    private int preX, preY;
    private int centerX, centerY;
    public TRectangle() {
        this.shape = new Rectangle();
    }

    @Override
    public void prepareDrawing(int x, int y){
        Rectangle rectangle = (Rectangle) this.shape;
        rectangle.setFrame(x, y, 0, 0);


        this.preX = x;
        this.preY = y;
    }


    @Override
    public void resize(int x, int y) {
        Rectangle rectangle = (Rectangle) this.shape;
        if (this.preX > x || this.preY > y) {
            if (this.preX > x && this.preY <= y) {
                rectangle.setFrame(x, rectangle.getY(), Math.abs(x - preX), y - rectangle.getY());
            } else if (this.preX <= x && this.preY > y) {
                rectangle.setFrame( rectangle.getX(), y, x - rectangle.getX(), Math.abs(y - preY));
            } else {
                rectangle.setFrame(x, y, Math.abs(x - preX), Math.abs(y - preY));
            }
        } else {
            rectangle.setFrame(rectangle.getX(), rectangle.getY(), x - rectangle.getX(), y - rectangle.getY());
        }
        this.centerX = rectangle.x + rectangle.width / 2;
        this.centerY = rectangle.y + rectangle.height / 2;
    }


    @Override
    public TShape clone() {
        return new TRectangle();
    }




}