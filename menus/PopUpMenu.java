package menus;

import frames.DrawingPanel;
import global.Constants.EPopupMenu;
import shapes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.sql.Time;
import java.util.Vector;

public class PopUpMenu extends PopupMenu {

    private DrawingPanel drawingPanel;

    private int px;
    private int py;

    private Vector<TShape> copyShapes;
    private Vector<TShape> selectShapes;

    public PopUpMenu(DrawingPanel drawingPanel) throws HeadlessException {
        ActionHandler actionHandler = new ActionHandler();

        this.drawingPanel = drawingPanel;
        this.copyShapes = new Vector<>();
        this.selectShapes = new Vector<>();

        for (EPopupMenu ePopupMenu : EPopupMenu.values()) {
            MenuItem menuItem = new MenuItem(ePopupMenu.getMenuName());
            menuItem.setActionCommand(ePopupMenu.getMenuName());
            menuItem.addActionListener(actionHandler);
            this.add(menuItem);
        }
        this.px = 10;
        this.py = 10;

    }

    private void cut() {
        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
        Vector<TShape> selectShapes = this.drawingPanel.getSelectShapes();
        Vector<TShape> groupShapes = this.drawingPanel.getGroupShapes();
        TShape group = this.drawingPanel.getGroupShape();
        TShape currentShape = this.drawingPanel.getCurrentShape();
        Graphics2D graphics = this.drawingPanel.getBufferGraphics();

        this.copyShapes.removeAllElements();
        this.px = 10;
        this.py = 10;

        if (!selectShapes.isEmpty()) {
            for (TShape shape : selectShapes) {
                this.copyShapes.add(shape);
                shape.setSelected(false);
                shape.draw(graphics);
                shape.drawAnchors(graphics);
                shapes.remove(shape);
            }
        } else if (currentShape.equals(group)) {
            for (TShape shape : groupShapes) {
                this.copyShapes.add(shape);
                shape.setSelected(false);
                shape.draw(graphics);
                shape.drawAnchors(graphics);
                shapes.remove(shape);
            }
        } else {
            this.copyShapes.add(currentShape);
            currentShape.setSelected(false);
            currentShape.draw(graphics);
            currentShape.drawAnchors(graphics);
            shapes.remove(currentShape);
        }
        this.drawingPanel.setCurrentShape(null);
        this.drawingPanel.setShapes(shapes);
        this.drawingPanel.repaint();
    }

    private void copy() {
        Vector<TShape> selectShapes = this.drawingPanel.getSelectShapes();
        Vector<TShape> groupShapes = this.drawingPanel.getGroupShapes();
        TShape group = this.drawingPanel.getGroupShape();
        TShape currentShape = this.drawingPanel.getCurrentShape();


        this.copyShapes.removeAllElements();
        this.px = 10;
        this.py = 10;

        if (!selectShapes.isEmpty()) {
            for (TShape shape : selectShapes) {
                this.copyShapes.add(shape);
            }
        } else if (currentShape.equals(group)) {
            for (TShape shape : groupShapes) {
                if (!shape.equals(group)) {
                    this.copyShapes.add(shape);
                }
            }
        } else {
            this.copyShapes.add(currentShape);
        }


    }

    private void paste() {
        if (!this.copyShapes.isEmpty()) {
            Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
            Graphics2D graphics = this.drawingPanel.getBufferGraphics();

            for (TShape shape : this.copyShapes) {
                TShape copyShape = shape.clone();
                copyShape.setShape(shape.getShape());
                copyShape.setFillColor(shape.getFillColor());
                copyShape.setLineColor(shape.getLineColor());
                copyShape.setStrokeValue(shape.getStrokeValue());
                copyShape.setSelected(false);

                if (copyShape instanceof TTextBox && shape instanceof TTextBox) {
                    ((TTextBox) copyShape).setText(((TTextBox) shape).getText());
                } else if (copyShape instanceof TPolyLine && shape instanceof TPolyLine) {
                    ((TPolyLine) copyShape).setxPoints(((TPolyLine) shape).getxPoints());
                    ((TPolyLine) copyShape).setyPoints(((TPolyLine) shape).getyPoints());
                    ((TPolyLine) copyShape).setSortX(((TPolyLine) shape).getSortX());
                    ((TPolyLine) copyShape).setSortY(((TPolyLine) shape).getSortY());
                } else if (copyShape instanceof TRegularTriangle && shape instanceof TRegularTriangle) {
                    ((TRegularTriangle) copyShape).setxPoint(((TRegularTriangle) shape).getxPoint());
                    ((TRegularTriangle) copyShape).setyPoint(((TRegularTriangle) shape).getyPoint());
                }else if (copyShape instanceof TRightTriangle && shape instanceof TRightTriangle) {
                    ((TRightTriangle) copyShape).setxPoint(((TRightTriangle) shape).getxPoint());
                    ((TRightTriangle) copyShape).setyPoint(((TRightTriangle) shape).getyPoint());
                }else if (copyShape instanceof TPentagon && shape instanceof TPentagon) {
                    ((TPentagon) copyShape).setxPoint(((TPentagon) shape).getxPoint());
                    ((TPentagon) copyShape).setyPoint(((TPentagon) shape).getyPoint());
                }else if (copyShape instanceof THexagon && shape instanceof THexagon) {
                    ((THexagon) copyShape).setxPoint(((THexagon) shape).getxPoint());
                    ((THexagon) copyShape).setyPoint(((THexagon) shape).getyPoint());
                }

                AffineTransform affineTransform = new AffineTransform();
                affineTransform.setToIdentity();
                affineTransform.translate(px, py);
                copyShape.transformShape(affineTransform);
                copyShape.draw(graphics);
                shapes.add(copyShape);

                this.drawingPanel.repaint();

                this.drawingPanel.setShapes(shapes);
                this.px += 10;
                this.py += 10;

            }

        }

    }

    private void delete() {
        Graphics2D graphics = this.drawingPanel.getBufferGraphics();

        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
        Vector<TShape> selectShapes = this.drawingPanel.getSelectShapes();
        Vector<TShape> groupShapes = this.drawingPanel.getGroupShapes();
        TShape group = this.drawingPanel.getGroupShape();
        TShape currentShape = this.drawingPanel.getCurrentShape();

        this.copyShapes.removeAllElements();
        this.px = 10;
        this.py = 10;

        if (!selectShapes.isEmpty()) {
            for (TShape shape : selectShapes) {
                shape.draw(graphics);
                shape.drawAnchors(graphics);
                shapes.remove(shape);
                this.drawingPanel.repaint();
            }
            selectShapes.removeAllElements();
            this.drawingPanel.setSelectShapes(selectShapes);
        }else if (currentShape.equals(group)) {
            for (TShape shape : groupShapes) {
                shape.draw(graphics);
                shapes.remove(shape);
                this.drawingPanel.repaint();
            }
            groupShapes.removeAllElements();
            this.drawingPanel.setGroupShapes(groupShapes,null);
        }
        else {
            TShape shape = this.drawingPanel.getCurrentShape();
            shape.drawAnchors(graphics);
            shape.draw(graphics);
            shapes.remove(shape);
            this.drawingPanel.repaint();
        }
        this.drawingPanel.setCurrentShape(null);
        this.drawingPanel.setShapes(shapes);
        this.drawingPanel.repaint();
    }

    public void group() {
        Graphics2D graphics = this.drawingPanel.getBufferGraphics();


        this.selectShapes = this.drawingPanel.getSelectShapes();

        Vector<TShape> groupShapes = this.drawingPanel.getGroupShapes();

        if (!groupShapes.isEmpty()) {
            return;
        }
        if (selectShapes.isEmpty()) {
            return;
        }

        TRectangle group = new TRectangle();
        int minX = 9999;
        int minY = 9999;
        int maxX = -9999;
        int maxY = -9999;

        for (TShape shape : this.selectShapes) {

            groupShapes.add(shape);

            if (shape.getBounds().getX() < minX) {
                minX = (int) shape.getBounds().getX();
            }

            if (shape.getBounds().getY() < minY) {
                minY = (int) shape.getBounds().getY();
            }

            if (shape.getBounds().getX() + shape.getBounds().getWidth()> maxX) {
                maxX = (int) (shape.getBounds().getX() + shape.getBounds().getWidth());
            }

            if (shape.getBounds().getY() + shape.getBounds().getHeight()> maxY) {
                maxY = (int) (shape.getBounds().getY() + shape.getBounds().getHeight());
            }

            shape.drawAnchors(graphics);
        }

        group.prepareDrawing(minX - 20, minY - 20);
        group.resize(maxX + 20, maxY + 20);

        group.draw(graphics);
        group.drawAnchors(graphics);

        groupShapes.add(group);

        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
        shapes.add(group);
        this.drawingPanel.setCurrentShape(group);

        this.drawingPanel.setGroupShape(group);
        this.drawingPanel.setGroupShapes(groupShapes, group);
        this.drawingPanel.setShapes(shapes);
    }
    public void unGroup() {
        TShape group = this.drawingPanel.getGroupShape();
        Vector<TShape> groupShapes = this.drawingPanel.getGroupShapes();
        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();

        Graphics2D graphics = this.drawingPanel.getBufferGraphics();

        if (group.isSelected()) {
            group.drawAnchors(graphics);
        }
        shapes.remove(group);
        groupShapes.removeAllElements();

        group.draw(graphics);
        this.drawingPanel.repaint();

        if (this.drawingPanel.getCurrentShape().equals(group)) {
            this.drawingPanel.setCurrentShape(null);
        }

        this.drawingPanel.setGroupShapes(groupShapes, null);
        this.drawingPanel.setShapes(shapes);

    }

    public void setFrame() {
        TShape shape = this.drawingPanel.getCurrentShape();

        JPanel panel = new JPanel( new GridLayout(4, 2) );
        panel.add( new JLabel("x") );
        JTextField xValue = new JTextField(10);
        xValue.setText(String.valueOf(shape.getBounds().getX()));
        panel.add( xValue );

        panel.add( new JLabel("y") );
        JTextField yValue = new JTextField(10);
        yValue.setText(String.valueOf(shape.getBounds().getY()));
        panel.add( yValue );

        panel.add( new JLabel("w") );
        JTextField wValue = new JTextField(10);
        wValue.setText(String.valueOf(shape.getBounds().getWidth()));
        panel.add( wValue );

        panel.add( new JLabel("h") );
        JTextField hValue = new JTextField(10);
        hValue.setText(String.valueOf(shape.getBounds().getHeight()));
        panel.add( hValue );

        int result = JOptionPane.showConfirmDialog(
                this.drawingPanel, // use your JFrame here
                panel,
                "Use a Panel",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.YES_OPTION)
        {
            if (xValue.getText().matches("[+-]?\\d*(\\.\\d+)?")
                    && yValue.getText().matches("[+-]?\\d*(\\.\\d+)?")
                    && wValue.getText().matches("[+-]?\\d*(\\.\\d+)?")
                    && hValue.getText().matches("[+-]?\\d*(\\.\\d+)?")) {
                Point2D scaleFactor = computeScaleFactor(Double.parseDouble(wValue.getText()), Double.parseDouble(hValue.getText()), shape);
                AffineTransform affineTransform = new AffineTransform();
                affineTransform.translate(Double.parseDouble(xValue.getText()) - shape.getBounds().getX(), Double.parseDouble(yValue.getText()) - shape.getBounds().getY());
                affineTransform.scale(scaleFactor.getX(), scaleFactor.getY());

                Graphics2D graphics2D = this.drawingPanel.getBufferGraphics();
                shape.drawAnchors(graphics2D);
                shape.draw(graphics2D);
                shape.transformShape(affineTransform);
                shape.transformAnchor(affineTransform);
                shape.draw(graphics2D);
                shape.drawAnchors(graphics2D);
                this.drawingPanel.repaint();
            }
        }
        else
        {
            return;
        }

    }

    public Point2D computeScaleFactor(double pw, double ph, TShape shape) {

        double cw = shape.getBounds().getWidth();
        double ch = shape.getBounds().getHeight();

        double xFactor = 1.0;
        if ( cw != pw) {
            xFactor = pw / cw;
        }
        double yFactor = 1.0;
        if (ch != ph) {
            yFactor = ph / ch;
        }
        return new Point2D.Double(xFactor, yFactor);
    }



    public void generalShape() {
        TShape shape = this.drawingPanel.getCurrentShape();

        if (shape == null) {
            return;
        }

        this.drawingPanel.setLineColor(shape.getLineColor());
        this.drawingPanel.setFillColor(shape.getFillColor());
        this.drawingPanel.setStroke(shape.getStrokeValue());

    }
    public void front() {
        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
        TShape shape = this.drawingPanel.getCurrentShape();

        if (shape == null) {
            return;
        }

        shapes.remove(shape);
        shapes.add(shape);

        this.drawingPanel.setShapes(shapes);
        this.drawingPanel.repaint();
    }
    public void behind() {
        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
        TShape shape = this.drawingPanel.getCurrentShape();

        if (shape == null) {
            return;
        }

        shapes.remove(shape);
        shapes.add(0, shape);

        this.drawingPanel.setShapes(shapes);
        this.drawingPanel.repaint();
    }
    public void undo() {
        Vector<DrawingPanel.TimeShape> timeShapes = this.drawingPanel.getTimeShapes();
        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();

        if (timeShapes.isEmpty()) {
            return;
        }

        if (this.drawingPanel.getTimeIndex() == 0 && !timeShapes.isEmpty()) {
            this.drawingPanel.setTimeIndex(timeShapes.size()-1);
        }


        Graphics2D graphics2D = this.drawingPanel.getBufferGraphics();

        TShape undoShape = timeShapes.get(this.drawingPanel.getTimeIndex()).getShape();
        TShape undoDrawShape = timeShapes.get(this.drawingPanel.getTimeIndex()).getCopyShape();

        boolean remove = true;

        for (TShape shape : shapes) {
            if (shape.equals(undoShape)) {
                for (int i = 0; i < this.drawingPanel.getTimeIndex(); i++) {
                    if (timeShapes.get(i).getShape().equals(shape)) {
                        remove = false;
                    }
                }

                int index = shapes.indexOf(shape);
                shape.draw(graphics2D);
                if (remove == false) {
                    undoDrawShape.draw(graphics2D);
                    shapes.set(index, undoDrawShape);
                } else {
                    shapes.remove(shape);
                }
                this.drawingPanel.setTimeIndex(this.drawingPanel.getTimeIndex()-1);
                this.drawingPanel.setShapes(shapes);
                this.drawingPanel.repaint();
                return;
            }
        }

        undoDrawShape.draw(graphics2D);
        shapes.add(undoDrawShape);
        this.drawingPanel.setShapes(shapes);
        this.drawingPanel.setTimeIndex(this.drawingPanel.getTimeIndex()-1);
        this.drawingPanel.repaint();
        return;
    }
    public void redo() {
        Vector<DrawingPanel.TimeShape> timeShapes = this.drawingPanel.getTimeShapes();
        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();

        if (timeShapes.isEmpty()) {
            return;
        }

        if (this.drawingPanel.getTimeIndex() > timeShapes.size() && timeShapes.isEmpty()) {
            return;
        } else {
            this.drawingPanel.setTimeIndex(this.drawingPanel.getTimeIndex()+1);
        }


        Graphics2D graphics2D = this.drawingPanel.getBufferGraphics();

        TShape redoShape = timeShapes.get(this.drawingPanel.getTimeIndex()).getShape();
        TShape redoDrawShape = timeShapes.get(this.drawingPanel.getTimeIndex()).getCopyShape();

        boolean add = true;

        for (TShape shape : shapes) {
            if (shape.equals(redoShape)) {
                for (int i = 0; i < this.drawingPanel.getTimeIndex(); i++) {
                    if (timeShapes.get(i).getShape().equals(shape)) {
                        add = false;
                    }
                }
                int index = shapes.indexOf(shape);
                shape.draw(graphics2D);
                if (add == false) {
                    redoDrawShape.draw(graphics2D);
                    shapes.set(index, redoDrawShape);
                } else {
                    redoDrawShape.draw(graphics2D);
                    shapes.add(redoDrawShape);
                }
                this.drawingPanel.setTimeIndex(this.drawingPanel.getTimeIndex()+1);
                this.drawingPanel.setShapes(shapes);
                this.drawingPanel.repaint();
                return;
            }
        }

        redoDrawShape.draw(graphics2D);
        shapes.add(redoDrawShape);
        this.drawingPanel.setShapes(shapes);
        this.drawingPanel.setTimeIndex(this.drawingPanel.getTimeIndex()-1);
        this.drawingPanel.repaint();
        return;
    }





    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == EPopupMenu.eCut.getMenuName()) {
                cut();
            } else if (e.getActionCommand() == EPopupMenu.eCopy.getMenuName()) {
                copy();
            } else if (e.getActionCommand() == EPopupMenu.ePaste.getMenuName()) {
                paste();
            }else if (e.getActionCommand() == EPopupMenu.eDelete.getMenuName()) {
                delete();
            }else if (e.getActionCommand() == EPopupMenu.eGroup.getMenuName()) {
                group();
            }else if (e.getActionCommand() == EPopupMenu.eUnGroup.getMenuName()) {
                unGroup();
            }else if (e.getActionCommand() == EPopupMenu.eSetFrame.getMenuName()) {
                setFrame();
            }else if (e.getActionCommand() == EPopupMenu.eGeneralShape.getMenuName()) {
                generalShape();
            }else if (e.getActionCommand() == EPopupMenu.eFront.getMenuName()) {
                front();
            }else if (e.getActionCommand() == EPopupMenu.eBehind.getMenuName()) {
                behind();
            }else if (e.getActionCommand() == EPopupMenu.eUndo.getMenuName()) {
                undo();
            } else if (e.getActionCommand() == EPopupMenu.eRedo.getMenuName()) {
                redo();
            }
        }
    }
}
