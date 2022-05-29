package transformer;

import shapes.TShape;
import shapes.TTextBox;

import java.awt.*;
import java.util.Vector;

public class Enter extends Transformer{

    public Enter(TShape selectShape, Vector<TShape> selectShapes) {
        super(selectShape, selectShapes);
    }

    @Override
    public void prepare(int x, int y, Graphics2D graphics) {

    }

    @Override
    public void keep(int x, int y, Graphics2D graphics, Image image) {

    }

    @Override
    public void finish(int x, int y, Graphics2D graphics2D, Vector<TShape> shapes) {

    }

    public void prepareText(TextField input) {
        TTextBox textBox = (TTextBox) this.selectShape;
        input.setText(textBox.getText());
        input.setBounds(textBox.getBounds());
        input.setVisible(true);

    }

    public void keepText(TextField input, Graphics2D graphics, Vector<TShape> shapes) {
        input.setVisible(false);

        String text = input.getText();

        TTextBox textBox = (TTextBox) this.selectShape;
        textBox.inputText(text);
        textBox.drawText(graphics);

        int index = shapes.indexOf(textBox);
        shapes.set(index, textBox);
    }
}
