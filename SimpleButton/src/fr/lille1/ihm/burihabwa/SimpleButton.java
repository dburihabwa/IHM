package fr.lille1.ihm.burihabwa;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.ClickOnShape;
import fr.lri.swingstates.canvas.transitions.EnterOnShape;
import fr.lri.swingstates.canvas.transitions.LeaveOnShape;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;

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
		rectangle = canvas.newRectangle(50, 50, 100, 100);
		label = canvas.newText(x, y, text, new Font("verdana", Font.PLAIN, 12));
		rectangle.translateTo(label.getCenterX(), label.getCenterY());
		rectangle.setParent(label);
		final CExtensionalTag tag = new CExtensionalTag(canvas) {
			public void added(CShape shape) {
				shape.setOutlined(true);
			}

			public void removed(CShape shape) {
				shape.setOutlined(false);
			}
		};
		stateMachine = new CStateMachine() {
			State state;
			public State idle = new State() {
				Transition enterOnShape = new EnterOnShape(">> hover") {
					@Override
					public void action() {
//						super.action();
//						state = hover;
						getShape().addTag(tag);
						System.out.println("Entered the shape!");
					}
				};
			};
			public State hover = new State() {
				Transition onLeaveShape = new LeaveOnShape(">> idle") {
					@Override
					public void action() {
//						super.action();
//						state = idle;
						getShape().removeTag(tag);
						System.out.println("Left the shape!");
					}
				};
				Transition onClickShape = new ClickOnShape(">> clicked") {
					@Override
					public void action() {
						// TODO Auto-generated method stub
						super.action();
						getShape().setFillPaint(Color.YELLOW);
						System.out.println("Clicked on the button!");
					}
					
				};

			};
			public State clicked = new State() {
				Transition onClickShape = new ClickOnShape(">> clicked") {
					@Override
					public void action() {
						// TODO Auto-generated method stub
						super.action();
						getShape().setFillPaint(Color.YELLOW);
						System.out.println("Clicked on the button!");
					}
					
				};
			};
		};
		stateMachine.attachTo(canvas);
	}

	public void action() {
		System.out.println("ACTION!");
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
		SimpleButton simpleButton = new SimpleButton(canvas, "PLOP");
		frame.getContentPane().add(canvas);
		frame.setTitle("Première machine à états sur un canevas");
		frame.setVisible(true);
		
//		StateMachineVisualization smv = new StateMachineVisualization(
//				simpleButton.stateMachine);
//		frame.add(smv);

		frame.pack();
	}

}
