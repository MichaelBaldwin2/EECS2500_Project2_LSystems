/*
 * Written by: Mike Baldwin
 */

import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Project2 {
	private static SystemState currentState;
	private static Stack<SystemState> savedStateStack;
	private static CustomCanvas customCanvas;
	private static String state;
	private static int angle;
	private static int lineLength;
	private static boolean instantDraw;

	public static void calculateLSystem(final CustomCanvas customCanvas, String start, String[] rules, int iterations, int angle, int lineLength, boolean instantDraw) {
		Project2.customCanvas = customCanvas;
		String state = start;
		Map<Character, char[]> parsedRules = parseRules(rules);

		for (int i = 0; i < iterations; i++) {
			state = applyRules(parsedRules, state);
		}

		setProperties(state, angle, lineLength, instantDraw);
		draw(2);
	}

	private static Map<Character, char[]> parseRules(final String[] rules) {
		Map<Character, char[]> parsedRules = new HashMap<>();

		for (String rule : rules) {
			rule = rule.replaceAll("[:\\s]", "");
			char[] parsedRule = rule.toCharArray();
			char constant = parsedRule[0];
			char[] production = Arrays.copyOfRange(parsedRule, 1, parsedRule.length);
			parsedRules.put(constant, production);
		}

		return parsedRules;
	}

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
	 * Changes the properties, used to start a new drawing.
	 *
	 * @param state      System state to draw.
	 * @param angle      Turning angle.
	 * @param lineLength Drawing length for each line.
	 */
	private static void setProperties(final String state, final int angle, final int lineLength, boolean instantDraw) {
		double canvasWidth = customCanvas.getSize().width - customCanvas.getInsets().left - customCanvas.getInsets().right;
		double canvasHeight = customCanvas.getSize().height - customCanvas.getInsets().bottom - customCanvas.getInsets().top;
		currentState = new SystemState(canvasWidth / 2, canvasHeight / 2, 180);
		Project2.instantDraw = instantDraw;

		savedStateStack = new Stack<>();
		Project2.state = state;
		Project2.angle = angle;
		Project2.lineLength = lineLength;
	}

	/**
	 * Draws the current system, tick by tick.
	 *
	 * @param delay Time between ticks.
	 */
	private static void draw(final int delay) {
		customCanvas.clear();

		for (char item : state.toCharArray()) {
			switch (item) {
				case 'F': {
					double nextX = currentState.x + (lineLength * Math.sin(Math.toRadians(currentState.angle)));
					double nextY = currentState.y + (lineLength * Math.cos(Math.toRadians(currentState.angle)));
					customCanvas.addLine(new Line2D.Double(currentState.x, currentState.y, nextX, nextY));
					currentState = new SystemState(nextX, nextY, currentState.angle);
					if (!instantDraw) {
						try {
							Thread.sleep(delay * 100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				break;
				case '+': {
					int nextAngle = currentState.angle + angle;
					currentState = new SystemState(currentState.x, currentState.y, nextAngle);
				}
				break;
				case '-': {
					int nextAngle = currentState.angle - angle;
					currentState = new SystemState(currentState.x, currentState.y, nextAngle);
				}
				break;
				case '[':
					savedStateStack.push(currentState);
					break;
				case ']':
					if (!savedStateStack.isEmpty())
						currentState = savedStateStack.pop();
					break;
			}

			if (!instantDraw)
				customCanvas.draw();
		}
		customCanvas.draw();
	}

	public static void main(final String[] args) {
		CustomFrame frame = new CustomFrame();
	}

	private static class SystemState {
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
