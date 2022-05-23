package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class TPentagon extends TShape {

    private final int MAX_POINT = 5;
    private int preX, preY;
    private int[] xPoint, yPoint;

    private Point2D pre1, pre2, pre3, pre4, pre5;
    private Point2D cur1, cur2, cur3, cur4, cur5;
    private Polygon pentagon;
    private boolean first;

    public TPentagon() {
        this.shape = new Rectangle();
        this.xPoint = new int[MAX_POINT];
        this.yPoint = new int[MAX_POINT];

        this.cur1 = new Point2D.Double();
        this.cur2 = new Point2D.Double();
        this.cur3 = new Point2D.Double();
        this.cur4 = new Point2D.Double();
        this.cur5 = new Point2D.Double();

        this.first = true;
    }

    @Override
    public TShape clone() {
        return new TPentagon();
    }

    @Override
    public void prepareDrawing(int x, int y) {
        Rectangle rectangle = (Rectangle) this.shape;
        rectangle.setFrame(x, y ,0, 0);

        this.preX = x;
        this.preY = y;
    }

    private void setPoint() {
        Rectangle rectangle = (Rectangle) this.shape;
        xPoint[0] = (int) rectangle.getX();
        yPoint[0] = (int) (rectangle.getY() + rectangle.getHeight() / 3);
        xPoint[1] = (int) (rectangle.getX() + rectangle.getWidth() / 2);
        yPoint[1] = (int) (rectangle.getY());
        xPoint[2] = (int) (rectangle.getX() + rectangle.getWidth());
        yPoint[2] = (int) (rectangle.getY() + rectangle.getHeight() / 3);
        xPoint[3] = (int) (rectangle.getX() + 3 * rectangle.getWidth() / 4);
        yPoint[3] = (int) (rectangle.getY() + rectangle.getHeight());
        xPoint[4] = (int) (rectangle.getX() + rectangle.getWidth() / 4);
        yPoint[4] = (int) (rectangle.getY() + rectangle.getHeight());


        this.pre1 = new Point2D.Double(xPoint[0], yPoint[0]);
        this.pre2 = new Point2D.Double(xPoint[1], yPoint[1]);
        this.pre3 = new Point2D.Double(xPoint[2], yPoint[2]);
        this.pre4 = new Point2D.Double(xPoint[3], yPoint[3]);
        this.pre5 = new Point2D.Double(xPoint[4], yPoint[4]);

        this.pentagon = new Polygon(xPoint, yPoint, MAX_POINT);
    }


    public void transformShape(AffineTransform affineTransform) {
        super.transformShape(affineTransform);

        affineTransform.transform(pre1, cur1);
        affineTransform.transform(pre2, cur2);
        affineTransform.transform(pre3, cur3);
        affineTransform.transform(pre4, cur4);
        affineTransform.transform(pre5, cur5);


        xPoint[0] = (int) cur1.getX();
        yPoint[0] = (int) cur1.getY();

        xPoint[1] = (int) cur2.getX();
        yPoint[1] = (int) cur2.getY();

        xPoint[2] = (int) cur3.getX();
        yPoint[2] = (int) cur3.getY();

        xPoint[3] = (int) cur4.getX();
        yPoint[3] = (int) cur4.getY();

        xPoint[4] = (int) cur5.getX();
        yPoint[4] = (int) cur5.getY();



        this.pentagon = new Polygon(xPoint, yPoint, MAX_POINT);
        pre1 = cur1;
        pre2 = cur2;
        pre3 = cur3;
        pre4 = cur4;
        pre5 = cur5;
    }



    @Override
    public void resize(int x, int y) {
        Rectangle rectangle = (Rectangle) this.shape;
        if (this.preX > x || this.preY > y) {
            if (this.preX > x && this.preY <= y) {
                rectangle.setLocation(x, (int) rectangle.getY());
                rectangle.setSize(Math.abs(x - preX), y - (int)rectangle.getY());
            } else if (this.preX <= x && this.preY > y) {
                rectangle.setLocation((int) rectangle.getX(), y);
                rectangle.setSize(x - (int) rectangle.getX(), Math.abs(y - preY));
            } else {
                rectangle.setLocation(x, y);
                rectangle.setSize(Math.abs(x - preX), Math.abs(y - preY));
            }
        } else {
            rectangle.setSize( x - (int)rectangle.getX(), y - (int)rectangle.getY());
        }
        setPoint();
    }


    @Override
    public void draw(Graphics2D graphics) {
        if (first == true) {
            this.setPoint();
            this.first = false;
        }
        graphics.setColor(this.lineColor);
        graphics.setStroke(new BasicStroke(this.strokeValue, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.7f));
        graphics.draw(this.pentagon);
        if (this.fillColor != null) {
            graphics.setColor(fillColor);
            graphics.fill(this.pentagon);
        };
    }





}
