package frames;

import menus.EditMenu;
import menus.FileMenu;

import javax.swing.*;
import java.util.Locale;

public class MenuBar extends JMenuBar {
    //components
    private FileMenu fileMenu;
    private EditMenu editMenu;
    private DrawingPanel drawingPanel;

    public MenuBar() {
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isMac = osName.startsWith("mac os x");
        if (isMac) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        //components
        this.fileMenu = new FileMenu("File");
        this.add(fileMenu);

        this.editMenu = new EditMenu("Edit");
        this.add(editMenu);



    }

    public void init(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        this.fileMenu.init(this.drawingPanel);
        this.editMenu.init(this.drawingPanel);
    }



}
