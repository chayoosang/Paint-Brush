package transformer;

import frames.DrawingPanel.TimeShape;
import shapes.TPolyLine;
import shapes.TShape;
import shapes.TTextBox;

import java.awt.*;
import java.util.Vector;

public class Drawer extends Transformer {

    public Drawer(TShape selectShape, Vector<TShape> selectShapes) {
        super(selectShape, selectShapes);
    }



    @Override
    public void prepare(int x, int y, Graphics2D graphics) {
        this.selectShape.prepareDrawing(x, y);
        this.selectShape.draw(graphics);
    }

    public void setFont(String font) {
        if (this.selectShape instanceof TTextBox) {
            ((TTextBox) this.selectShape).setFont(font);
        }
    }


    @Override
    public void keep(int x, int y, Graphics2D graphics, Image image) {
        this.selectShape.draw(graphics);
        this.selectShape.resize(x, y);
        this.selectShape.draw(graphics);

        graphics.drawImage(image, 0, 0, null);
    }

    @Override
    public void continueTransform(int x, int y, Graphics2D graphics2D) {
        this.selectShape.addPoint(x, y);
    }

    @Override
    public void finish(int x, int y, Graphics2D graphics2D, Vector<TShape> shapes, Vector<TimeShape> timeShapes) {
        this.selectShape.setFirst(false);
        shapes.add(this.selectShape);
        timeShapes.add(new TimeShape(this.selectShape));
    }
}
