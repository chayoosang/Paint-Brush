package shapes;

import global.Constants.EAnchors;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.Vector;


public abstract class TShape implements Serializable, Cloneable {

     protected AffineTransform affineTransform;

     protected Shape shape;
     protected TAnchors anchors;
     protected Color lineColor, fillColor;
     protected int strokeValue;
     private double angle;
     private boolean selected;
     private boolean first;

     public TShape() {
          this.lineColor = Color.BLACK;
          this.fillColor = null;
          this.selected = false;
          this.affineTransform = new AffineTransform();
          this.affineTransform.setToIdentity();
          this.anchors = new TAnchors();
          this.angle = 0;
          this.first = true;
     }

     public Color getLineColor() {
          return lineColor;
     }

     public void setLineColor(Color lineColor) {
          this.lineColor = lineColor;
     }

     public int getStrokeValue() {
          return strokeValue;
     }

     public void setStrokeValue(int strokeValue) {
          this.strokeValue = strokeValue;
     }

     public Shape getShape() {
          return shape;
     }

     public void setShape(Shape shape) {
          this.shape = shape;
     }

     public double getCenterX() {
          return this.shape.getBounds2D().getCenterX();
     }
     public double getCenterY() {
          return this.shape.getBounds2D().getCenterY();
     }

     public double getChangeCenterX() {
          return affineTransform.createTransformedShape(this.shape).getBounds2D().getCenterX();
     }

     public double getChangeCenterY() {
          return affineTransform.createTransformedShape(this.shape).getBounds2D().getCenterY();
     }

     public boolean isSelected() {
          return selected;
     }

     public void setSelected(boolean selected) {
          this.selected = selected;
     }

     public void setSelectAnchors(EAnchors eAnchors) {
          this.anchors.setSelectedAnchor(eAnchors);
     }
     public EAnchors getSelectAnchors() {
          return this.anchors.getSelectedAnchor();
     }

     public Vector<Ellipse2D.Double> getAnchors() {
          return this.anchors.getAnchors();
     }

     public Vector<Shape> getChangeAnchors() {
          return this.anchors.getChangeAnchors();
     }

     public Shape getChangeShape() {
          return this.affineTransform.createTransformedShape(this.shape);
     }

     public Color getFillColor() {
          return fillColor;
     }

     public void setFillColor(Color fillColor) {
          this.fillColor = fillColor;
     }

     public boolean isFirst() {
          return first;
     }

     public void setFirst(boolean first) {
          this.first = first;
     }

     public AffineTransform getAffineTransform() {
          return affineTransform;
     }

     public void addAngle(double angle) {
          this.angle +=angle;
          if (this.angle >= 360) {
               this.angle -= 360;
          }
     }

     public double getAngle() {
          return angle;
     }

     public abstract TShape clone();

     public abstract void prepareDrawing(int x, int y);
     public void addPoint(int x, int y) {
     }
     public abstract void resize(int x, int y);



     public void transformShape(AffineTransform affineTransform) {
          this.shape = affineTransform.createTransformedShape(this.shape);
//          this.affineTransform.concatenate(affineTransform);
     }


     public void transformAnchor(AffineTransform affineTransform) {
          this.anchors.transformShape(affineTransform);

     }


     public boolean contains(int x, int y) {
          if (this.anchors.anchorContains(x, y)) {
               return true;
          }
          return this.affineTransform.createTransformedShape(this.shape).contains(x, y);
     }
     public void draw(Graphics2D graphics){

          graphics.setColor(this.lineColor);
          graphics.setStroke(new BasicStroke(strokeValue, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.7f));
          graphics.draw(this.shape);
          if (this.fillColor != null) {
               graphics.setColor(this.fillColor);
               graphics.fill(this.shape);
          }

     }

     public void drawAnchors(Graphics2D graphics) {
          this.anchors.draw(graphics, this.shape.getBounds2D());
     }

     public Rectangle getBounds() {
          return this.shape.getBounds();
     }







}
