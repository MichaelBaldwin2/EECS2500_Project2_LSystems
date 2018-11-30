/*
 * Written by: Mike Baldwin
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CustomPanel extends JPanel {
	private List<Line2D.Double> lines = new ArrayList<>();

	public void draw(String expandedString, double angle, double lineLength, boolean instantDraw) {
		clear();
		LSystemState currentState = new LSystemState(200, 200, 90);
		Stack<LSystemState> savedStateStack = new Stack<>();

		for (char item : expandedString.toCharArray()) {
			switch (item) {
				case 'F': {
					double nextX = currentState.getX() + (lineLength * Math.sin(Math.toRadians(currentState.getAngle())));
					double nextY = currentState.getY() + (lineLength * Math.cos(Math.toRadians(currentState.getAngle())));
					lines.add(new Line2D.Double(currentState.getX(), currentState.getY(), nextX, nextY));
					currentState = new LSystemState(nextX, nextY, currentState.getAngle());
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

			if (!instantDraw)
				draw();
		}
		draw();
	}

	public void draw() {
		Graphics2D gfx = (Graphics2D) getGraphics();

		gfx.setColor(Color.blue);
		for (Line2D.Double line : lines)
			gfx.drawLine((int) Math.round(line.getX1()), (int) Math.round(line.getY1()), (int) Math.round(line.getX2()), (int) Math.round(line.getY2()));
	}

	public void clear() {
		Graphics2D gfx = (Graphics2D) getGraphics();
		lines.clear();

		gfx.setColor(Color.white);
		gfx.clearRect(1, 1, getWidth() - 2, getHeight() - 2);
		gfx.fillRect(2, 2, getWidth() - 3, getHeight() - 3);
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		lines = new ArrayList<>();
		draw();
	}
}
