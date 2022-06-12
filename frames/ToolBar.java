package frames;

import global.Constants.ETools;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ToolBar extends JToolBar {

    //components
    private JButton lineColorButton;
    private JButton fillColorButton;
    private SpinnerNumberModel strokeModel;
    private JSpinner strokeSelect;
    private JComboBox<String> fontSelect;
    private String font[] = {"Serif", "SanSerif", "monospaced", "Dialog"};

    private BufferedImage lineColorImage;
    private BufferedImage fillColorImage;
    private Color lineColor;
    private Color fillColor;

    //associations
    private DrawingPanel drawingPanel;


    public ToolBar() {
        ButtonGroup buttonGroup = new ButtonGroup();
        ActionHandler actionHandler = new ActionHandler();
        ChangeHandler changeHandler = new ChangeHandler();

        this.lineColor = Color.BLACK;
        this.fillColor = Color.white;
        this.lineColorImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        this.fillColorImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);

        for (ETools eTools : ETools.values()) {
            ImageIcon imageIcon = imageChange(eTools.getImageIcon(), 20, 20);
            JRadioButton toolButton = new JRadioButton(imageIcon);
            toolButton.setToolTipText(eTools.getLabel());
            toolButton.setActionCommand(eTools.name());
            toolButton.addActionListener(actionHandler);
            buttonGroup.add(toolButton);
            this.add(toolButton);
        }

        this.lineColorButton = new JButton("Line");
        this.lineColorButton.setToolTipText("색을 선택해주세요.");
        this.lineColorButton.addActionListener(actionHandler);
        this.lineColorButton.setIcon(new ImageIcon(lineColorImage));
        this.add(lineColorButton);

        this.fillColorButton = new JButton("Fill");
        this.fillColorButton.setToolTipText("색을 선택해주세요.");
        this.fillColorButton.addActionListener(actionHandler);
        this.setLineColorImage(Color.white, fillColorImage);
        this.fillColorButton.setIcon(new ImageIcon(fillColorImage));
        this.add(fillColorButton);

        this.strokeModel = new SpinnerNumberModel(1, 1, 16, 1);
        this.strokeSelect = new JSpinner(strokeModel);
        this.strokeSelect.addChangeListener(changeHandler);
        this.add(strokeSelect);

        this.fontSelect = new JComboBox(font);
        this.fontSelect.addActionListener(actionHandler);
        this.add(fontSelect);
    }

    private ImageIcon imageChange(ImageIcon icon, int x, int y) {
        Image img = icon.getImage();
        Image changeImg = img.getScaledInstance(x, y, Image.SCALE_SMOOTH);
        ImageIcon changeIcon = new ImageIcon(changeImg);
        return changeIcon;
    }


    private void setStroke() {
        int strokeValue = (int) strokeModel.getValue();
        this.drawingPanel.setStroke(strokeValue);
    }

    private void setLineColor() {
        lineColor = JColorChooser.showDialog(this, "색을 선택해 주세요", this.lineColor);
        setLineColorImage(lineColor, this.lineColorImage);
        drawingPanel.setLineColor(lineColor);
    }

    private void setFillColor() {
        fillColor = JColorChooser.showDialog(this, "색을 선택해 주세요", this.fillColor);
        setLineColorImage(fillColor, this.fillColorImage);
        drawingPanel.setFillColor(fillColor);
    }

    public void setLineColorImage(Color color, BufferedImage bufferedImage) {
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(color);
        graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graphics.dispose();
        this.repaint();
    }

    public void init(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        JRadioButton defaultButton = (JRadioButton) this.getComponent(ETools.eSelection.ordinal());
        defaultButton.doClick();
    }

    private void selectImage() {
        File file = null;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
        int reply = fileChooser.showOpenDialog(this.drawingPanel);
        if (reply == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }
        if (file != null) {
            ImageIcon image = new ImageIcon(String.valueOf(file));
            this.drawingPanel.setSelectImage(image);
        }

    }

    private void setFont() {
        String font = fontSelect.getSelectedItem().toString();
        this.drawingPanel.setFont(font);
    }


    private class ActionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == lineColorButton) {
                setLineColor();
            } else if (e.getSource() == fillColorButton) {
                setFillColor();
            }  else if (e.getSource() == fontSelect) {
                setFont();
            }else if (e.getActionCommand().equals(ETools.eImage.name())) {
                selectImage();
                drawingPanel.setSelectedTool(ETools.valueOf(e.getActionCommand()));
            } else {
                drawingPanel.setSelectedTool(ETools.valueOf(e.getActionCommand()));
            }
        }

    }


    private class ChangeHandler implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            setStroke();
        }
    }


}
