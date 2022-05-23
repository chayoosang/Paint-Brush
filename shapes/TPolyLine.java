package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.nio.channels.Pipe;
import java.util.ArrayList;

public class TPolyLine extends TShape{
    private  final int MAX_POINTS = 100;

    private int[] xPoints, yPoints;
    private ArrayList<Integer> sortX, sortY;
    private ArrayList<Point2D> prePoints, curPoints;
    private int nPoints;


    public TPolyLine() {
        this.shape = new Rectangle();
        this.sortX = new ArrayList<>();
        this.sortY = new ArrayList<>();
        this.prePoints = new ArrayList<>();
        this.curPoints = new ArrayList<>();
    }

    @Override
    public TShape clone() {
        return new TPolyLine();
    }

    @Override
    public void prepareDrawing(int x, int y){
        this.nPoints = 0;

        this.xPoints = new int[MAX_POINTS];
        this.yPoints = new int[MAX_POINTS];

        this.addPoint(x,y);
        this.addPoint(x,y);
    }


    @Override
    public void addPoint(int x, int y) {
        this.xPoints[nPoints] = x;
        this.yPoints[nPoints] = y;
        this.sortX.add(x);
        this.sortY.add(y);

        this.prePoints.add(new Point2D.Double(x, y));

        this.nPoints++;
    }

    @Override
    public void resize(int x, int y) {
        this.xPoints[nPoints-1] = x;
        this.yPoints[nPoints-1] = y;
    }

    public void transformShape(AffineTransform affineTransform) {
        super.transformShape(affineTransform);

        for (int i = 0; i < nPoints; i++) {
            Point2D curPoint = affineTransform.transform(prePoints.get(i), new Point2D.Double());
            xPoints[i] = (int) curPoint.getX();
            yPoints[i] = (int) curPoint.getY();
            prePoints.set(i, curPoint);
        }


    }



    @Override
    public void draw(Graphics2D graphics) {
        setRectangle();
        graphics.setColor(this.lineColor);
        graphics.setStroke(new BasicStroke(this.strokeValue, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.7f));
        graphics.drawPolyline(this.xPoints, this.yPoints, this.nPoints);
    }

    private void setRectangle() {
        this.sort();
        int x = sortX.get(0);
        int y = sortY.get(0);
        int w = sortX.get(sortX.size()-1) - sortX.get(0);
        int h = sortY.get(sortX.size()-1) - sortY.get(0);
        this.shape = new Rectangle(x, y, w, h);
    }

    private void sort() {
        this.sortX.sort(null);
        this.sortY.sort(null);
    }






}
