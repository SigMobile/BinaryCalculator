package com.ACM.binarycalculator;

public class Fractions {

	// transform the incoming fraction (just the fraction portion, like
	// ".4"octal to ".5"decimal) from whatever base it was in to decimal.
	public static String convertFractionPortionToDecimal(
			String fractionPortion, int incomingRadix) {

		// array to hold each converted index of the fraction, each index of
		// this array will be added to yield the converted fraction
		// double[] arrayToAdd = new double[fractionPortion.length()];
		double returnDouble = 0;
		String getNum = null;
		// traverse the incoming string
		for (int i = 0; i < fractionPortion.length(); i++) {
			// get the current index as a char because we need to test if it's a
			// regular number or an A-F(hex number)
			char getChar = (char) (fractionPortion.charAt(i));
			if (Character.isLetter(getChar)) {
				// if it's a number greater than 9 (a hex digit A-F) convert it
				// to it's decimal value
				getNum = "" + getChar;
				int decimalValueOfHexAlpha = Integer.parseInt(getNum,
						incomingRadix);
				getNum = "" + decimalValueOfHexAlpha;
			} else {
				getNum = "" + getChar;
			}
			// convert that string number into a real double
			double toDouble = Double.parseDouble(getNum);
			// if we are converting to decimal add that number to the array,
			// after dividing by the radix raised
			// to the -offset. For octal this would be .4 * (1/(8^1)) = .5
			returnDouble += toDouble * Math.pow(incomingRadix, -(i + 1));
		}
		// return our newly converted number as a string
		return "" + returnDouble;
	}

	public static String convertFractionPortionFromDecimal(
			String numberToConvert, int outgoingRadix) {

		String roundedToSixPlaces = null;
		String newlyRoundedFraction = null;

		String convertedFraction = ""
				+ convertFractionPortionFromDecimal(
						Double.parseDouble(numberToConvert), outgoingRadix, 0);

		if (convertedFraction.length() == 0) {
			return "";
		}
		if (convertedFraction.length() > 5) {
			int lastDigitInFraction = Integer
					.parseInt(convertedFraction.substring(convertedFraction
							.length() - 1), outgoingRadix);
			if (lastDigitInFraction >= 5) {
				roundedToSixPlaces = convertedFraction.substring(0,
						convertedFraction.length() - 1);

				char sixthPlace = roundedToSixPlaces.charAt(roundedToSixPlaces
						.length() - 1);
				String sixthPlaceString = "" + sixthPlace;

				int numberToRound = Integer.parseInt(sixthPlaceString,
						outgoingRadix);
				++numberToRound;

				newlyRoundedFraction = roundedToSixPlaces.substring(0,
						roundedToSixPlaces.length() - 1);

				StringBuilder build = new StringBuilder(newlyRoundedFraction);
				
				if (outgoingRadix == 16) {
					build.append(Integer.toHexString(numberToRound));
				} else {
					build.append(numberToRound);
				}

				newlyRoundedFraction = build.toString();
			} else {
				newlyRoundedFraction = convertedFraction.substring(0,
						convertedFraction.length() - 1);
			}
		} else {
			newlyRoundedFraction = convertedFraction;
		}

		return newlyRoundedFraction;
	}

	private static String convertFractionPortionFromDecimal(
			double numberToConvert, int outgoingRadix, int numberOfDecimalPlaces) {

		String retVal = null;

		if (numberToConvert == 0 || numberOfDecimalPlaces == 7) {
			return "";
		}

		double mult = numberToConvert * outgoingRadix;
		String multString = "" + mult;

		String[] integerAndFraction = multString.split("\\.");

		String toDouble = "." + integerAndFraction[1];

		double fractionDouble = Double.parseDouble(toDouble);

		++numberOfDecimalPlaces;

		int testIfgreaterThanTen = Integer.parseInt(integerAndFraction[0]);
		if (testIfgreaterThanTen >= 10) {
			retVal = Integer.toHexString(testIfgreaterThanTen);
		} else {
			retVal = "" + testIfgreaterThanTen;
		}
		return retVal
				+ convertFractionPortionFromDecimal(fractionDouble,
						outgoingRadix, numberOfDecimalPlaces);
	}
}
