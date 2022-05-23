package transformer;

import shapes.TShape;
import global.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Vector;


public class Resizer extends Transformer {

    public Resizer(TShape selectShape) {
        super(selectShape);
    }

    @Override
    public void prepare(int x, int y, Graphics2D graphics) {
        this.previous.setLocation(x, y);
    }

    @Override
    public void keep(int x, int y, Graphics2D graphics, Image image, Cursor cursor) {
        this.selectShape.draw(graphics);

        AffineTransform affineTransform = new AffineTransform();
        Point2D resizeOrigin = this.getResizeAnchor();
        affineTransform.translate(resizeOrigin.getX(), resizeOrigin.getY());
        Point2D scaleFactor = computeScaleFactor(this.previous, new Point2D.Double(x, y),cursor);
        affineTransform.scale(scaleFactor.getX(), scaleFactor.getY());
        affineTransform.translate(-resizeOrigin.getX(), -resizeOrigin.getY());

        this.selectShape.transformShape(affineTransform);

        this.selectShape.draw(graphics);
        this.previous.setLocation(x, y);
        graphics.drawImage(image, 0, 0, null);
    }

    public Point2D computeScaleFactor(Point2D previous, Point2D current, Cursor cursor) {
        double px = previous.getX();
        double py = previous.getY();
        double cx = current.getX();
        double cy = current.getY();

        double pw = 0;
        double ph = 0;

        switch (cursor.getType()) {
            case Cursor.NW_RESIZE_CURSOR:
                pw = -(cx - px);
                ph = -(cy - py);
                break;
            case Cursor.W_RESIZE_CURSOR:
                pw = -(cx - px);
                ph = 0;
                break;
            case Cursor.SW_RESIZE_CURSOR:
                pw = -(cx - px);
                ph = cy - py;
                break;
            case Cursor.N_RESIZE_CURSOR:
                pw = 0;
                ph = -(cy - py);
                break;
            case Cursor.S_RESIZE_CURSOR:
                pw = 0;
                ph = cy - py;
                break;
            case Cursor.NE_RESIZE_CURSOR:
                pw = cx - px;
                ph = -(cy - py);
                break;
            case Cursor.E_RESIZE_CURSOR:
                pw = cx - px;
                ph = 0;
                break;
            case Cursor.SE_RESIZE_CURSOR:
                pw = cx - px;
                ph = cy - py;
                break;
        }
        double cw = this.selectShape.getBounds().getWidth();
        double ch = this.selectShape.getBounds().getHeight();

        double xFactor = 1.0;
        if (cw > 0.0) {
            xFactor = (1.0 + pw / cw);
        }
        double yFactor = 1.0;
        if (ch > 0.0) {
            yFactor = (1.0 + ph / ch);
        }
        return new Point2D.Double(xFactor, yFactor);
    }


    private Point getResizeAnchor() {
        Point resizeAnchor = new Point();

        if (this.selectShape.getSelectAnchors() == EAnchors.eNW) {
            resizeAnchor.setLocation(this.selectShape.getAnchors().get(EAnchors.eSE.ordinal()).getX(),
                    this.selectShape.getAnchors().get(EAnchors.eSE.ordinal()).getY());

        } else if (this.selectShape.getSelectAnchors() == EAnchors.eNE) {
            resizeAnchor.setLocation(this.selectShape.getAnchors().get(EAnchors.eSW.ordinal()).getX(),
                    this.selectShape.getAnchors().get(EAnchors.eSW.ordinal()).getY());

        }else if (this.selectShape.getSelectAnchors() == EAnchors.eNN) {
            resizeAnchor.setLocation(0, this.selectShape.getAnchors().get(EAnchors.eSS.ordinal()).getY());
        }else if (this.selectShape.getSelectAnchors() == EAnchors.eSS) {
            resizeAnchor.setLocation(0,this.selectShape.getAnchors().get(EAnchors.eNN.ordinal()).getY());

        }else if (this.selectShape.getSelectAnchors() == EAnchors.eWW) {
            resizeAnchor.setLocation(this.selectShape.getAnchors().get(EAnchors.eEE.ordinal()).getX(), 0);
        }else if (this.selectShape.getSelectAnchors() == EAnchors.eEE) {
            resizeAnchor.setLocation(this.selectShape.getAnchors().get(EAnchors.eWW.ordinal()).getX(),0);

        }else if (this.selectShape.getSelectAnchors() == EAnchors.eSW) {
            resizeAnchor.setLocation(this.selectShape.getAnchors().get(EAnchors.eNE.ordinal()).getX(),
                    this.selectShape.getAnchors().get(EAnchors.eNE.ordinal()).getY());

        }else if (this.selectShape.getSelectAnchors() == EAnchors.eSE) {
            resizeAnchor.setLocation(this.selectShape.getAnchors().get(EAnchors.eNW.ordinal()).getX(),
                    this.selectShape.getAnchors().get(EAnchors.eNW.ordinal()).getY());
        }


        return resizeAnchor;
    }



    @Override
    public void finish(int x, int y, Graphics2D graphics2D, Vector<TShape> shapes) {
        int index = shapes.indexOf(this.selectShape);
        shapes.set(index, this.selectShape);
    }
}
