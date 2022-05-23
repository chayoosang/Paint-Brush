package transformer;

import global.Constants.EAnchors;
import shapes.TShape;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Vector;

public abstract class Transformer {

    protected TShape selectShape;
    protected Point2D previous, center;

    public Transformer(TShape selectShape) {
        this.selectShape = selectShape;
        this.previous = new Point2D.Double();
        this.center = new Point2D.Double();
    }

    public abstract void prepare(int x, int y, Graphics2D graphics);

    public abstract void keep(int x, int y, Graphics2D graphics, Image image, Cursor cursor);

    public abstract void finish(int x, int y, Graphics2D graphics2D, Vector<TShape> shapes);

    public void continueTransform(int x, int y, Graphics2D graphics2D) {
    }


}
