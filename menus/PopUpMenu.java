package menus;

import frames.DrawingPanel;
import global.Constants.EPopupMenu;
import shapes.TRectangle;
import shapes.TShape;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class PopUpMenu extends PopupMenu {

    private DrawingPanel drawingPanel;

    private Vector<TShape> selectShapes;

    public PopUpMenu(DrawingPanel drawingPanel) throws HeadlessException {
        ActionHandler actionHandler = new ActionHandler();

        this.drawingPanel = drawingPanel;

        this.selectShapes = new Vector<>();

        for (EPopupMenu ePopupMenu : EPopupMenu.values()) {
            MenuItem menuItem = new MenuItem(ePopupMenu.getMenuName());
            menuItem.setActionCommand(ePopupMenu.getMenuName());
            menuItem.addActionListener(actionHandler);
            this.add(menuItem);
        }


    }

    public void cut() {

    }
    public void copy() {

    }
    public void paste() {

    }
    public void delete() {

    }
    public void group() {
        Graphics2D graphics = (Graphics2D) this.drawingPanel.getGraphics();
        graphics.setXORMode(this.drawingPanel.getBackground());

        this.selectShapes = this.drawingPanel.getSelectShapes();

        System.out.println("Aa");

        if (selectShapes.isEmpty()) {
            return;
        }

        TRectangle group = new TRectangle();
        int minX = 9999;
        int minY = 9999;
        int maxX = -9999;
        int maxY = -9999;

        for (TShape shape : this.selectShapes) {
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
        }

        group.prepareDrawing(minX - 20, minY - 20);
        group.resize(maxX + 20, maxY + 20);

        group.draw(graphics);
        group.drawAnchors(graphics);
        this.drawingPanel.setCurrentShape(group);
        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
        shapes.add(group);
        this.drawingPanel.setShapes(shapes);
    }
    public void unGroup() {

    }
    public void generalShape() {

    }
    public void front() {

    }
    public void behind() {

    }
    public void undo() {

    }
    public void redo() {

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
