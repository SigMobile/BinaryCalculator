package com.ACM.binarycalculator;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author James Van Gaasbeck, ACM at UCF <jjvg@knights.ucf.edu>
 */
/*
 * Class to actually evaluate an expression. The methods within the class expect
 * the expression to be in Post-Fix notation. Works with fractions and
 * negatives.
 * 
 * This class is very similar to this post in a java blog:
 * http://java.macteki.com/2011/06/arithmetic-evaluator.html
 * 
 * Example:
 * post-fix=6 2 + 5 * 8 4 / -
 * evaluation=38.0
 * 
 * ^^Notice that there is an additional ".0" appended to the number, if the
 * result is a whole number.
 * 
 * TODO: Make this class handle bitwise operations OR create a different class
 * to handle bitwise operations.
 */
public class PostfixEvaluator {

	/**
	 * 
	 * @param postfix
	 *            - The post-fix expression, in the form of a string, that is to
	 *            be evaluated. Must be in post-fix and base-10.
	 * @return - The answer in the form of a string and in base-10. If the
	 *         answer is a whole number it will return an additional decimal
	 *         point and zero.
	 */
	public static String evaluate(String postfix) {
		Stack<String> stack = new Stack<String>();
		// StringTokenizer to split up the expression
		StringTokenizer toke = new StringTokenizer(postfix, "x+-/)( \n", true);

		while (toke.hasMoreElements()) {
			String currentToken = toke.nextElement().toString();

			// we need to check if the the dash is being used as a minus sign or
			// a negative sign.
			if (currentToken.equals("-")) {
				// temporary variable to hold the minus/negative
				String minusOrNegativeSign = currentToken;

				// check the next token to see if we are dealing with a
				// minus sign or a negative sign
				currentToken = toke.nextElement().toString();
				if (!currentToken.equals(" ")) {
					// if the next token isn't a space then we are dealing
					// with a negative number.
					// so add the number with the negative sign in front of
					// it
					stack.push(minusOrNegativeSign + currentToken);
				}
				// if we are dealing with a minus sign then do the math stuff to
				// it
				else {
					String operand2 = stack.pop();
					String operand1 = stack.pop();
					String value = doMathStuff(operand1, operand2,
							minusOrNegativeSign);
					stack.push(value);
				}
			}
			// if the currentToken is a number push it to the stack
			else if (!(currentToken.equals("+") || currentToken.equals("x")
					|| currentToken.equals("-") || currentToken.equals("/")
					|| currentToken.equals("\n") || currentToken.equals(" ")
					|| currentToken.equals("(") || currentToken.equals(")"))) {

				stack.push(currentToken);

			}
			// if the currentToken is an operator, pop the two most recent
			// numbers off of the stack and do math to it, then push the outcome
			// back on the stack.
			else if (currentToken.equals("+") || currentToken.equals("x")
					|| currentToken.equals("-") || currentToken.equals("/")
					|| currentToken.equals("\n")) {

				String operand2 = stack.pop();
				String operand1 = stack.pop();
				String value = doMathStuff(operand1, operand2, currentToken);
				stack.push(value);
			}
		}
		// return the answer (in the form of a string), which will be the
		// last/only thing left in the stack.
		return stack.pop();
	}

	// method that actually does the basic arithmetic based upon the current
	// operator.
	private static String doMathStuff(String operand1, String operand2,
			String currentToken) {

		double value1 = Double.parseDouble(operand1);
		double value2 = Double.parseDouble(operand2);
		double result = 0;

		if (currentToken.equals("+")) {
			result = value1 + value2;
		} else if (currentToken.equals("-")) {
			result = value1 - value2;
		} else if (currentToken.equals("x")) {
			result = value1 * value2;
		} else if (currentToken.equals("/")) {
			result = value1 / value2;
		}
		// return the result as a string
		return "" + result;
	}

}
