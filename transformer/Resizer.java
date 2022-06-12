package transformer;

import frames.DrawingPanel;
import shapes.TShape;
import global.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Vector;


public class Resizer extends Transformer {

    public Resizer(TShape selectShape, Vector<TShape> selectShapes) {
        super(selectShape, selectShapes);
    }

    @Override
    public void prepare(int x, int y, Graphics2D graphics) {
        this.previous.setLocation(x, y);
    }

    @Override
    public void keep(int x, int y, Graphics2D graphics, Image image) {

        Point2D scaleFactor = computeScaleFactor(this.previous, new Point2D.Double(x, y));

        if (!this.selectShapes.isEmpty()) {
            for (TShape shape : this.selectShapes) {
                AffineTransform affineTransform = new AffineTransform();
                shape.setSelectAnchors(this.selectShape.getSelectAnchors());
                shape.draw(graphics);
                Point2D resizeOrigin = this.getResizeAnchor(shape);
                affineTransform.translate(resizeOrigin.getX(), resizeOrigin.getY());
                affineTransform.scale(scaleFactor.getX(), scaleFactor.getY());
                affineTransform.translate(-resizeOrigin.getX(), -resizeOrigin.getY());

                shape.transformShape(affineTransform);
                shape.transformAnchor(affineTransform);
                shape.draw(graphics);
            }
        } else {
            AffineTransform affineTransform = new AffineTransform();
            this.selectShape.draw(graphics);
            Point2D resizeOrigin = this.getResizeAnchor(this.selectShape);
            affineTransform.translate(resizeOrigin.getX(), resizeOrigin.getY());
            affineTransform.scale(scaleFactor.getX(), scaleFactor.getY());
            affineTransform.translate(-resizeOrigin.getX(), -resizeOrigin.getY());
            this.selectShape.transformShape(affineTransform);
            this.selectShape.transformAnchor(affineTransform);
            this.selectShape.draw(graphics);
        }


        this.previous.setLocation(x, y);
        graphics.drawImage(image, 0, 0, null);
    }

    public Point2D computeScaleFactor(Point2D previous, Point2D current) {
        double px = previous.getX();
        double py = previous.getY();
        double cx = current.getX();
        double cy = current.getY();

        double pw = 0;
        double ph = 0;

        switch (this.selectShape.getSelectAnchors()) {
            case eNW:
                pw = -(cx - px);
                ph = -(cy - py);
                break;
            case eWW:
                pw = -(cx - px);
                ph = 0;
                break;
            case eSW:
                pw = -(cx - px);
                ph = cy - py;
                break;
            case eNN:
                pw = 0;
                ph = -(cy - py);
                break;
            case eSS:
                pw = 0;
                ph = cy - py;
                break;
            case eNE:
                pw = cx - px;
                ph = -(cy - py);
                break;
            case eEE:
                pw = cx - px;
                ph = 0;
                break;
            case eSE:
                pw = cx - px;
                ph = cy - py;
                break;
        }
        Shape changeShape = this.selectShape.getChangeShape();

        double cw = changeShape.getBounds().getWidth();
        double ch = changeShape.getBounds().getHeight();

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


    private Point getResizeAnchor(TShape shape) {
        Point resizeAnchor = new Point();

        if (shape.getSelectAnchors() == EAnchors.eNW) {
            resizeAnchor.setLocation(shape.getAnchors().get(EAnchors.eSE.ordinal()).getX(),
                    shape.getAnchors().get(EAnchors.eSE.ordinal()).getY());

        } else if (shape.getSelectAnchors() == EAnchors.eNE) {
            resizeAnchor.setLocation(shape.getAnchors().get(EAnchors.eSW.ordinal()).getX(),
                    shape.getAnchors().get(EAnchors.eSW.ordinal()).getY());

        }else if (shape.getSelectAnchors() == EAnchors.eNN) {
            resizeAnchor.setLocation(0, shape.getAnchors().get(EAnchors.eSS.ordinal()).getY());
        }else if (shape.getSelectAnchors() == EAnchors.eSS) {
            resizeAnchor.setLocation(0,shape.getAnchors().get(EAnchors.eNN.ordinal()).getY());

        }else if (shape.getSelectAnchors() == EAnchors.eWW) {
            resizeAnchor.setLocation(shape.getAnchors().get(EAnchors.eEE.ordinal()).getX(), 0);
        }else if (shape.getSelectAnchors() == EAnchors.eEE) {
            resizeAnchor.setLocation(shape.getAnchors().get(EAnchors.eWW.ordinal()).getX(),0);

        }else if (shape.getSelectAnchors() == EAnchors.eSW) {
            resizeAnchor.setLocation(shape.getAnchors().get(EAnchors.eNE.ordinal()).getX(),
                    shape.getAnchors().get(EAnchors.eNE.ordinal()).getY());

        }else if (shape.getSelectAnchors() == EAnchors.eSE) {
            resizeAnchor.setLocation(shape.getAnchors().get(EAnchors.eNW.ordinal()).getX(),
                    shape.getAnchors().get(EAnchors.eNW.ordinal()).getY());
        }


        return resizeAnchor;
    }



    @Override
    public void finish(int x, int y, Graphics2D graphics2D, Vector<TShape> shapes, Vector<DrawingPanel.TimeShape> timeShapes) {
        int index = shapes.indexOf(this.selectShape);
        shapes.set(index, this.selectShape);
        timeShapes.add(new DrawingPanel.TimeShape(this.selectShape));
    }
}
