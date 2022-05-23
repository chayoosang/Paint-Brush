package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class TCurve extends TShape {
    private int count;
    private boolean finishPoint;

    private ArrayList<Point> pointX, pointY;



    public TCurve() {
        this.shape = new CubicCurve2D.Double();
        this.pointX = new ArrayList<>();
        this.pointY = new ArrayList<>();
    }

    public ArrayList<Point> getPointX() {
        return pointX;
    }

    public void setPointX(ArrayList<Point> pointX) {
        this.pointX = pointX;
    }

    public ArrayList<Point> getPointY() {
        return pointY;
    }

    public void setPointY(ArrayList<Point> pointY) {
        this.pointY = pointY;
    }

    @Override
    public void prepareDrawing(int x, int y) {
        this.count = 0;
        this.finishPoint = false;

        CubicCurve2D curve = (CubicCurve2D) this.shape;
        curve.setCurve(x, y, x, y, x, y, x, y);
        pointX.add(new Point("x1", x));
        pointY.add(new Point("y1", y));

    }


    @Override
    public void resize(int x, int y) {
        CubicCurve2D curve = (CubicCurve2D) this.shape;
        switch (count) {
            case 0:
                curve.setCurve(curve.getX1(), curve.getY1(), curve.getCtrlX1(), curve.getCtrlY1(), curve.getCtrlX2(), curve.getCtrlY2(), x, y);
                break;
            case 1:
                curve.setCurve(curve.getX1(), curve.getY1(), x, y, curve.getCtrlX2(), curve.getCtrlY2(), curve.getX2(), curve.getY2());
                break;
            case 2:
                curve.setCurve(curve.getX1(), curve.getY1(), curve.getCtrlX1(), curve.getCtrlY1(), x, y, curve.getX2(), curve.getY2());
                break;
        }
    }

    @Override
    public void addPoint(int x, int y) {
        CubicCurve2D curve = (CubicCurve2D) this.shape;
        switch (count) {
            case 0:
                curve.setCurve(curve.getX1(), curve.getY1(), curve.getCtrlX1(), curve.getCtrlY1(), curve.getCtrlX2(), curve.getCtrlY2(), x, y);
                pointX.add(new Point("x2", x));
                pointY.add(new Point("y2", y));
                count++;
                break;
            case 1:
                curve.setCurve(curve.getX1(), curve.getY1(), x, y, curve.getCtrlX2(), curve.getCtrlY2(), curve.getX2(), curve.getY2());
                pointX.add(new Point("ctrlX1", x));
                pointY.add(new Point("ctrlY1", y));
                count++;
                break;
            case 2:
                curve.setCurve(curve.getX1(), curve.getY1(), curve.getCtrlX1(), curve.getCtrlY1(), x, y, curve.getX2(), curve.getY2());
                pointX.add(new Point("ctrlX2", x));
                pointY.add(new Point("ctrlY2", y));
                count++;
                this.finishPoint = true;
                break;
        }
    }

    public boolean getFinishPoint() {
        return this.finishPoint;
    }


    @Override
    public TShape clone() {
        return new TCurve();
    }




    private void sortPoints() {
        Collections.sort(this.pointX);
        Collections.sort(this.pointY);
    }





    private class Point implements Comparable<Point>, Serializable {
        private String point;
        private int value;

        public Point(String point, int value) {
            this.point = point;
            this.value = value;
        }

        public String getPoint() {
            return point;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(Point point) {
            if (this.value < point.getValue()) {
                return -1;
            } else if (this.value > point.getValue()) {
                return 1;
            } else {
                return 0;
            }
        }

    }



}
