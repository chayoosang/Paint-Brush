package transformer;

import frames.DrawingPanel;
import shapes.TShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Vector;

public class Rotator extends Transformer {

    public Rotator(TShape selectShape, Vector<TShape> selectShapes) {
        super(selectShape, selectShapes);
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
    public void keep(int x, int y, Graphics2D graphics, Image image) {

        double rotateAngle = computeAngle(this.center, this.previous, new Point2D.Double(x, y));
        if (!this.selectShapes.isEmpty()) {
            for (TShape shape : this.selectShapes) {
                AffineTransform affineTransform = new AffineTransform();
                AffineTransform anchorAffineTransform = new AffineTransform();
                shape.draw(graphics);
                this.center.setLocation(
                        shape.getCenterX(),
                        shape.getCenterY()
                );
                shape.addAngle(rotateAngle);
                affineTransform.setToRotation(Math.toRadians(rotateAngle), center.getX(), center.getY());
                anchorAffineTransform.setToRotation(Math.toRadians(rotateAngle), center.getX()-5, center.getY()-5);
                shape.transformShape(affineTransform);
                shape.transformAnchor(anchorAffineTransform);
                shape.draw(graphics);
            }
        } else {
            AffineTransform affineTransform = new AffineTransform();
            AffineTransform anchorAffineTransform = new AffineTransform();
            this.selectShape.draw(graphics);
            this.selectShape.addAngle(rotateAngle);
            affineTransform.setToRotation(Math.toRadians(rotateAngle), center.getX(), center.getY());
            anchorAffineTransform.setToRotation(Math.toRadians(rotateAngle), center.getX()-5, center.getY()-5);
            this.selectShape.transformShape(affineTransform);
            this.selectShape.transformAnchor(anchorAffineTransform);
            this.selectShape.draw(graphics);

        }

        this.previous.setLocation(x, y);
        graphics.drawImage(image, 0, 0, null);
    }

    @Override
    public void finish(int x, int y, Graphics2D graphics2D, Vector<TShape> shapes, Vector<DrawingPanel.TimeShape> timeShapes) {


        if (!this.selectShapes.isEmpty()) {
            for (TShape shape : this.selectShapes) {
                int index = shapes.indexOf(shape);
                shapes.set(index, shape);
                timeShapes.add(new DrawingPanel.TimeShape(this.selectShape));
            }
        } else {
            int index = shapes.indexOf(this.selectShape);
            shapes.set(index, this.selectShape);
            timeShapes.add(new DrawingPanel.TimeShape(this.selectShape));
        }



    }
}
