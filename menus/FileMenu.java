package menus;

import frames.DrawingPanel;
import frames.DrawingPanel.TimeShape;
import global.Constants.EFileMenu;
import shapes.TShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.Vector;

public class FileMenu extends JMenu {
    private DrawingPanel drawingPanel;

    private PrinterJob printerJob;
    private JFileChooser fileChooser;
    private File file;

    public FileMenu(String title) {
        super(title);
        this.printerJob = PrinterJob.getPrinterJob();
        this.fileChooser = new JFileChooser();
        ActionHandler actionHandler = new ActionHandler();

        for (EFileMenu eMenuItem : EFileMenu.values()) {
            JMenuItem jMenuItem = new JMenuItem(eMenuItem.getLabel());
            jMenuItem.addActionListener(actionHandler);
            jMenuItem.setActionCommand(eMenuItem.name());
            jMenuItem.setAccelerator(eMenuItem.getKeyStroke());
            this.add(jMenuItem);
        }
    }

    public void init(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }



    private void store(File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.drawingPanel.getShapes());
            objectOutputStream.close();
            this.drawingPanel.setUpdated(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object object = objectInputStream.readObject();
            this.drawingPanel.setShapes(object);
            this.drawingPanel.loadDraw();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (this.drawingPanel.isUpdated()) {
            if (this.file == null) {
                saveAs();
            } else {
                store(this.file);
            }
        }
    }

    public void saveAs() {
        int result = this.fileChooser.showSaveDialog(this.drawingPanel);
        if (result == JFileChooser.APPROVE_OPTION) {
            this.file = this.fileChooser.getSelectedFile();
            store(this.file);
        } else {
            return;
        }
    }


    public void open() {
        if (this.drawingPanel.isUpdated()) {
            int result = JOptionPane.CANCEL_OPTION;
            result = JOptionPane.showConfirmDialog(this, "그림판의 변경하신 내용을 저장하시겠습니까? (저장하지 않으면 병경 사항이 유실됩니다.)", "열기",
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                save();
            }else if (result == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        int reply = this.fileChooser.showOpenDialog(this.drawingPanel);
        if (reply == JFileChooser.APPROVE_OPTION) {
            this.file = this.fileChooser.getSelectedFile();
            load(this.file);
        } else {
            return;
        }
    }



    private void print() {
        this.printerJob.setJobName("그림 인쇄");
        this.printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D graphics2D = (Graphics2D) graphics;
                graphics2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                drawingPanel.paint(graphics2D);
                return Printable.PAGE_EXISTS;
            }
        });
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    private void newPanel() {
        Vector<TShape> shapes = (Vector<TShape>) this.drawingPanel.getShapes();
        this.file = null;
        if (this.drawingPanel.isUpdated()) {
            int result = JOptionPane.showConfirmDialog(this, "그림판의 변경하신 내용을 저장하시겠습니까? (저장하지 않으면 병경 사항이 유실됩니다.)","새로 만들기",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == 0) {
                save();
            } else if (result == 2) {
                return;
            }
        }
        shapes.clear();
        this.drawingPanel.setShapes(shapes);
        this.drawingPanel.loadDraw();
        this.drawingPanel.setUpdated(false);
    }

    private void quit() {
        if (this.drawingPanel.isUpdated()) {
            int save = JOptionPane.showConfirmDialog(this, "그림판의 변경하신 내용을 저장하시겠습니까? (저장하지 않으면 병경 사항이 유실됩니다.)","종료하기",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (save == 0) {
                save();
            } else if (save == 2) {
                return;
            }
        }
        System.exit(0);
    }

    public class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == EFileMenu.eNew.name()) {
                newPanel();
            } else if (e.getActionCommand() == EFileMenu.eSave.name()) {
                save();
            } else if (e.getActionCommand() == EFileMenu.eSaveAs.name()) {
                saveAs();
            } else if (e.getActionCommand() == EFileMenu.eOpen.name()) {
                open();
            } else if (e.getActionCommand() == EFileMenu.ePrint.name()) {
                print();
            } else if (e.getActionCommand() == EFileMenu.eQuit.name()) {
                quit();
            }
        }
    }







}
