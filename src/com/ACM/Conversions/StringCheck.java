package com.ACM.Conversions;

/**
 * @author Kevin Anderson
 * 
 *         This class edits a entered expression to the correct grammar used
 *         within the Expression class. 
 * 
 */

public class StringCheck {
	// expression to be passed into expression class
	private String correctedExpression;

	public StringCheck(String string) {
		this.correctedExpression = "";
		int i = 0;

		while (i < string.length()) { // scans expression character by character
			if (string.charAt(i) == 'x') { // replaces x with *
				this.correctedExpression += "*";
				i++;
				continue;
			}
			if (string.charAt(i) == ')') { // if closing parenthesis found
				this.correctedExpression += ")";
				if ((i + 1) == string.length()) // if end of string break
					break;
				// else if next char is a number then add the implied x
				if (string.charAt(i + 1) >= '0' && string.charAt(i + 1) <= '9') {
					this.correctedExpression += " x ";
					i++;
					continue;
				}
			}
			// for all other cases just add char
			this.correctedExpression += string.charAt(i);
			i++;

		}
	}

	public String getCorrectedExpression() {
		return this.correctedExpression;
	}
}
