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
    private JButton colorButton;
    private SpinnerNumberModel strokeModel;
    private JSpinner strokeSelect;

    private BufferedImage colorSample;
    private Color color;

    //associations
    private DrawingPanel drawingPanel;



    public ToolBar() {
        ButtonGroup buttonGroup = new ButtonGroup();
        ActionHandler actionHandler = new ActionHandler();
        ChangeHandler changeHandler = new ChangeHandler();

        this.color = Color.BLACK;
        this.colorSample = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);

        for (ETools eTools : ETools.values()) {
            ImageIcon imageIcon = imageChange(eTools.getImageIcon(), 20, 20);
            JRadioButton toolButton = new JRadioButton(imageIcon);
            toolButton.setToolTipText(eTools.getLabel());
            toolButton.setActionCommand(eTools.name());
            toolButton.addActionListener(actionHandler);
            buttonGroup.add(toolButton);
            this.add(toolButton);
        }

        this.colorButton = new JButton("Color");
        this.colorButton.setToolTipText("색을 선택해주세요.");
        this.colorButton.addActionListener(actionHandler);
        this.colorButton.setIcon(new ImageIcon(colorSample));
        this.add(colorButton);

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

    private void setColor() {
        this.color = JColorChooser.showDialog(this, "색을 선택해 주세요", this.color);
        setColorSample(color);
        drawingPanel.setColor(color);
    }

    public void setColorSample(Color color) {
    	 Graphics2D graphics = this.colorSample.createGraphics();
         graphics.setColor(color);
         graphics.fillRect(0, 0, this.colorSample.getWidth(), this.colorSample.getHeight());
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
            if (e.getSource() == colorButton) {
                setColor();
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
