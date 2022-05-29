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
        this.px = 10;
        this.py = 10;
    }

    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    private void cut() {
        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
        Vector<TShape> selectShapes = this.drawingPanel.getSelectShapes();
        this.copyShapes.removeAllElements();
        this.px = 10;
        this.py = 10;

        if (!selectShapes.isEmpty()) {
            for (TShape shape : selectShapes) {
                this.copyShapes.add(shape);
                shapes.remove(shape);
            }
        } else {
            TShape shape = this.drawingPanel.getCurrentShape();
            this.copyShapes.add(shape);
            shapes.remove(shape);
        }

        this.drawingPanel.setShapes(shapes);
    }

    private void copy() {
        Vector<TShape> selectShapes = this.drawingPanel.getSelectShapes();
        this.copyShapes.removeAllElements();
        this.px = 10;
        this.py = 10;

        if (!selectShapes.isEmpty()) {
            for (TShape shape : selectShapes) {
                this.copyShapes.add(shape);
            }
        } else {
            TShape shape = this.drawingPanel.getCurrentShape();
            this.copyShapes.add(shape);
        }
    }

    private void paste() {
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

                shapes.add(copyShape);

                this.drawingPanel.setShapes(shapes);
                this.px += 10;
                this.py += 10;
            }


        }
    }

    private void delete() {
        Graphics2D graphics = (Graphics2D) this.drawingPanel.getGraphics();
        graphics.setXORMode(Color.white);

        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
        Vector<TShape> selectShapes = this.drawingPanel.getSelectShapes();
        this.copyShapes.removeAllElements();
        this.px = 10;
        this.py = 10;

        if (!selectShapes.isEmpty()) {
            for (TShape shape : selectShapes) {
                if (shape.isSelected()) {
                    shape.drawAnchors(graphics);
                }
                shapes.remove(shape);
            }
        } else {
            TShape shape = this.drawingPanel.getCurrentShape();
            if (shape.isSelected()) {
                shape.drawAnchors(graphics);
            }
            shapes.remove(shape);
        }
        this.drawingPanel.setShapes(shapes);
    }

    private void undo() {
        Vector<TimeShape> undoShape = this.drawingPanel.getUndoShape();
        Vector<TShape> drawShape = (Vector<TShape>) this.drawingPanel.getShapes();
        if (undoShape.size() == 0) {
            drawShape.clear();
            this.drawingPanel.setShapes(drawShape);
            return;
        }
        else  {
            if (undoShape.get(undoShape.size()-1).getShape() != drawShape.get(drawShape.size() - 1)) {
                drawShape.remove(drawShape.size() - 1);
                this.drawingPanel.setShapes(drawShape);
            } else {
                TShape changeShape = undoShape.get(undoShape.size()-1).getShape();
                TShape shape = drawShape.get(drawShape.size() - 1);
                shape.setLineColor(changeShape.getLineColor());
                shape.setStrokeValue(changeShape.getStrokeValue());
//                shape.resizeShape(undoShape.get(undoShape.size()-1).getRectangle());
                drawShape.set(drawShape.size() - 1, shape);
                this.drawingPanel.setShapes(drawShape);
            }
            undoShape.remove(undoShape.size()-1);
            this.drawingPanel.setUndoShape(undoShape);
        }
        this.redoTime = undoShape.size() + 1;

    }

    private void redo() {
        Vector<TimeShape> redoShape = this.drawingPanel.getRedoShape();
        Vector<TimeShape> undoShape = this.drawingPanel.getUndoShape();
        Vector<TShape> drawShape = (Vector<TShape>) this.drawingPanel.getShapes();


        if (redoShape.size() == 0 || this.redoTime > redoShape.size()-1) {
            return;
        }
        if (this.redoTime == -9999) {
            return;
        }else if (this.undoTime < 0 && !(this.undoTime == -9999)) {
            this.undoTime = -2;
            this.redoTime = 0;
        }

         if (drawShape.size() == 0) {
                TShape shape = redoShape.get(this.redoTime).getShape();
//                shape.resizeShape(redoShape.get(this.redoTime).getRectangle());
                drawShape.add(shape);
                this.drawingPanel.setShapes(drawShape);
            }else {
                if (redoShape.get(this.redoTime).getShape() != drawShape.get(drawShape.size() - 1)) {
                    TShape shape = redoShape.get(this.redoTime).getShape();
                    drawShape.add(shape);
                    this.drawingPanel.setShapes(drawShape);
                } else {
                	TShape shape = drawShape.get(drawShape.size() - 1);
                    TShape changeShape = redoShape.get(this.redoTime).getShape();
                    shape.setLineColor(changeShape.getLineColor());
                    shape.setStrokeValue(changeShape.getStrokeValue());
//                    shape.resizeShape(redoShape.get(this.redoTime).getRectangle());
                    drawShape.set(drawShape.size() - 1, shape);
                    this.drawingPanel.setShapes(drawShape);
                }
             undoShape.add(redoShape.get(this.redoTime));
            }

        this.redoTime++;
        this.undoTime = this.redoTime -2;
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
