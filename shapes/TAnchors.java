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

    private boolean first;

    @SuppressWarnings("unused")
    public TAnchors() {
        this.anchors = new Vector<>();
        for (EAnchors eAnchor: EAnchors.values()) {
            this.anchors.add(new Ellipse2D.Double(0, 0, ANCHOR_W, ANCHOR_H));
        }
        this.eSelectedAnchor = null;
        this.first = true;
    }


    public EAnchors getSelectedAnchor(){
        return this.eSelectedAnchor;
    }

    private void computeCoordinate(Rectangle2D r) {
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
    }

    public void draw(Graphics2D g2D, Rectangle2D rectangle) {
        if (this.first == true) {
            computeCoordinate(rectangle);
            this.first = false;
        }
        for (Ellipse2D.Double anchor: this.anchors) {
            Color color = g2D.getColor();
            g2D.setColor(g2D.getBackground());
            g2D.fill(anchor);
            g2D.setColor(color);
            g2D.draw(anchor);
        }
    }

    public boolean contains(int x, int y) {
        for (int i = 0; i< anchors.size(); i++) {
            if (anchors.get(i).contains(x, y)) {
                this.eSelectedAnchor = EAnchors.values()[i];
                return true;
            }
        }
        this.eSelectedAnchor = null;
        return false;
    }


    public Vector<Ellipse2D.Double> getAnchors() {
        return anchors;
    }



}
