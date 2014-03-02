package fr.lille1.ihm.burihabwa;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import javax.swing.JFrame;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.EnterOnShape;
import fr.lri.swingstates.canvas.transitions.LeaveOnShape;
import fr.lri.swingstates.canvas.transitions.PressOnShape;
import fr.lri.swingstates.canvas.transitions.ReleaseOnShape;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Release;
import fr.lri.swingstates.sm.transitions.TimeOut;

/**
 * @author Nicolas Roussel (roussel@lri.fr)
 * 
 */
public class SimpleButton {

	private CText label;
	private CRectangle rectangle;
	private CStateMachine stateMachine;
	static int times = 0;

	SimpleButton(final Canvas canvas, final String text) {
		int x = 0, y = 0;
		rectangle = canvas.newRectangle(100, 100, 100, 100);
		label = canvas.newText(x, y, text, new Font("verdana", Font.PLAIN, 12));
		rectangle.translateTo(label.getCenterX(), label.getCenterY());
		rectangle.setParent(label);
		final CExtensionalTag boxTag = new CExtensionalTag(canvas) {
			public void added(CShape shape) {
				shape.setOutlined(true);
			}

			public void removed(CShape shape) {
				shape.setOutlined(false);
			}
		};
		final CExtensionalTag colorTag = new CExtensionalTag(canvas) {
			Paint initColor = null;
			Paint actColor = Color.YELLOW;

			@Override
			public void added(CShape shape) {
				super.added(shape);
				if (initColor == null) {
					initColor = shape.getFillPaint();
				}
				shape.setFillPaint(actColor);
			}

			@Override
			public void removed(CShape shape) {
				super.removed(shape);
				shape.setFillPaint(initColor);
			}
		};

		stateMachine = new CStateMachine() {
			public State idle = new State() {
				Transition enterOnShape = new EnterOnShape(">> over") {
					@Override
					public void action() {
						super.action();
						getShape().addTag(boxTag);
					}
				};
			};
			public State over = new State() {
				Transition onLeaveShape = new LeaveOnShape(">> idle") {
					@Override
					public void action() {
						super.action();
						getShape().removeTag(boxTag);
					}
				};
				Transition onPressShape = new PressOnShape(">> activated") {
					@Override
					public void action() {
						super.action();
						getShape().addTag(colorTag);
					}

				};
			};
			public State activated = new State() {
				Transition onLeaveShape = new LeaveOnShape(">> deactivated") {
					@Override
					public void action() {
						super.action();
						getShape().removeTag(colorTag);
						getShape().removeTag(boxTag);
					}
				};
				Transition onReleaseShape = new ReleaseOnShape(">> over") {
					@Override
					public void action() {
						super.action();
						getShape().removeTag(colorTag);
					}
				};
				Transition timeout = new TimeOut(">> activated") {
					@Override
					public void action() {
						super.action();
						System.out.println("DEMI-CLICK!");
					}
				};
			};
			public State deactivated = new State() {

				Transition onReleaseShape = new Release(">> idle") {
					@Override
					public void action() {
						super.action();
						getShape().removeTag(boxTag);
					}
				};
				Transition onEnterShape = new EnterOnShape(">> activated") {
					@Override
					public void action() {
						super.action();
						getShape().addTag(colorTag);
					}
				};
			};
		};
		stateMachine.attachTo(rectangle);
	}

	public CShape getShape() {
		return label;
	}

	public CRectangle getRectangle() {
		return rectangle;
	}

	static public void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Canvas canvas = new Canvas(400, 400);
		new SimpleButton(canvas, "simple");
		frame.getContentPane().add(canvas);
		frame.setTitle("Première machine à états sur un canevas");
		frame.setVisible(true);

		// StateMachineVisualization smv = new StateMachineVisualization(
		// simpleButton.stateMachine);
		// frame.add(smv);

		frame.pack();
	}

}
