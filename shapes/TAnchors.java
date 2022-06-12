package shapes;

import global.Constants.EAnchors;

import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;
import java.util.Vector;

public class TAnchors implements Serializable {

    static final int ANCHOR_W = 10;
    static final int ANCHOR_H = 10;

    private Vector<Ellipse2D.Double> anchors;
    private EAnchors eSelectedAnchor;
    private Vector<Point2D> prePoint;

    private AffineTransform affineTransform;
    private boolean first;
    private boolean line;

    @SuppressWarnings("unused")
    public TAnchors() {
        this.anchors = new Vector<>();
        for (EAnchors eAnchor : EAnchors.values()) {
            this.anchors.add(new Ellipse2D.Double(0, 0, ANCHOR_W, ANCHOR_H));
        }
        this.eSelectedAnchor = null;
        this.first = true;
        this.prePoint = new Vector<>();
        this.affineTransform = new AffineTransform();
        this.affineTransform.setToIdentity();
        this.line = false;
    }

    public boolean isLine() {
        return line;
    }

    public void setLine(boolean line) {
        this.line = line;
    }

    public EAnchors getSelectedAnchor() {
        return this.eSelectedAnchor;
    }

    public void setSelectedAnchor(EAnchors eSelectedAnchor) {
        this.eSelectedAnchor = eSelectedAnchor;
    }

    public void setChangePoint() {
        for (Ellipse2D anchor : anchors) {
            this.prePoint.add(affineTransform.transform(new Point2D.Double(anchor.getX(), anchor.getY()), new Point2D.Double()));
        }
    }

    public Vector<Point2D> getPrePoint() {
        return prePoint;
    }

    public void computeCoordinate(Rectangle2D r) {
        for (EAnchors eAnchors : EAnchors.values()) {
            eAnchors.setLine(r.getBounds());
            this.anchors.get(eAnchors.ordinal()).setFrame(eAnchors.getLine());
        }

        this.first = true;
    }

    public void transformShape(AffineTransform affineTransform) {
        Dimension2D dimension = new Dimension(ANCHOR_W, ANCHOR_H);

        for (Ellipse2D anchor : anchors) {
            anchor.setFrame(affineTransform.transform(new Point2D.Double(anchor.getX(), anchor.getY()), new Point2D.Double()), dimension);
        }
//        this.affineTransform.concatenate(affineTransform);
    }


    public void draw(Graphics2D graphics, Rectangle2D rectangle) {
        if (this.first == true) {
            computeCoordinate(rectangle);
            this.first = false;
        }

        if (this.line != true) {
            for (Ellipse2D.Double anchor : this.anchors) {
                graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.7f));
                graphics.setColor(Color.white);
                graphics.fill(anchor);
                graphics.setColor(Color.black);
                graphics.draw(anchor);
            }
        } else {
            for (int i = 0; i < this.anchors.size(); i++) {
                if (i == EAnchors.eNW.ordinal() || i == EAnchors.eSE.ordinal() || i == EAnchors.eRR.ordinal()) {
                    graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.7f));
                    graphics.setColor(Color.white);
                    graphics.fill(anchors.get(i));
                    graphics.setColor(Color.black);
                    graphics.draw(anchors.get(i));
                }
            }
        }


    }


    public boolean anchorContains(int x, int y) {
        if (this.line != true) {
            for (int i = 0; i < anchors.size(); i++) {
                if (this.affineTransform.createTransformedShape(anchors.get(i)).contains(x, y)) {
                    this.eSelectedAnchor = EAnchors.values()[i];
                    return true;
                }
            }
            this.eSelectedAnchor = null;
            return false;
        } else {
            for (int i = 0; i < anchors.size(); i++) {
                if (i == EAnchors.eNW.ordinal() || i == EAnchors.eSE.ordinal() || i == EAnchors.eRR.ordinal()) {
                    if (this.affineTransform.createTransformedShape(anchors.get(i)).contains(x, y)) {
                        this.eSelectedAnchor = EAnchors.values()[i];
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public Vector<Ellipse2D.Double> getAnchors() {
        return anchors;
    }


    public Vector<Shape> getChangeAnchors() {
        Vector<Shape> changAnchors = new Vector<>();
        for (Ellipse2D anchor : anchors) {
            changAnchors.add(this.affineTransform.createTransformedShape(anchor));
        }
        return changAnchors;
    }
}
