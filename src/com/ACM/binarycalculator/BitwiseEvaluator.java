package com.ACM.binarycalculator;

import java.math.BigInteger;

import android.content.Context;
import android.widget.Toast;

public class BitwiseEvaluator {

	/**
	 * 
	 * @param expression
	 *            - The expression containing bitwise operation to be evaluated.
	 * @param appContext
	 *            - The context of the application so we can throw a Toast if
	 *            something goes wrong. NOTE: remember to send the
	 *            getSherlockActivity() if ActionBarSherlock is being used.
	 * @return - The evaluated bits in the form of a string.
	 */
	public static String Evaluate(String expression, Context appContext) {

		// lets just not do bitwise operations when the expression contains an
		// operator, decimal, or is negative.
		if (expression.contains("+") || expression.contains("x")
				|| expression.contains("/") || expression.contains(".")
				|| expression.contains("-")) {

			Toast.makeText(appContext, "Not a valid bitwise expression.",
					Toast.LENGTH_SHORT).show();
			return "";
		}

		// don't do anything if there isn't an AND, NAND, NOR, OR, XOR, NOT in
		// the expression
		if (!(expression.contains("N") || expression.contains("O"))) {
			return "";
		}

		// first index should be the first set of bits
		// second index should the Bitwise operation
		// third index should be the second set of bits
		String[] expressionParts = expression.split(" ");
		String retVal = null;

		int first = Integer.parseInt(expressionParts[0], 2);
		String bitwiseOperator = expressionParts[1];
		int second = Integer.parseInt(expressionParts[2], 2);

		if (bitwiseOperator.equals("AND")) {

			int answer = (first & second);
			retVal = Integer.toBinaryString(answer);

		} else if (bitwiseOperator.equals("OR")) {

			int answer = (first | second);
			retVal = Integer.toBinaryString(answer);

		} else if (bitwiseOperator.equals("NOR")) {

			int answer = (~(first | second));
			retVal = Integer.toBinaryString(answer);

		} else if (bitwiseOperator.equals("XOR")) {

			int answer = (first ^ second);
			retVal = Integer.toBinaryString(answer);

		} else if (bitwiseOperator.equals("NAND")) {

			int answer = (~(first & second));
			retVal = Integer.toBinaryString(answer);

		} else if (bitwiseOperator.equals("XNOR")) {

			int answer = (~(first ^ second));
			retVal = Integer.toBinaryString(answer);
		}
		return retVal;
	}

}
