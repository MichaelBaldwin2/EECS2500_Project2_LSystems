/*
 * Written by: Mike Baldwin
 */

import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;


/**
 * Implements rule parsing and application for L-Systems.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 */
public final class Project2 {

	/**
	 * GUI frame.
	 */
	private static CustomFrame frame;
	/**
	 * Turtle graphics controller.
	 */
	private static Turtle turtle;

	/**
	 * Creates GUI and Turtle graphics controller.
	 *
	 * @param args Program args, ignored.
	 */
	public static void main(final String[] args) {
		frame = new CustomFrame();
		turtle = new Turtle(frame.getCanvas());
	}

	/**
	 * Calculates and draws an L-System.
	 *
	 * @param start         Axiom.
	 * @param rules         Rules to apply.
	 * @param iterations    Number of iterations to calculate.
	 * @param angle         Turning angle.
	 * @param lineLength    Length of each drawn line.
	 * @param startPosition Starting position (1 center, 2 corner, 3 bottom).
	 * @param vertical      True if the starting angle is vertical.
	 */
	public static void calculateLSystem(final String start, final String[] rules, final int iterations, final int angle, final int lineLength, final int startPosition, final boolean vertical) {
		String state = start;
		Map<Character, char[]> parsedRules = parseRules(rules);

		for (int i = 0; i < iterations; i++) {
			state = applyRules(parsedRules, state);
		}

		turtle.setProperties(state, angle, lineLength, startPosition, vertical);
		turtle.draw(2);
	}

	/**
	 * Parses user input and returns a map of rules to apply.
	 *
	 * @param rules Rules input.
	 * @return Map of productions.
	 */
	private static Map<Character, char[]> parseRules(final String[] rules) {
		Map<Character, char[]> parsedRules = new HashMap<Character, char[]>();

		for (String rule : rules) {
			rule = rule.replaceAll("[:\\s]", "");
			char[] parsedRule = rule.toCharArray();
			char constant = parsedRule[0];
			char[] production = Arrays.copyOfRange(parsedRule, 1, parsedRule.length);
			parsedRules.put(constant, production);

		}

		return parsedRules;
	}

	/**
	 * Calculates another iteration of the system.
	 *
	 * @param rules System rules.
	 * @param state Current state of the system.
	 * @return Next iteration of the system.
	 */
	private static String applyRules(final Map<Character, char[]> rules, final String state) {
		String newState = "";

		for (char item : state.toCharArray()) {
			if (rules.get(item) != null) {
				String p = new String(rules.get(item));
				newState += p;
			} else {
				newState += item;
			}
		}

		return newState;
	}

	/**
	 * Empty constructor. I DID THE THING HERE
	 */
	private Project2() {

	}
}

/**
 * Implements simple turtle graphics.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 */
class Turtle {

	/**
	 * Minimum distance from the border to start drawing from.
	 */
	private static final int OFFSET = 10;
	/**
	 * Vertical starting angle.
	 */
	private static final int V_ANGLE = 180;
	/**
	 * Horizontal starting angle.
	 */
	private static final int H_ANGLE = 90;

	/**
	 * Current turtle state.
	 */
	private TurtleState currentState;
	/**
	 * States stack.
	 */
	private Stack<TurtleState> stack;
	/**
	 * Canvas to draw on.
	 */
	private CustomCanvas canvas;
	/**
	 * State to draw.
	 */
	private String state;
	/**
	 * Turning angle.
	 */
	private int angle;
	/**
	 * Drawing length for each line.
	 */
	private int lineLength;

	/**
	 * Thread for drawing.
	 */
	private Thread drawingThread;

	/**
	 * Constructor.
	 *
	 * @param canvas Canvas to draw on.
	 */
	public Turtle(final CustomCanvas canvas) {
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
			this.currentState = new TurtleState(canvasWidth / 2, canvasHeight / 2, startAngle);
		else if (startPosition == 2) // Corner
			this.currentState = new TurtleState(OFFSET, canvasHeight + canvas.getInsets().bottom - OFFSET, startAngle);
		else
			// Bottom
			this.currentState = new TurtleState(canvasWidth / 2, canvasHeight + canvas.getInsets().bottom - OFFSET, startAngle);

		this.stack = new Stack<TurtleState>();
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

		drawingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					canvas.clear();

					for (char item : state.toCharArray()) {
						if (item == 'F' || item == 'G') {
							double nextX = currentState.x + (lineLength * Math.sin(Math.toRadians(currentState.angle)));
							double nextY = currentState.y + (lineLength * Math.cos(Math.toRadians(currentState.angle)));
							canvas.addLine(new Line2D.Double(currentState.x, currentState.y, nextX, nextY));
							currentState = new TurtleState(nextX, nextY, currentState.angle);

							TimeUnit.MILLISECONDS.sleep(delay);
						} else if (item == '+') {
							int nextAngle = currentState.angle + angle;
							currentState = new TurtleState(currentState.x, currentState.y, nextAngle);
						} else if (item == '-') {
							int nextAngle = currentState.angle - angle;
							currentState = new TurtleState(currentState.x, currentState.y, nextAngle);
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
			}
		});

		drawingThread.start();
	}

	/**
	 * Stores the state of the turtle in a given moment.
	 *
	 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo
	 * Amo</a>
	 */
	private final class TurtleState {

		/**
		 * X coordinate.
		 */
		public double x;
		/**
		 * Y coordinate.
		 */
		public double y;
		/**
		 * Angle.
		 */
		public int angle;

		/**
		 * Constructor.
		 *
		 * @param x     X coordinate.
		 * @param y     Y coordinate.
		 * @param angle Angle.
		 */
		private TurtleState(final double x, final double y, final int angle) {
			this.x = x;
			this.y = y;
			this.angle = angle;
		}
	}
}
