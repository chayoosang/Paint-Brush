package transformer;

import frames.DrawingPanel;
import shapes.TShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Vector;

public class Translator extends Transformer {

    public Translator(TShape selectShape , Vector<TShape> selectShapes) {
        super(selectShape, selectShapes);
    }

    @Override
    public void prepare(int x, int y, Graphics2D graphics) {
        this.previous.setLocation(x, y);
    }

    @Override
    public void keep(int x, int y, Graphics2D graphics, Image image) {

        if (!this.selectShapes.isEmpty()) {
            for (TShape shape : this.selectShapes) {
                shape.draw(graphics);
                AffineTransform affineTransform = new AffineTransform();
                affineTransform.setToTranslation(x - this.previous.getX(), y - this.previous.getY());
                shape.transformShape(affineTransform);
                shape.transformAnchor(affineTransform);
                shape.draw(graphics);
            }
        } else {
            this.selectShape.draw(graphics);
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.setToTranslation(x - this.previous.getX(), y - this.previous.getY());
            this.selectShape.transformShape(affineTransform);
            this.selectShape.transformAnchor(affineTransform);
            this.selectShape.draw(graphics);
        }

        this.previous.setLocation(x, y);
        graphics.drawImage(image, 0, 0, null);
    }


    @Override
    public void finish(int x, int y, Graphics2D graphics2D, Vector<TShape> shapes, Vector<DrawingPanel.TimeShape> timeShapes) {
        if (!this.selectShapes.isEmpty()) {
            for (TShape shape : this.selectShapes) {
                int index = shapes.indexOf(shape);
                shapes.set(index, shape);
                timeShapes.add(new DrawingPanel.TimeShape(this.selectShape));
            }
        } else {
            int index = shapes.indexOf(this.selectShape);
            shapes.set(index, this.selectShape);
            timeShapes.add(new DrawingPanel.TimeShape(this.selectShape));
        }
    }
}
