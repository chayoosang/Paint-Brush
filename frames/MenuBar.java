package frames;

import menus.EditMenu;
import menus.FileMenu;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    //components
    private FileMenu fileMenu;
    private EditMenu editMenu;
    private DrawingPanel drawingPanel;

    public MenuBar() {

        //components
        this.fileMenu = new FileMenu("File");
        this.add(fileMenu);

        this.editMenu = new EditMenu("Edit");
        this.add(editMenu);


    }

    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        this.fileMenu.associate(this.drawingPanel);
        this.editMenu.associate(this.drawingPanel);
    }



}
