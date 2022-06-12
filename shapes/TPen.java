package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TPen extends TShape {

    private ArrayList<Point2D> points;

    public TPen() {
        points = new ArrayList<>();
        this.shape = new Rectangle();
    }

    @Override
    public TShape clone() {
        return new TPen();
    }

    @Override
    public void prepareDrawing(int x, int y) {
        points.add(new Point2D.Double(x, y));
    }

    @Override
    public void resize(int x, int y) {
        points.add(new Point2D.Double(x, y));
        setRectangle();
    }

    public void setRectangle() {
        int minX = 9999;
        int minY = 9999;
        int maxX = -9999;
        int maxY = -9999;

        for (Point2D point : this.points) {

            if (point.getX() < minX) {
                minX = (int) point.getX();
            }

            if (point.getY() < minY) {
                minY = (int) point.getY();
            }

            if (point.getX()> maxX) {
                maxX = (int) point.getX();
            }

            if (point.getY()> maxY) {
                maxY = (int) point.getY();
            }
        }
        Rectangle rectangle = (Rectangle) this.shape;
        rectangle.setFrame(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public void transformShape(AffineTransform affineTransform) {
        for (Point2D point : points) {
            int index = points.indexOf(point);
            Point2D changePoint = affineTransform.transform(point, new Point2D.Double());
            points.set(index, changePoint);
        }

    }

    @Override
    public void draw(Graphics2D graphics) {
        graphics.setColor(this.lineColor);
        graphics.setStroke(new BasicStroke(strokeValue, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.7f));
        for (int i = 0; i < points.size()-1; i++) {
            graphics.drawLine((int) points.get(i).getX(), (int) points.get(i).getY(), (int) points.get(i+1).getX(), (int) points.get(i+1).getY());
        }

    }
}
