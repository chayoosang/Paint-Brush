package transformer;

import shapes.TShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Vector;

public class Rotator extends Transformer {

    public Rotator(TShape selectShape) {
        super(selectShape);
    }

    @Override
    public void prepare(int x, int y, Graphics2D graphics) {
        this.center.setLocation(
                this.selectShape.getCenterX(),
                this.selectShape.getCenterY()
        );
        this.previous.setLocation(x, y);
    }

    private double computeAngle(Point2D center, Point2D previous, Point2D current) {
        double statAngle = Math.toDegrees(
                Math.atan2(center.getX() - previous.getX(), center.getY() - previous.getY())
        );
        double endAngle = Math.toDegrees(
                Math.atan2(center.getX() - current.getX(), center.getY() - current.getY())
        );
        double angle = statAngle - endAngle;
        if(angle < 0 ) angle += 360;
        return angle;
    }
    @Override
    public void keep(int x, int y, Graphics2D graphics, Image image, Cursor cursor) {
        this.selectShape.draw(graphics);
        AffineTransform affineTransform = new AffineTransform();
        double rotateAngle = computeAngle(this.center, this.previous, new Point2D.Double(x, y));
        affineTransform.setToRotation(Math.toRadians(rotateAngle), center.getX(), center.getY());
        this.selectShape.transformShape(affineTransform);

        this.selectShape.draw(graphics);
        this.previous.setLocation(x, y);

        graphics.drawImage(image, 0, 0, null);
    }

    @Override
    public void finish(int x, int y, Graphics2D graphics2D, Vector<TShape> shapes) {

        int index = shapes.indexOf(this.selectShape);
		shapes.set(index, this.selectShape);
    }
}
