/*
 * Written by: Mike Baldwin
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
	IMPORTANT INFO:
		I don't separate the rules by boxes, instead I separate them by equal signs and by new lines.
		So each rule should have a single character on the LHS of the equal sign, and then whatever you want on the RHS.
		When you want to add more than one rule, you MUST use the enter/return key to add a new line and then you can write the new rule.
		The reason I do this is to allow more than 5 rules to be written. I know the requirements only call for 5, but I like to live dangerously :)

		---------------------------------------------------
		Ex:
			Start Symbol: F

			Rules:
					F=F+G+F+G
					G=F+F

		---------------------------------------------------
 */

/**
 * Project 2 class. Sets up the GUI and performs actions on button presses.
 */
public class Project2 extends JFrame implements ActionListener {
	private CustomPanel drawPanel;
	private JTextField startTextField;
	private JTextArea rulesTextArea;
	private JTextField iterationsTextField;
	private JTextField angleTextField;
	private JSlider lineLengthSlider;

	/**
	 * Main method, nuff said.
	 */
	public static void main(final String[] args) {
		new Project2();
	}

	/**
	 * Constructor. Private because it's only ever used within this class itself.
	 */
	private Project2() {
		setTitle("EECS2500 - Project 2 - L Systems");
		initUI();
		pack();
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Sets up the GUI.
	 */
	private void initUI() {
		//Construct the main drawPanel that encompasses all
		JPanel mainPanel = new JPanel(new GridBagLayout());
		getContentPane().add(mainPanel);

		//Construct the menu drawPanel that holds all the user configurable settings
		JPanel menuPanel = new JPanel(new GridBagLayout());
		menuPanel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 5)));

		//Construct and setup the canvas properties
		drawPanel = new CustomPanel();
		drawPanel.setBackground(Color.white);
		drawPanel.setPreferredSize(new Dimension(550, 550));
		drawPanel.setBorder(new LineBorder(Color.black));

		//Main constraints object used
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		//START SYMBOL
		menuPanel.add(new JLabel("Start Symbol"), constraints);
		menuPanel.add((startTextField = new JTextField()), constraints);

		//RULES
		menuPanel.add(new JLabel("Rule"), constraints);
		rulesTextArea = new JTextArea(4, 10);
		rulesTextArea.setLineWrap(true);
		menuPanel.add(new JScrollPane(rulesTextArea), constraints);

		//ITERATIONS
		menuPanel.add(new JLabel("Iterations"), constraints);
		iterationsTextField = new JTextField();
		menuPanel.add(iterationsTextField, constraints);

		//ANGLE
		menuPanel.add(new JLabel("Angle"), constraints);
		angleTextField = new JTextField();
		menuPanel.add(angleTextField, constraints);

		//LINE LENGTH
		menuPanel.add(new JLabel("Line Length"), constraints);
		lineLengthSlider = new JSlider(1, 15);
		menuPanel.add(lineLengthSlider, constraints);

		//Draw Button
		JButton drawButton = new JButton("Draw");
		drawButton.addActionListener(this);
		menuPanel.add(drawButton, constraints);

		mainPanel.add(menuPanel);
		mainPanel.add(drawPanel);
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		try {
			String startSymbol = startTextField.getText().trim();
			String[] rules = rulesTextArea.getText().split("\n");
			int iterations = Integer.parseInt(iterationsTextField.getText());
			int angle = Integer.parseInt(angleTextField.getText());
			int lineLength = lineLengthSlider.getValue();
			drawPanel.draw(new LSystem(startSymbol, rules, iterations).toString(), angle, lineLength);
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(drawPanel, "Incorrect Input." + exception, "Error", JOptionPane.ERROR_MESSAGE);
		}

	}
}
