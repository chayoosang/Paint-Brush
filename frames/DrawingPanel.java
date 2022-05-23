package frames;

import global.Constants.EAnchors;
import global.Constants.ETools;
import global.Constants.ETransformationStyle;
import shapes.TSelection;
import shapes.TShape;
import transformer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class DrawingPanel extends JPanel {


	//attribute - 자기 자신을 나타내는 값 (추상적)
	//components - 자기 자신을 나태내는 구체적인 구성품


	//associated attribute - 나와 통신하기 위해서 만들어진 값 (연결된 속성)
	//싱크로노서 커뮤니케이션 - 전화 (동시)
	//에이싱크로너서 커뮤니케이션 - 우체통 (동시 X)

	//	private ETools eSelectedTool;
	private enum EDrawingState {
		eIdle,
		e2PointDrawing,
		eNPointDrawing,
	}

	EDrawingState edrawingState;
	private boolean update;

	private Vector<TShape> shapes;
	private Vector<TimeShape> undoShape;
	private Vector<TimeShape> redoShape;

	private Image bufferImage;
	private Graphics2D bufferGraphics;

	private int strokeValue;
	private Color lineColor;
	private Color fillColor;

	private ETools selectedTool;
	private TShape currentShape;
	private TextField input;
	private Transformer transformer;



	public DrawingPanel() {
		MyKeyHandler myKeyHandler = new MyKeyHandler();
		this.edrawingState = EDrawingState.eIdle;
		this.update = false;

		this.input = new TextField();
		this.input.setVisible(false);

		this.input.addKeyListener(myKeyHandler);
		this.add(this.input);

		this.shapes = new Vector<>();
		this.undoShape = new Vector<>();
		this.redoShape = new Vector<>();
		this.strokeValue = 1;

		MouseHandler mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
		this.addMouseWheelListener(mouseHandler);
		this.setBackground(Color.WHITE);

		this.transformer = null;
	}

	public boolean isUpdated() {
		return this.update;
	}

	public void setUpdated(boolean update) {
		this.update = update;
	}

	@SuppressWarnings("unchecked")
	public void setShapes(Object shapes) {
		this.shapes = (Vector<TShape>) shapes;
		this.edrawingState = EDrawingState.eIdle;
		this.drawDoubleBuffer();
		this.repaint();
	}

	public Vector<TimeShape> getUndoShape() {
		return undoShape;
	}

	public void setUndoShape(Vector<TimeShape> undoShape) {
		this.undoShape = undoShape;
	}

	public Vector<TimeShape> getRedoShape() {
		return redoShape;
	}

	public void setRedoShape(Vector<TimeShape> redoShape) {
		this.redoShape = redoShape;
	}

	public Object getShapes() {
		return  this.shapes;
	}

	public TShape getOnShape() {
		return currentShape;
	}

	public Image getImage() {
		Image image = createImage(this.getWidth(), this.getHeight());
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		return image;
	}

	public void setSelectedTool(ETools selectedTool) {
		this.selectedTool = selectedTool;
		if (this.currentShape != null) {
			this.edrawingState = EDrawingState.eIdle;
		}
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public void setStroke(int strokeValue) {
		this.strokeValue = strokeValue;
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		super.paint(graphics2D);
//		if (this.bufferImage != null) {
//			graphics2D.drawImage(this.bufferImage, 0, 0, null);
//		}
		for (TShape shape : shapes) {
			shape.draw(graphics2D);
		}

	}

	@Override
	public void update(Graphics g) {
		print(g);
	}



	private boolean onShape(int x, int y) {
		for (TShape shape : this.shapes) {
			if (shape.contains(x, y)) {
				return true;
			}
		}
		return false;
	}

	private TShape selectShape(int x, int y) {
		for (TShape shape : this.shapes) {
			if (shape.contains(x, y)) {
				return shape;
			}
		}
		return null;
	}

	private void selected(TShape selectShape) {
		Graphics2D graphics  = (Graphics2D) this.getGraphics();
		graphics.setXORMode(this.getBackground());

		if (this.currentShape != null && !(this.currentShape instanceof TSelection)) {
			this.currentShape.setSelected(false);
			this.currentShape.drawAnchors(graphics);
		}

		this.currentShape = selectShape;
		this.currentShape.setSelected(true);
	}



	private void changeCursor(int x, int y) {
		Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
		if (this.selectedTool == ETools.eSelection) {
			cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
			if (onShape(x, y)) {
				EAnchors eAnchor = this.currentShape.getSelectAnchors();
				if (eAnchor != null) {
					cursor = eAnchor.getCursor();
				} else {
					cursor = new Cursor(Cursor.MOVE_CURSOR);
				}
			}
		}
		this.setCursor(cursor);
	}

	public void selectDraw(int x, int y) {

		TShape shape = selectShape(x, y);
		if (selectedTool == ETools.eSelection) {
			if (shape == null) {
				this.currentShape = this.selectedTool.getTool();
				this.transformer = new Drawer(this.currentShape);
			} else {
				if (shape.getSelectAnchors() == EAnchors.eRR) {
					this.transformer = new Rotator(shape);
					selected(shape);
				} else if (shape.getSelectAnchors() == null) {
					this.transformer = new Translator(shape);
					selected(shape);
				} else {
					this.transformer = new Resizer(shape);
					selected(shape);
				}
			}
		} else {
			shape = this.selectedTool.getTool();
			shape.setLineColor(this.lineColor);
			shape.setFillColor(this.fillColor);
			shape.setStrokeValue(this.strokeValue);
			this.transformer = new Drawer(shape);
			selected(shape);
		}

	}

	public void prepareTransforming(int x, int y) {
		Graphics2D graphics = (Graphics2D) this.getGraphics();
		graphics.setXORMode(this.getBackground());
		this.transformer.prepare(x, y, graphics);
	}

	public void keepTransforming(int x, int y) {
		Graphics2D graphics = (Graphics2D) this.getGraphics();
		graphics.setXORMode(this.getBackground());
		this.transformer.keep(x, y, graphics, this.getImage(), this.getCursor());
	}

	public void continueTransform(int x, int y) {
		Graphics2D graphics = (Graphics2D) this.getGraphics();
		graphics.setXORMode(this.getBackground());
		this.transformer.continueTransform(x, y, graphics);
	}

	private void finishTransforming(int x, int y) {
		Graphics2D graphics = (Graphics2D) this.getGraphics();
		graphics.setXORMode(this.getBackground());

		if (this.currentShape instanceof TSelection) {
			repaint();
		} else {
			this.transformer.finish(x,y,graphics, shapes);
			this.currentShape.drawAnchors(graphics);
//			this.undoShape.add(new TimeShape(this.currentShape, this.currentShape.getBounds()));
//			this.redoShape.add(new TimeShape(this.currentShape, this.currentShape.getBounds()));
			this.drawDoubleBuffer();
			this.setUpdated(true);
		}

	}


//
//	private void prepareText() {
//		if (this.onShape instanceof TTextBox) {
//			this.input.setText(((TTextBox) this.onShape).getText());
//			this.input.setBounds(this.onShape.getBounds());
//			this.input.setVisible(true);
//		}
//	}
//
//	private void drawingText() {
//		Graphics2D graphics  = (Graphics2D) this.getGraphics();
//		this.input.setVisible(false);
//
//		String text = this.input.getText();
//
//
//		if (this.onShape instanceof TTextBox) {
//			((TTextBox) this.onShape).inputText(text);
//			((TTextBox) this.onShape).drawText(graphics);
//
//			int index = shapes.indexOf(onShape);
//			this.shapes.set(index, onShape);
//			this.drawDoubleBuffer();
//		}
//	}
//




	private void drawDoubleBuffer() {
		this.bufferImage = getImage();
		this.bufferGraphics = (Graphics2D) this.bufferImage.getGraphics();
		for (TShape shape : shapes) {
			shape.draw(this.bufferGraphics);
		}
	}

	public class TimeShape {
		private TShape shape;
		private Rectangle rectangle;

		public TimeShape(TShape shape, Rectangle rectangle) {
			this.shape = shape;
			this.rectangle = rectangle;
		}

		public TShape getShape() {
			return shape;
		}

		public Rectangle getRectangle() {
			return rectangle;
		}

	}





	/*
	* 다형성이라는 것은 제목만 같고 내용은 다른것
	* Class의 함수적 interface만 동일하면 어느 시점의 특정 형태로서 같이 작동 할 수 있는것
	* */

	private class MyKeyHandler implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//					drawingText();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}



	private class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getClickCount() == 1) {
					lButtonClick(e);
				} else if (e.getClickCount() == 2) {
					lButtonDoubleClick(e);
				}
			}
		}

		private void lButtonClick(MouseEvent e) {
			if (edrawingState == EDrawingState.eIdle) {
				if (selectedTool.getTransformationStyle() == ETransformationStyle.eNPTransformation) {
					prepareTransforming(e.getX(), e.getY());
					edrawingState = EDrawingState.eNPointDrawing;
				}
			} else if (edrawingState == EDrawingState.eNPointDrawing) {
				continueTransform(e.getX(), e.getY());
			}
		}

		private void lButtonDoubleClick(MouseEvent e) {
			if (edrawingState == EDrawingState.eNPointDrawing) {
				finishTransforming(e.getX(), e.getY());
				edrawingState = EDrawingState.eIdle;
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (edrawingState == EDrawingState.eNPointDrawing) {
				keepTransforming(e.getX(), e.getY());
			} else if (edrawingState == EDrawingState.eIdle) {
				changeCursor(e.getX(), e.getY());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(edrawingState == EDrawingState.eIdle) {
				selectDraw(e.getX(), e.getY());
				if (selectedTool.getTransformationStyle() == ETransformationStyle.e2PTransformation) {
					prepareTransforming(e.getX(), e.getY());
					edrawingState = EDrawingState.e2PointDrawing;
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (edrawingState == EDrawingState.e2PointDrawing) {
				keepTransforming(e.getX(), e.getY());
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (edrawingState == EDrawingState.e2PointDrawing) {
				finishTransforming(e.getX(), e.getY());
				edrawingState = EDrawingState.eIdle;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		}
	}




}

