import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LSystem {
	private String expandedRules;

	public LSystem(String startSymbol, String[] rules, int iterations) {
		Map<Character, char[]> parsedRules = parseRules(rules);
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
