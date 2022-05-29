package transformer;

import shapes.TPolyLine;
import shapes.TShape;

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

    @Override
    public void keep(int x, int y, Graphics2D graphics, Image image) {
        this.selectShape.draw(graphics);
        this.selectShape.resize(x, y);
        this.selectShape.draw(graphics);

        graphics.drawImage(image, 0, 0, null);
    }

    @Override
    public void continueTransform(int x, int y, Graphics2D graphics2D) {
        if (this.selectShape instanceof TPolyLine) {
            this.selectShape.addPoint(x, y);
        }
    }

    @Override
    public void finish(int x, int y, Graphics2D graphics2D, Vector<TShape> shapes) {;
        shapes.add(this.selectShape);
    }
}
