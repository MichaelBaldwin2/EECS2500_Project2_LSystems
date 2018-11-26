/*
 * Written by: Mike Baldwin
 */

import java.awt.geom.Line2D;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class LSystem {
	private static final int OFFSET = 10;
	private static final int V_ANGLE = 180;
	private static final int H_ANGLE = 90;

	private SystemState currentState;
	private Stack<SystemState> stack;
	private CustomCanvas canvas;
	private String state;
	private int angle;
	private int lineLength;
	private Thread drawingThread;

	public LSystem(final CustomCanvas canvas) {
		this.canvas = canvas;
	}

	/**
	 * Changes the properties, used to start a new drawing.
	 *
	 * @param state         System state to draw.
	 * @param angle         Turning angle.
	 * @param lineLength    Drawing length for each line.
	 * @param startPosition Starting position (1 center, 2 corner, 3 bottom).
	 * @param vertical      True if the starting angle is vertical.
	 */
	public final void setProperties(final String state, final int angle, final int lineLength, final int startPosition, final boolean vertical) {
		double canvasWidth = canvas.getSize().width - canvas.getInsets().left - canvas.getInsets().right;
		double canvasHeight = canvas.getSize().height - canvas.getInsets().bottom - canvas.getInsets().top;
		int startAngle = vertical ? V_ANGLE : H_ANGLE;

		if (startPosition == 1) // Center
			this.currentState = new SystemState(canvasWidth / 2, canvasHeight / 2, startAngle);
		else if (startPosition == 2) // Corner
			this.currentState = new SystemState(OFFSET, canvasHeight + canvas.getInsets().bottom - OFFSET, startAngle);
		else
			// Bottom
			this.currentState = new SystemState(canvasWidth / 2, canvasHeight + canvas.getInsets().bottom - OFFSET, startAngle);

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
		if (drawingThread != null) {
			try {
				drawingThread.interrupt();
				drawingThread.join();
			} catch (InterruptedException e1) {
				return;
			}
		}

		drawingThread = new Thread(() -> {
			try {
				canvas.clear();

				for (char item : state.toCharArray()) {
					if (item == 'F' || item == 'G') {
						double nextX = currentState.x + (lineLength * Math.sin(Math.toRadians(currentState.angle)));
						double nextY = currentState.y + (lineLength * Math.cos(Math.toRadians(currentState.angle)));
						canvas.addLine(new Line2D.Double(currentState.x, currentState.y, nextX, nextY));
						currentState = new SystemState(nextX, nextY, currentState.angle);

						TimeUnit.MILLISECONDS.sleep(delay);
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
			} catch (InterruptedException e) {
				return;
			}
		});

		drawingThread.start();
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
