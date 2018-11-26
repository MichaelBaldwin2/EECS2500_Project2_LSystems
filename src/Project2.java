/*
 * Written by: Mike Baldwin
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Project2 {
	private static LSystem system;

	public static void calculateLSystem(final String start, final String[] rules, final int iterations, final int angle, final int lineLength, final int startPosition, final boolean vertical) {
		String state = start;
		Map<Character, char[]> parsedRules = parseRules(rules);

		for (int i = 0; i < iterations; i++) {
			state = applyRules(parsedRules, state);
		}

		system.setProperties(state, angle, lineLength, startPosition, vertical);
		system.draw(2);
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

	public static void main(final String[] args) {
		CustomFrame frame = new CustomFrame();
		system = new LSystem(frame.getCanvas());
	}
}
