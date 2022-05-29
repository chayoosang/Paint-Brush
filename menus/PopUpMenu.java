package menus;

import frames.DrawingPanel;
import global.Constants.EPopupMenu;
import shapes.TShape;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class PopUpMenu extends PopupMenu {

    private ActionHandler actionHandler;
    private DrawingPanel drawingPanel;

    private Vector<TShape> selectShapes;

    public PopUpMenu(DrawingPanel drawingPanel) throws HeadlessException {
        this.drawingPanel = drawingPanel;

        this.selectShapes = new Vector<>();

        for (EPopupMenu ePopupMenu : EPopupMenu.values()) {
            this.add(new MenuItem(ePopupMenu.getMenuName()));
            this.addActionListener(this.actionHandler);
            this.setActionCommand(ePopupMenu.getMenuName());
        }

        this.actionHandler = new ActionHandler();
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
            if (e.getActionCommand() == EPopupMenu.eCut.name()) {
                cut();
            } else if (e.getActionCommand() == EPopupMenu.eCopy.name()) {
                copy();
            } else if (e.getActionCommand() == EPopupMenu.ePaste.name()) {
                paste();
            }else if (e.getActionCommand() == EPopupMenu.eDelete.name()) {
                delete();
            }else if (e.getActionCommand() == EPopupMenu.eGroup.name()) {
                group();
            }else if (e.getActionCommand() == EPopupMenu.eUnGroup.name()) {
                unGroup();
            }else if (e.getActionCommand() == EPopupMenu.eGeneralShape.name()) {
                generalShape();
            }else if (e.getActionCommand() == EPopupMenu.eFront.name()) {
                front();
            }else if (e.getActionCommand() == EPopupMenu.eBehind.name()) {
                behind();
            }else if (e.getActionCommand() == EPopupMenu.eUndo.name()) {
                undo();
            } else if (e.getActionCommand() == EPopupMenu.eRedo.name()) {
                redo();
            }
        }
    }
}
