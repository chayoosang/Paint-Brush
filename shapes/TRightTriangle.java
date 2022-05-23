package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class TRightTriangle extends TShape {

    private final int MAX_POINT = 3;
    private int preX, preY;
    private int[] xPoint, yPoint;
    private Polygon rightTriangle;
    private boolean first;
    private Point2D curP1, curP2, curP3, preP1, preP2, preP3;

    public TRightTriangle() {
        this.shape = new Rectangle();
        this.xPoint = new int[MAX_POINT];
        this.yPoint = new int[MAX_POINT];
        this.curP1 = new Point2D.Double();
        this.curP2 = new Point2D.Double();
        this.curP3 = new Point2D.Double();
    }
    @Override
    public TShape clone() {
        return new TRightTriangle();
    }


    @Override
    public void prepareDrawing(int x, int y) {
        Rectangle rightTriangle = (Rectangle) this.shape;
        rightTriangle.setFrame(x, y ,0, 0);

        this.preX = x;
        this.preY = y;

        this.first = true;
    }

    private void setPoint() {
        Rectangle rectangle = (Rectangle) this.shape;
        xPoint[0] = (int) rectangle.getX();
        yPoint[0] = (int) rectangle.getY();
        xPoint[1] = (int) rectangle.getX();
        yPoint[1] = (int) (rectangle.getY() + rectangle.getHeight());
        xPoint[2] = (int) (rectangle.getX() + rectangle.getWidth());
        yPoint[2] = (int) (rectangle.getY() + rectangle.getHeight());

        this.preP1 = new Point2D.Double(xPoint[0], yPoint[0]);
        this.preP2 = new Point2D.Double(xPoint[1], yPoint[1]);
        this.preP3 = new Point2D.Double(xPoint[2], yPoint[2]);

        this.rightTriangle = new Polygon(xPoint, yPoint, MAX_POINT);
    }


    public void transformShape(AffineTransform affineTransform) {
        super.transformShape(affineTransform);

        affineTransform.transform(preP1, curP1);
        affineTransform.transform(preP2, curP2);
        affineTransform.transform(preP3, curP3);


        xPoint[0] = (int) curP1.getX();
        yPoint[0] = (int) curP1.getY();

        xPoint[1] = (int) curP2.getX();
        yPoint[1] = (int) curP2.getY();

        xPoint[2] = (int) curP3.getX();
        yPoint[2] = (int) curP3.getY();

        this.rightTriangle = new Polygon(xPoint, yPoint, MAX_POINT);
        preP1 = curP1;
        preP2 = curP2;
        preP3 = curP3;
    }




    @Override
    public void resize(int x, int y) {
        Rectangle rightTriangle = (Rectangle) this.shape;
        if (this.preX > x || this.preY > y) {
            if (this.preX > x && this.preY <= y) {
                rightTriangle.setLocation(x, (int) rightTriangle.getY());
                rightTriangle.setSize(Math.abs(x - preX), y - (int)rightTriangle.getY());
            } else if (this.preX <= x && this.preY > y) {
                rightTriangle.setLocation((int) rightTriangle.getX(), y);
                rightTriangle.setSize(x - (int) rightTriangle.getX(), Math.abs(y - preY));
            } else {
                rightTriangle.setLocation(x, y);
                rightTriangle.setSize(Math.abs(x - preX), Math.abs(y - preY));
            }
        } else {
            rightTriangle.setSize( x - (int)rightTriangle.getX(), y - (int)rightTriangle.getY());
        }
        setPoint();
        this.first = true;
    }

    @Override
    public void draw(Graphics2D graphics) {
        if(first == true){
            this.setPoint();
            this.first = false;
        }
        graphics.setColor(this.lineColor);
        graphics.setStroke(new BasicStroke(this.strokeValue, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.7f));
        graphics.draw(this.rightTriangle);

    }






}
