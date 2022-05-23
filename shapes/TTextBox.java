package shapes;

import java.awt.*;

public class TTextBox extends TShape {

    private int preX, preY;
    private String text;

    public TTextBox() {
        this.shape = new Rectangle();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void prepareDrawing(int x, int y) {
        Rectangle textBox = (Rectangle) this.shape;
        textBox.setFrame(x, y, 0, 0);

        this.preX = x;
        this.preY = y;
    }

    @Override
    public TShape clone() {
        return new TTextBox();
    }

    @Override
    public void resize(int x, int y) {
        Rectangle textBox = (Rectangle) this.shape;
        if (this.preX > x || this.preY > y) {
            if (this.preX > x && this.preY <= y) {
                textBox.setFrame(x, textBox.getY(), Math.abs(x - preX), y - textBox.getY());
            } else if (this.preX <= x && this.preY > y) {
                textBox.setFrame( textBox.getX(), y, x - textBox.getX(), Math.abs(y - preY));
            } else {
                textBox.setFrame(x, y, Math.abs(x - preX), Math.abs(y - preY));
            }
        } else {
            textBox.setFrame(textBox.getX(), textBox.getY(), x - textBox.getX(), y - textBox.getY());
        }
    }

    public void inputText(String text) {
        this.text = text;
    }

    public void drawText(Graphics2D graphics) {
        graphics.setColor(this.lineColor);
        graphics.drawString(this.getText(), (int) (this.getBounds().getX() + 2), (int) (this.getBounds().getY() + this.getBounds().getHeight() / 2 + 2));
    }

    @Override
    public void draw(Graphics2D graphics) {
        graphics.setColor(Color.black);
        graphics.draw(this.shape);
        graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.7f));
        if (this.text != null) {
            drawText(graphics);
        }
    }







}
