/*
 * Written by: Mike Baldwin
 */

import java.awt.geom.Line2D;
import java.util.Stack;

public class LSystem {
	private SystemState currentState;
	private Stack<SystemState> stack;
	private CustomCanvas canvas;
	private String state;
	private int angle;
	private int lineLength;

	public LSystem(final CustomCanvas canvas) {
		this.canvas = canvas;
	}

	/**
	 * Changes the properties, used to start a new drawing.
	 *
	 * @param state         System state to draw.
	 * @param angle         Turning angle.
	 * @param lineLength    Drawing length for each line.
	 */
	public final void setProperties(final String state, final int angle, final int lineLength) {
		double canvasWidth = canvas.getSize().width - canvas.getInsets().left - canvas.getInsets().right;
		double canvasHeight = canvas.getSize().height - canvas.getInsets().bottom - canvas.getInsets().top;
		currentState = new SystemState(canvasWidth / 2, canvasHeight / 2, 180);

		this.stack = new Stack<>();
		this.state = state;
		this.angle = angle;
		this.lineLength = lineLength;
	}

	/**
	 * Draws the current system, tick by tick.
	 *
	 * @param delay Time between ticks.
	 */
	public final void draw(final int delay) {
		canvas.clear();

		for (char item : state.toCharArray()) {
			if (item == 'F' || item == 'G') {
				double nextX = currentState.x + (lineLength * Math.sin(Math.toRadians(currentState.angle)));
				double nextY = currentState.y + (lineLength * Math.cos(Math.toRadians(currentState.angle)));
				canvas.addLine(new Line2D.Double(currentState.x, currentState.y, nextX, nextY));
				currentState = new SystemState(nextX, nextY, currentState.angle);
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else if (item == '+') {
				int nextAngle = currentState.angle + angle;
				currentState = new SystemState(currentState.x, currentState.y, nextAngle);
			} else if (item == '-') {
				int nextAngle = currentState.angle - angle;
				currentState = new SystemState(currentState.x, currentState.y, nextAngle);
			} else if (item == '[') {
				stack.push(currentState);
			} else if (item == ']') {
				if (!stack.isEmpty()) currentState = stack.pop();
			}

			canvas.draw();
		}
	}

	private final class SystemState {
		public double x;
		public double y;
		public int angle;

		private SystemState(final double x, final double y, final int angle) {
			this.x = x;
			this.y = y;
			this.angle = angle;
		}
	}
}
