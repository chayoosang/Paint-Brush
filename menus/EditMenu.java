package menus;

import frames.DrawingPanel;
import frames.DrawingPanel.TimeShape;
import global.Constants.EEditMenu;
import shapes.TCurve;
import shapes.TShape;
import shapes.TTextBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class EditMenu extends JMenu {

    private DrawingPanel drawingPanel;

    private TShape selectShape;
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
        this.selectShape = this.drawingPanel.getOnShape();
        shapes.remove(this.selectShape);
        this.drawingPanel.setShapes(shapes);
    }

    private void copy() {
        this.selectShape = this.drawingPanel.getOnShape();
    }

    private void paste() {
        if (this.selectShape != null) {
            Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
            TShape shape = this.selectShape.clone();
            shape.setLineColor(this.selectShape.getLineColor());
            shape.setStrokeValue(this.selectShape.getStrokeValue());

            if (shape instanceof TTextBox && this.selectShape instanceof TTextBox) {
                ((TTextBox) shape).setText(((TTextBox) this.selectShape).getText());
            }
            if (shape instanceof TCurve && this.selectShape instanceof TCurve) {
                ((TCurve) shape).setPointX(((TCurve) this.selectShape).getPointX());
                ((TCurve) shape).setPointY(((TCurve) this.selectShape).getPointY());
            }
//            shape.resizeShape(this.selectShape.getBounds());
//            shape.resizeShape(shape.setLocation(0, 0));
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
