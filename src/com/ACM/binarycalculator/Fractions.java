package com.ACM.binarycalculator;

public class Fractions {


	// transform the incoming fraction (just the fraction portion, like
	// ".4"octal to ".5"decimal) from whatever base it was in to decimal.
	public static String convertFractionPortion(String fractionPortion,
			int incomingRadix, boolean isConvertingToDecimal) {
		// array to hold each converted index of the fraction, each index of
		// this array will be added to yield the converted fraction
		double[] arrayToAdd = new double[fractionPortion.length()];
		double returnDouble = 0;
		// traverse the incoming string
		for (int i = 0; i < fractionPortion.length(); i++) {
			// get the current index as a string
			String getNum = "" + (fractionPortion.charAt(i));
			// convert that string number into a real double
			double toDouble = Double.parseDouble(getNum);
			// add that number to the array, after dividing by the radix raised
			// to the -offset. For octal this would be .4 * (1/(8^1)) = .5
			if(isConvertingToDecimal){
			arrayToAdd[i] = toDouble * Math.pow(incomingRadix, -(i + 1));
			}else{
				arrayToAdd[i] = toDouble * Math.pow(incomingRadix, (i + 1));
			}
			// add each index together to get the converted outcome
			if (i > 0) {
				returnDouble = arrayToAdd[i - 1] + arrayToAdd[i];
			} else if (i == 0) {
				returnDouble = arrayToAdd[i];
			}
		}
		// return our newly converted number as a string
		return "" + returnDouble;
	}
	
	
}
