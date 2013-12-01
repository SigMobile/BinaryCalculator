package com.ACM.Conversions;

public class StringCheck {
	private String correctedExpression;

	public StringCheck(String string) {
		this.correctedExpression = "";
		int i = 0;

		while (i < string.length()) {
			if (string.charAt(i) == 'x') {
				this.correctedExpression += "*";
				i++;
				continue;
			}
			/*
			 * if (string.charAt(i) == '(') { this.correctedExpression += "( ";
			 * i++; continue; }
			 */
			if (string.charAt(i) == ')') {
				this.correctedExpression += ")";
				if ((i + 1) == string.length()) //end of string so break
					break;

				if (string.charAt(i + 1) >= '0' && string.charAt(i + 1) <= '9') {
					this.correctedExpression += " x ";
					i++;
					continue;
				}
			}
			this.correctedExpression += string.charAt(i);
			i++;

		}
	}

	public String getCorrectedExpression() {
		return this.correctedExpression;
	}
}
