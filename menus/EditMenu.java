package menus;

import frames.DrawingPanel;
import frames.DrawingPanel.TimeShape;
import global.Constants.EEditMenu;
import shapes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.Vector;

public class EditMenu extends JMenu {

    private DrawingPanel drawingPanel;

    private Vector<TShape> copyShapes;
    private int redoTime;
    private int undoTime;

    private boolean first;

    private int px;
    private int py;

    public EditMenu(String s) {
        super(s);
        this.copyShapes = new Vector<>();
        this.undoTime = -9999;
        this.redoTime = -9999;
        ActionHandler actionHandler = new ActionHandler();
        for (EEditMenu editMenu : EEditMenu.values()) {
            JMenuItem jMenuItem = new JMenuItem(editMenu.getLabel());
            jMenuItem.addActionListener(actionHandler);
            jMenuItem.setActionCommand(editMenu.name());
            jMenuItem.setAccelerator(editMenu.getKeyStroke());
            this.add(jMenuItem);
        }

        this.first = true;

        this.px = 10;
        this.py = 10;
    }

    public void init(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
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
        this.first = true;
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
        this.first = true;

    }

    private void paste() {
        Vector<TShape> selectedShape = this.drawingPanel.getSelectShapes();
        Graphics2D graphics = this.drawingPanel.getBufferGraphics();



        if (!this.copyShapes.isEmpty()) {
            Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();

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

                this.first = false;
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
                shape.setSelected(false);
                shape.draw(graphics);
                shape.drawAnchors(graphics);
                shapes.remove(shape);
                this.drawingPanel.repaint();
            }
            selectShapes.removeAllElements();
            this.drawingPanel.setSelectShapes(selectShapes);
        }else if (currentShape.equals(group)) {
            for (TShape shape : groupShapes) {
                shape.setSelected(false);
                shape.draw(graphics);
                shapes.remove(shape);
                this.drawingPanel.repaint();
            }
            groupShapes.removeAllElements();
            this.drawingPanel.setGroupShapes(groupShapes,null);
        } else {
            TShape shape = this.drawingPanel.getCurrentShape();
            shape.setSelected(false);
            shape.drawAnchors(graphics);
            shape.draw(graphics);
            shapes.remove(shape);
            this.drawingPanel.repaint();
        }
        this.drawingPanel.setCurrentShape(null);
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
            if (e.getActionCommand() == EEditMenu.eCut.name()) {
                cut();
            } else if (e.getActionCommand() == EEditMenu.eCopy.name()) {
                copy();
            } else if (e.getActionCommand() == EEditMenu.ePaste.name()) {
                paste();
            }else if (e.getActionCommand() == EEditMenu.eDelete.name()) {
                delete();
            }else if (e.getActionCommand() == EEditMenu.eUndo.name()) {
                undo();
            } else if (e.getActionCommand() == EEditMenu.eRedo.name()) {
                redo();
            }
        }
    }
}
