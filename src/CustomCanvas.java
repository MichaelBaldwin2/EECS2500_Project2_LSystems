/*
 * Written by: Mike Baldwin
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class CustomCanvas extends JPanel {
	private List<Line2D.Double> lines;

	public final void addLine(final Line2D.Double line) {
		lines.add(line);
	}

	public final void draw() {
		Graphics2D gfx = (Graphics2D) getGraphics();

		gfx.setColor(Color.blue);
		for (Line2D.Double line : lines)
			gfx.drawLine((int) Math.round(line.getX1()), (int) Math.round(line.getY1()), (int) Math.round(line.getX2()), (int) Math.round(line.getY2()));
	}

	public final void clear() {
		Graphics2D gfx = (Graphics2D) getGraphics();
		lines.clear();

		gfx.setColor(Color.white);
		gfx.clearRect(1, 1, getWidth() - 2, getHeight() - 2);
		gfx.fillRect(2, 2, getWidth() - 3, getHeight() - 3);
	}

	@Override
	public final void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);

		lines = new ArrayList<>();
		draw();
	}
}
