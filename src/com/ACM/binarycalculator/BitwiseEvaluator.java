package com.ACM.binarycalculator;

import java.util.BitSet;
import java.util.StringTokenizer;

public class BitwiseEvaluator {

	public static String evaluateBitwiseOperation(String binaryExpression) {

		if (!(binaryExpression.contains("AND")
				|| binaryExpression.contains("OR")
				|| binaryExpression.contains("XOR")
				|| binaryExpression.contains("NOT") || binaryExpression
					.contains("NAND"))) {
			return "";
		}

		StringTokenizer toke = new StringTokenizer(binaryExpression, " ");
		BitSet bs1 = null;
		BitSet bs2 = null;

		String token1 = null, bitwiseOp = null, token2 = null, retVal = null;
		while (toke.hasMoreTokens()) {
			token1 = toke.nextToken().toString();
			bitwiseOp = toke.nextToken().toString();
			token2 = toke.nextToken().toString();
			if (token1.equals(" ")) {
				// do nothing if a space
			} else if (bitwiseOp.equals("AND")) {
				bs1 = new BitSet(Integer.parseInt(token1, 2));
				bs2 = new BitSet(Integer.parseInt(token2, 2));

				bs1.and(bs2);
				retVal = bs1.toString();
			} else if (bitwiseOp.equals("OR")) {
				bs1 = new BitSet(Integer.parseInt(token1, 2));
				bs2 = new BitSet(Integer.parseInt(token2, 2));

				bs1.or(bs2);
				retVal = bs1.toString();
			} else if (bitwiseOp.equals("XOR")) {
				bs1 = new BitSet(Integer.parseInt(token1, 2));
				bs2 = new BitSet(Integer.parseInt(token2, 2));

				bs1.xor(bs2);
				retVal = bs1.toString();
			} else if (bitwiseOp.equals("NAND")) {
				bs1 = new BitSet(Integer.parseInt(token1, 2));
				bs2 = new BitSet(Integer.parseInt(token2, 2));

				bs1.and(bs2);
				bs1.flip(0);
				retVal = bs1.toString();
			} else if (bitwiseOp.equals("NOT")) {
				bs1 = new BitSet(Integer.parseInt(token1, 2));
				//bs2 = new BitSet(Integer.parseInt(token2, 2));

				//
				bs1.flip(0);
				retVal = bs1.toString();
			}
		}

		return retVal;
	}
}
