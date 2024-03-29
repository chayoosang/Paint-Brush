package frames;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
	//attribute
	private static final long serialVersionUID = 1L;

	//components
	private DrawingPanel drawingPanel;
	private MenuBar menuBar;
	private ToolBar toolBar;


	private Stroke stroke;
	private Color color;



	public MainFrame() {
		// attributes
		super("그림판");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 700);
		this.setLocation(100, 100);

		//components
		BorderLayout layoutManager = new BorderLayout();
		this.setLayout(layoutManager);

		this.color = Color.BLACK;
		this.stroke = new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,1.7f);

		this.drawingPanel = new DrawingPanel();
		this.add(drawingPanel, BorderLayout.CENTER);

		this.menuBar = new MenuBar();
		this.setJMenuBar(menuBar);

		this.toolBar = new ToolBar();
		this.add(toolBar, BorderLayout.NORTH);

	}

	public void setDrawingPanel(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}


    public void init() {
		this.toolBar.init(this.drawingPanel);
		this.menuBar.init(this.drawingPanel);
		this.drawingPanel.init();
    }
}

