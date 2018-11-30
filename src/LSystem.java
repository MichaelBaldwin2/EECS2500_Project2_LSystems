import javax.swing.*;
import java.util.*;

public class LSystem {
	private Queue<Character> ruleQueue;
	private String expandedRules;

	public LSystem(String startSymbol, String[] rules, int iterations) {
		//First we need to split each rule on the equals sign
		Map<Character, String> parsedRules = new HashMap<>();
		for (String iString : rules) {
			String[] sides = iString.split("=");
			parsedRules.put(sides[0].charAt(0), sides[1]);
		}

		ruleQueue = new PriorityQueue<>();
		ruleQueue.add(startSymbol.charAt(0));
		for(int i = 0; i < iterations; i++) {
			for(int qIndex = ruleQueue.size() - 1; qIndex >= 0 ; qIndex--) {
				Character iChar = ruleQueue.remove();
			}
		}

		//Map<Character, char[]> parsedRules = parseRules(rules);
		expandedRules = startSymbol;
		for (int i = 0; i < iterations; i++) {
			expandedRules = applyRules(parsedRules, expandedRules);
		}
	}

	private Map<Character, char[]> parseRules(final String[] rules) {
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

	private String applyRules(final Map<Character, char[]> rules, final String state) {
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

	@Override
	public String toString() {
		return expandedRules;
	}
}
