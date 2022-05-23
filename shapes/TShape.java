package shapes;

import global.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.Vector;


public abstract class TShape implements Serializable {

     protected AffineTransform affineTransform;

     protected Shape shape;
     private TAnchors anchors;
     protected Color lineColor, fillColor;
     protected int strokeValue;

     private boolean selected;
     protected int selectX, selectY, centerX, centerY;

     public TShape() {
          this.lineColor = Color.BLACK;
          this.fillColor = null;
          this.selected = false;
          this.anchors = new TAnchors();
          this.affineTransform = new AffineTransform();
          this.affineTransform.setToIdentity();
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

     public int getSelectX() {
          return selectX;
     }

     public void setSelectX(int selectX) {
          this.selectX = selectX;
     }

     public int getSelectY() {
          return selectY;
     }

     public void setSelectY(int selectY) {
          this.selectY = selectY;
     }

     public double getCenterX() {
          return this.shape.getBounds2D().getCenterX();
     }
     public double getCenterY() {
          return this.shape.getBounds2D().getCenterY();
     }

     public boolean isSelected() {
          return selected;
     }

     public void setSelected(boolean selected) {
          this.selected = selected;
     }

     public EAnchors getSelectAnchors() {
          return this.anchors.getSelectedAnchor();
     }

     public Vector<Ellipse2D.Double> getAnchors() {
          return this.anchors.getAnchors();
     }

     public Color getFillColor() {
          return fillColor;
     }

     public void setFillColor(Color fillColor) {
          this.fillColor = fillColor;
     }

     public abstract TShape clone();

     public abstract void prepareDrawing(int x, int y);
     public void addPoint(int x, int y) {
     }
     public abstract void resize(int x, int y);

     public Rectangle setLocation(int x, int y) {
          Rectangle rectangle = this.shape.getBounds();
          rectangle.setLocation(x, y);
          return rectangle;
     }

     public void transformShape(AffineTransform affineTransform) {
          this.shape = affineTransform.createTransformedShape(this.shape);
          this.anchors.transformShape(affineTransform);
     }

     public boolean contains(int x, int y) {
          if (this.anchors.contains(x, y)) {
               return true;
          }
          return this.shape.getBounds().contains(x, y);
     }
     public void draw(Graphics2D graphics){
          this.centerX = this.shape.getBounds().x + this.shape.getBounds().width / 2;
          this.centerY = this.shape.getBounds().y + this.shape.getBounds().height / 2;

          Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
          graphics.setColor(this.lineColor);
          graphics.setStroke(new BasicStroke(strokeValue, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.7f));
          graphics.draw(transformedShape);
          if (this.fillColor != null) {
               graphics.setColor(this.fillColor);
               graphics.fill(transformedShape);
          }
     }

     public void drawAnchors(Graphics2D graphics) {
          this.anchors.draw(graphics, this.shape.getBounds2D());
     }

     public Rectangle getBounds() {
          return this.shape.getBounds();
     }





}
