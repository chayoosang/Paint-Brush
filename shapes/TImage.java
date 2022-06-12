package shapes;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class TImage extends TShape {

    private ImageIcon image;
    private int preX, preY;
    private boolean first;

    public TImage() {
        this.shape = new Rectangle();
        this.first = true;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    @Override
    public TShape clone() {
        return new TImage();
    }

    @Override
    public void prepareDrawing(int x, int y) {
        Rectangle rectangle = (Rectangle) this.shape;
        rectangle.setFrame(x, y, 0, 0);

        this.preX = x;
        this.preY = y;
    }

    @Override
    public void resize(int x, int y) {
        Rectangle image = (Rectangle) this.shape;
        if (this.preX > x || this.preY > y) {
            if (this.preX > x && this.preY <= y) {
                image.setLocation(x, (int) image.getY());
                image.setSize(Math.abs(x - preX), y - (int) image.getY());
            } else if (this.preX <= x && this.preY > y) {
                image.setLocation((int) image.getX(), y);
                image.setSize(x - (int) image.getX(), Math.abs(y - preY));
            } else {
                image.setLocation(x, y);
                image.setSize(Math.abs(x - preX), Math.abs(y - preY));
            }
        } else {
            image.setSize(x - (int) image.getX(), y - (int) image.getY());
        }
    }

    public void transformShape(AffineTransform affineTransform) {
        this.shape = affineTransform.createTransformedShape(this.shape);
//          this.affineTransform.concatenate(affineTransform);
    }



    @Override
    public void draw(Graphics2D graphics) {

        graphics.drawImage(this.image.getImage(), (int) this.affineTransform.createTransformedShape(this.shape).getBounds2D().getX(),
                (int) this.affineTransform.createTransformedShape(this.shape).getBounds2D().getY(),
                (int) this.affineTransform.createTransformedShape(this.shape).getBounds2D().getWidth(),
                (int) this.affineTransform.createTransformedShape(this.shape).getBounds2D().getHeight(), null);

    }
}
