package shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class TTextBox extends TShape {

    private int preX, preY;
    private Point2D textPoint;
    private String text;
    private String fontString;
    private Font font;

    public TTextBox() {
        this.shape = new Rectangle();
        this.textPoint = new Point2D.Double();
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

        this.fillColor = Color.BLACK;
    }

    public void setFont(String fontString) {
        this.font = new Font(fontString, Font.PLAIN, 11);
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
        this.textPoint.setLocation(this.getBounds().getX() +2, this.getBounds().getY() + this.getBounds().getHeight() / 2 + 2);
    }

    public void transformShape(AffineTransform affineTransform) {
        super.transformShape(affineTransform);
        this.textPoint.setLocation(affineTransform.transform(this.textPoint, new Point2D.Double()));
    }

    public void inputText(String text) {
        this.text = text;
    }

    public void drawText(Graphics2D graphics) {
        graphics.setColor(this.lineColor);
        graphics.drawString(this.getText(), (int) (this.textPoint.getX()), (int) (this.textPoint.getY()));
    }

    @Override
    public void draw(Graphics2D graphics) {
        graphics.draw(this.shape);
        if (this.text != null) {
            if (this.fillColor != null) {;
                graphics.setFont(font);
                drawText(graphics);
            }
        }
    }

    public boolean containText(int x, int y) {
        int tx = (int) this.shape.getBounds().getX();
        int ty = (int) this.shape.getBounds().getY();
        int tw = (int) this.shape.getBounds().getWidth();
        int th = (int) this.shape.getBounds().getHeight();

        for (int i = tx; i <= tx + tw; i++) {
            if (i == x && y == ty) {
                return true;
            }
        }

        for (int i = ty; i <= ty + th; i++) {
            if (i == tx && y == i) {
                return true;
            }
        }

        for (int i = tx; i <= tx + tw; i++) {
            if (i == x && y == ty + th) {
                return true;
            }
        }

        for (int i = ty; i <= ty + th; i++) {
            if (i == tx + tw && y == i) {
                return true;
            }
        }

        return false;
    }







}
