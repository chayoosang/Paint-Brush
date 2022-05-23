package frames;

import global.Constants.ETools;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ToolBar extends JToolBar {

    //components
    private JButton lineColorButton;
    private JButton fillColorButton;
    private SpinnerNumberModel strokeModel;
    private JSpinner strokeSelect;

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

        this.lineColorButton = new JButton("LineColor");
        this.lineColorButton.setToolTipText("색을 선택해주세요.");
        this.lineColorButton.addActionListener(actionHandler);
        this.lineColorButton.setIcon(new ImageIcon(lineColorImage));
        this.add(lineColorButton);

        this.fillColorButton = new JButton("FillColor");
        this.fillColorButton.setToolTipText("색을 선택해주세요.");
        this.fillColorButton.addActionListener(actionHandler);
        this.setLineColorImage(Color.white, fillColorImage);
        this.fillColorButton.setIcon(new ImageIcon(fillColorImage));
        this.add(fillColorButton);

        this.strokeModel = new SpinnerNumberModel(1, 1, 16, 1);
        this.strokeSelect = new JSpinner(strokeModel);
        this.strokeSelect.addChangeListener(changeHandler);
        this.add(strokeSelect);


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

    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        JRadioButton defaultButton =  (JRadioButton) this.getComponent(ETools.eSelection.ordinal());
        defaultButton.doClick();
    }


    private class ActionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == lineColorButton) {
                setLineColor();
            } else if (e.getSource() == fillColorButton) {
                setFillColor();
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
