package com.ACM.binarycalculator;

import android.content.Context;
import android.widget.Toast;

//This is the class that evaluates bitwise expressions. It will only work with 
//expressions like "binary_number BITWISE_OPERATOR binary_number" so something
//like this: "110001 NOR 10001". Will not work with mathematical operators in 
//in the expression nor multiple bitwise operators.

/**
 * 
 * @author James Van Gaasbeck, ACM at UCF <jjvg@knights.ucf.edu>
 * 
 * 
 */
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

		// get the number values in the form of an integer
		int first = Integer.parseInt(expressionParts[0], 2);
		// get the bitwise operation
		String bitwiseOperator = expressionParts[1];
		int second = Integer.parseInt(expressionParts[2], 2);

		int lengthOfRetVal = 0;
		// get the longest length of the numbers for formating.
		if (expressionParts[0].length() > expressionParts[2].length())
			lengthOfRetVal = expressionParts[0].length();
		else
			lengthOfRetVal = expressionParts[2].length();

		// do the correct bitwise operation
		if (bitwiseOperator.equals("AND")) {

			int answer = (first & second);
			retVal = Integer.toBinaryString(answer);
			if (retVal.length() > lengthOfRetVal)
				retVal = retVal.substring(retVal.length() - lengthOfRetVal,
						retVal.length());

		} else if (bitwiseOperator.equals("OR")) {

			int answer = (first | second);
			retVal = Integer.toBinaryString(answer);
			if (retVal.length() > lengthOfRetVal)
				retVal = retVal.substring(retVal.length() - lengthOfRetVal,
						retVal.length());

		} else if (bitwiseOperator.equals("NOR")) {

			int answer = (~(first | second));
			retVal = Integer.toBinaryString(answer);
			if (retVal.length() > lengthOfRetVal)
				retVal = retVal.substring(retVal.length() - lengthOfRetVal,
						retVal.length());

		} else if (bitwiseOperator.equals("XOR")) {

			int answer = (first ^ second);
			retVal = Integer.toBinaryString(answer);
			if (retVal.length() > lengthOfRetVal)
				retVal = retVal.substring(retVal.length() - lengthOfRetVal,
						retVal.length());

		} else if (bitwiseOperator.equals("NAND")) {

			int answer = (~(first & second));
			retVal = Integer.toBinaryString(answer);
			if (retVal.length() > lengthOfRetVal)
				retVal = retVal.substring(retVal.length() - lengthOfRetVal,
						retVal.length());

		} else if (bitwiseOperator.equals("XNOR")) {

			int answer = (~(first ^ second));
			retVal = Integer.toBinaryString(answer);
			if (retVal.length() > lengthOfRetVal)
				retVal = retVal.substring(retVal.length() - lengthOfRetVal,
						retVal.length());
		}
		return retVal;
	}

}
