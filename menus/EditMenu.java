package menus;

import frames.DrawingPanel;
import frames.DrawingPanel.TimeShape;
import global.Constants.EEditMenu;
import shapes.TShape;
import shapes.TTextBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.Vector;

public class EditMenu extends JMenu {

    private DrawingPanel drawingPanel;

    private TShape copyShape;
    private int redoTime;
    private int undoTime;

    public EditMenu(String s) {
        super(s);
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
    }

    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    private void cut() {
        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
        this.copyShape = this.drawingPanel.getOnShape();
        shapes.remove(this.copyShape);
        this.drawingPanel.setShapes(shapes);
    }

    private void copy() {
        this.copyShape = this.drawingPanel.getOnShape();
    }

    private void paste() {
        if (this.copyShape != null) {
            Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();

            TShape shape = this.copyShape.clone();
            shape.setShape(this.copyShape.getShape());
            shape.setFillColor(this.copyShape.getFillColor());
            shape.setLineColor(this.copyShape.getLineColor());
            shape.setStrokeValue(this.copyShape.getStrokeValue());

            if (shape instanceof TTextBox && this.copyShape instanceof TTextBox) {
                ((TTextBox) shape).setText(((TTextBox) this.copyShape).getText());
            }


            AffineTransform affineTransform = new AffineTransform();
            affineTransform.translate(this.copyShape.getBounds().getX() - 10, this.copyShape.getBounds().getY() - 10);
            shape.transformShape(affineTransform);

            shapes.add(shape);

            this.drawingPanel.setShapes(shapes);
        }
    }

    private void delete() {
        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
        shapes.remove(this.drawingPanel.getOnShape());
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
