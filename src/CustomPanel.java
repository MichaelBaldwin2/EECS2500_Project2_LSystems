/*
 * Written by: Mike Baldwin
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * All this class does is perform the drawing of the lines and the LSystemState record within a stack.
 */
public class CustomPanel extends JPanel {
	private List<Line2D.Double> lines = new ArrayList<>();

	/**
	 * This method will draw the expanded rules onto the panel screen.
	 *
	 * @param expandedString The expanded string to parse and draw.
	 * @param angle          The angle delta to apply each time an angle change is requested.
	 * @param lineLength     The length of the line.
	 */
	public void draw(String expandedString, double angle, double lineLength) {
		LSystemState currentState = new LSystemState(getWidth() / 2d, getHeight() / 2d, 180);
		Stack<LSystemState> savedStateStack = new Stack<>();

		//First we clear the panel of old lines
		Graphics2D gfx = (Graphics2D) getGraphics();
		gfx.setColor(Color.white);
		gfx.clearRect(1, 1, getWidth() - 2, getHeight() - 2);
		gfx.fillRect(2, 2, getWidth() - 3, getHeight() - 3);

		//Now we loop through the expanded string, performing whatever operations are there, skipping any non-required ops.
		for (char item : expandedString.toCharArray()) {
			switch (item) {
				case 'F': {
					double nextX = currentState.getX() + (lineLength * Math.sin(Math.toRadians(currentState.getAngle())));
					double nextY = currentState.getY() + (lineLength * Math.cos(Math.toRadians(currentState.getAngle())));
					Line2D.Double line = new Line2D.Double(currentState.getX(), currentState.getY(), nextX, nextY);
					currentState = new LSystemState(nextX, nextY, currentState.getAngle());
					gfx.setColor(Color.black);
					gfx.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
				}
				break;
				case '+': {
					double nextAngle = currentState.getAngle() + angle;
					currentState = new LSystemState(currentState.getX(), currentState.getY(), nextAngle);
				}
				break;
				case '-': {
					double nextAngle = currentState.getAngle() - angle;
					currentState = new LSystemState(currentState.getX(), currentState.getY(), nextAngle);
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
		}
	}
}
