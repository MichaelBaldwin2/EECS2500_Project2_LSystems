import java.util.*;

/**
 * The LSystem class parses and builds an expanded system using supplied rules.
 */
public class LSystem {
	private Queue<Character> ruleQueue;

	/**
	 * Constructor for the LSystem.
	 *
	 * @param startSymbol The string to start with for performing iterations upon. It can support many characters, but only 1 is required to work.
	 * @param rules       The specified rules that we use to expand with.
	 * @param iterations  The requested number of iterations to expand the startSymbol with the rules.
	 */
	public LSystem(String startSymbol, String[] rules, int iterations) {

		//First we need to split each rule on the equals sign
		Map<Character, String> parsedRules = new HashMap<>();
		for (String iString : rules) {
			String[] sides = iString.split("=");
			parsedRules.put(sides[0].charAt(0), sides[1]);
		}

		//Next we must add the startSymbol (which can be more than 1 character) into the queue.
		ruleQueue = new ArrayDeque<>();
		for (char c : startSymbol.toCharArray())
			ruleQueue.add(c);

		//Now we expand the rules using a queue, the requested number of iterations
		for (int i = 0; i < iterations; i++) {
			int qSize = ruleQueue.size();
			for (int j = 0; j < qSize; j++) {
				Character iChar = ruleQueue.remove();

				//Catch the cases where we can just loop the char back into the queue without modification
				if (iChar.equals('+') || iChar.equals('-') || iChar.equals('[') || iChar.equals(']')) {
					ruleQueue.add(iChar);
					continue;
				}

				//Find the rule in the map, if not null, apply the rule by adding it to the queue
				String rule = parsedRules.get(iChar);
				if (rule == null)
					continue;
				for (char c : rule.toCharArray())
					ruleQueue.add(c);
			}
		}
	}

	@Override
	public String toString() {
		String fullString = "";
		for (char iChar : ruleQueue)
			fullString += iChar;
		return fullString;
	}
}
