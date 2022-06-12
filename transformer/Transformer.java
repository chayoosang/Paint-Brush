package transformer;

import frames.DrawingPanel;
import frames.DrawingPanel.TimeShape;
import shapes.TShape;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Vector;

public abstract class Transformer {

    protected TShape selectShape;
    protected Vector<TShape> selectShapes;
    protected Point2D previous, center;

    public Transformer(TShape selectShape, Vector<TShape> selectShapes) {
        this.selectShape = selectShape;
        this.previous = new Point2D.Double();
        this.center = new Point2D.Double();
        this.selectShapes = selectShapes;
    }

    public abstract void prepare(int x, int y, Graphics2D graphics);

    public abstract void keep(int x, int y, Graphics2D graphics, Image image);

    public abstract void finish(int x, int y, Graphics2D graphics2D, Vector<TShape> shapes, Vector<TimeShape> timeShapes);

    public void continueTransform(int x, int y, Graphics2D graphics2D) {
    }


}
