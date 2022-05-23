
package global;

import shapes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Constants {

	public enum ETransformationStyle {
		e2PTransformation,
		eNPTransformation
	}


	//static object는 상수임 -> 프로그램이 시작되는 시점에서 생성되고 고정된다.
	public enum ETools {

		eSelection("선택", new TSelection(),new ImageIcon("src/media/select.png"), ETransformationStyle.e2PTransformation),
		eRectangle("네모", new TRectangle(),new ImageIcon("src/media/rectangle.png"), ETransformationStyle.e2PTransformation),
		eOval("동그라미", new TOval(), new ImageIcon("src/media/oval.png"), ETransformationStyle.e2PTransformation),
		eRegularTriangle("정삼각형", new TRegularTriangle(), new ImageIcon("src/media/regularTriangle.png"), ETransformationStyle.e2PTransformation),
		eRightTriangle("직삼각형", new TRightTriangle(), new ImageIcon("src/media/rightTriangle.png"), ETransformationStyle.e2PTransformation),
		ePentagon("오각형", new TPentagon(), new ImageIcon("src/media/polygon.png"), ETransformationStyle.e2PTransformation),
		eHexagon("육각형", new THexagon(), new ImageIcon("src/media/hexagon.png"), ETransformationStyle.e2PTransformation),
		eLine("직선", new TLine(),  new ImageIcon("src/media/line.png"), ETransformationStyle.e2PTransformation),
		ePolyLine("다각선", new TPolyLine(),new ImageIcon("src/media/polyline.png"), ETransformationStyle.eNPTransformation),
		eCurve("곡선", new TCurve(), new ImageIcon("src/media/curve.png"), ETransformationStyle.eNPTransformation),
		eText("텍스트", new TTextBox(), new ImageIcon("src/media/textbox.png"), ETransformationStyle.e2PTransformation),;

		private final String label;
		private final TShape tool;
		private final ImageIcon imageIcon;
		private ETransformationStyle eTransformationStyle;


		ETools(String label, TShape tool, ImageIcon imageIcon, ETransformationStyle eTransformationStyle) {
			this.label = label;
			this.tool = tool;
			this.imageIcon = imageIcon;
			this.eTransformationStyle = eTransformationStyle;
		}

		public String getLabel() {
			return this.label;
		}
        public TShape getTool() {
			return this.tool.clone();
        }

		public ImageIcon getImageIcon() {
			return this.imageIcon;
		}

		public ETransformationStyle getTransformationStyle() {
			return this.eTransformationStyle;
		}
	}

	public enum EFileMenu{
		eNew("새로만들기", KeyStroke.getKeyStroke('N', InputEvent.CTRL_MASK)),
		eOpen("열기", KeyStroke.getKeyStroke('O', InputEvent.CTRL_MASK)),
		eSave("저장하기", KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK)),
		eSaveAs("다른이름으로 저장하기", KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK ^ InputEvent.SHIFT_MASK)),
		ePrint("프린트", KeyStroke.getKeyStroke('P', InputEvent.CTRL_MASK)),
		eQuit("종료", KeyStroke.getKeyStroke('Q', InputEvent.CTRL_MASK));


		private final String label;
		private final KeyStroke keyStroke;

		EFileMenu(String label , KeyStroke keyStroke) {
			this.label = label;
			this.keyStroke = keyStroke;
		}

		public String getLabel() {
			return this.label;
		}

		public KeyStroke getKeyStroke() {
			return keyStroke;
		}
	}

	public enum EEditMenu{
		eCut("잘라내기", KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK)),
		eCopy("복사하기", KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK)),
		ePaste("붙여넣기", KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK)),
		eDelete("삭제하기", KeyStroke.getKeyStroke('D', InputEvent.CTRL_MASK)),
		eUndo("실행 취소", KeyStroke.getKeyStroke('Z', InputEvent.CTRL_MASK)),
		eRedo("다시 실행", KeyStroke.getKeyStroke('Y', InputEvent.CTRL_MASK));


		private final String label;
		private final KeyStroke keyStroke;

		EEditMenu(String label , KeyStroke keyStroke) {
			this.label = label;
			this.keyStroke = keyStroke;
		}

		public String getLabel() {
			return this.label;
		}

		public KeyStroke getKeyStroke() {
			return keyStroke;
		}
	}

	public enum EAnchors {

		eNW(new Cursor(Cursor.NW_RESIZE_CURSOR)) {
			public void setLine(Rectangle bounds) {
				this.oval.setFrame(bounds.getX() - 5, bounds.getY() - 5, 10, 10);
			}
		},
		eWW(new Cursor(Cursor.W_RESIZE_CURSOR))  {
			public void setLine(Rectangle bounds) {
				this.oval.setFrame(bounds.getX() - 5, bounds.getY() + bounds.getHeight() / 2 , 10, 10);
			}
		},
		eSW(new Cursor(Cursor.SW_RESIZE_CURSOR)) {
			public void setLine(Rectangle bounds) {
				this.oval.setFrame(bounds.getX() - 5, bounds.getY() + bounds.getHeight() - 5, 10, 10);
			}
		},
		eNN(new Cursor(Cursor.N_RESIZE_CURSOR)) {
			public void setLine(Rectangle bounds) {
				this.oval.setFrame(bounds.getX() + bounds.getWidth() / 2 -5 , bounds.getY() - 5, 10, 10);
			}
		},
		eSS( new Cursor(Cursor.S_RESIZE_CURSOR)) {
			public void setLine(Rectangle bounds) {
				this.oval.setFrame(bounds.getX() + bounds.getWidth() / 2 -5 , bounds.getY() + bounds.getHeight() - 5, 10, 10);
			}
		},
		eNE( new Cursor(Cursor.NE_RESIZE_CURSOR)) {
			public void setLine(Rectangle bounds) {
				this.oval.setFrame(bounds.getX() + bounds.getWidth() - 5, bounds.getY() - 5, 10, 10);
			}
		},
		eEE(new Cursor(Cursor.E_RESIZE_CURSOR)) {
			public void setLine(Rectangle bounds) {
				this.oval.setFrame(bounds.getX() + bounds.getWidth() - 5, bounds.getY() + bounds.getHeight() / 2, 10, 10);
			}
		},
		eSE(new Cursor(Cursor.SE_RESIZE_CURSOR)) {
			public void setLine(Rectangle bounds) {
				this.oval.setFrame(bounds.getX() + bounds.getWidth() - 5, bounds.getY() + bounds.getHeight() - 5, 10, 10);
			}
		},
		eRR(new Cursor(Cursor.HAND_CURSOR)) {
			public void setLine(Rectangle bounds) {
				this.oval.setFrame(bounds.getX() + bounds.getWidth() /2 - 5, bounds.getY()  - 30, 10, 10);
			}
		},
		;
		protected Ellipse2D oval;
		private Cursor cursor;

		EAnchors(Cursor cursor) {
			this.oval = new Ellipse2D.Double();
			this.cursor = cursor;
		}

		public abstract void setLine(Rectangle bounds);

		public Cursor getCursor() {
			return this.cursor;
		}

		public Rectangle2D getLine(){
			return this.oval.getBounds2D();
		}



	}
}
