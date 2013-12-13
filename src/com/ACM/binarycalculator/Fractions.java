package com.ACM.binarycalculator;

/*
 * This is a class to convert fractions to different bases.
 *  This class pretty much only converts the fraction portion 
 *  because the fraction portion is the only part that needs special logical
 *  care when converting between bases.
 *  
 *  When ever converting from a base to another base the fraction
 *  is first converted to it's decimal equivalent and THEN 
 *  converted to the other base.
 */
public class Fractions {

	// transform the incoming fraction (just the fraction portion, like
	// ".4"octal to ".5"decimal) from whatever base it was in to decimal.
	public static String convertFractionPortionToDecimal(
			String fractionPortion, int incomingRadix) {

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

	// static function that is called to convert a decimal fraction to any other
	// base (except binary, binary has it's own conversion function in the
	// binaryFragment). The main function that does the conversion is recursive,
	// this function calls that recursive function.
	// This function also rounds the fraction to 6places.
	public static String convertFractionPortionFromDecimal(
			String numberToConvert, int outgoingRadix) {

		// call to our recursive function to convert fractions to different
		// bases.
		// store the outcome in a variable because we'll need to manipulate it
		// later.
		String convertedFraction = ""
				+ convertFractionPortionFromDecimal(
						Double.parseDouble(numberToConvert), outgoingRadix, 0);

		// some variables
		String roundedToSixPlaces = null;
		String newlyRoundedFraction = null;

		// if the the length is zero, return nothing. This handles cases like
		// ".0" etc..
		if (convertedFraction.length() == 0) {
			return "";
		}
		// only do the rounding logic if the fraction is greater than six,
		// because we only want to round if we have a fraction that is longer
		// than seven places. We will check if the seventh place is greater than
		// 5, if it's greater than five then that means that the 6th place needs
		// to be incremented.
		if (convertedFraction.length() > 6) {

			// get the last digit in the fraction
			int lastDigitInFraction = Integer
					.parseInt(convertedFraction.substring(convertedFraction
							.length() - 1), outgoingRadix);
			// test if the last digit is greater than or equal to five (if it is
			// we need to add one to the sixth place.) Needs to handle to
			// special case of the number being 9 or F in hex.
			if (lastDigitInFraction >= 5) {

				// get a string that is the six digits.
				roundedToSixPlaces = convertedFraction.substring(0,
						convertedFraction.length() - 1);

				// get the sixth digit
				char sixthPlace = roundedToSixPlaces.charAt(roundedToSixPlaces
						.length() - 1);
				// turn the char into a string so we can turn it into an integer
				// (seems kind of foolish, right?)
				String sixthPlaceString = "" + sixthPlace;

				// turn that char/string into a integer.
				int numberToRound = Integer.parseInt(sixthPlaceString,
						outgoingRadix);

				// add one to the number. Need to check if stepped off the edge
				// of the radix (i.e. 9 -> 10 in base ten, F -> G in base 16,
				// etc)
				if (numberToRound < outgoingRadix - 1) {
					// only round up if we are safe to.
					++numberToRound;
				}

				// new string that will have the cool shiny new rounded value.
				newlyRoundedFraction = roundedToSixPlaces.substring(0,
						roundedToSixPlaces.length() - 1);
				// use string builder cause that's what we do
				StringBuilder build = new StringBuilder(newlyRoundedFraction);

				// if the base is 16 and the number that is being round is
				// something like '11' we need to turn it into 'B' or else we'll
				// look like idiots.
				if (outgoingRadix == 16) {
					build.append(Integer.toHexString(numberToRound));
				} else {
					// otherwise just add the number to the string
					build.append(numberToRound);
				}

				// our return value
				newlyRoundedFraction = build.toString();
			} else {
				// if we don't need to round (i.e. the number was something like
				// .1234564) just cut off the last value (4 in this case)
				// because it does not need to be rounded up.
				newlyRoundedFraction = convertedFraction.substring(0,
						convertedFraction.length() - 1);
			}
		} else {
			// if the converted number wasn't even greater than 6 just return it
			// cause there's no rounding to be done.
			newlyRoundedFraction = convertedFraction;
		}
		return newlyRoundedFraction;
	}

	// this is the recursive function that get's called to convert from base10
	// fraction to base8 and base16 (binary has it's own special function cause
	// special and all that).
	// This function will return a max of seven digits in the fraction because
	// we want to round to six radix places in our fraction.
	// Rounding is taken care of in the function that calls this function, it
	// has the same name but is public.
	private static String convertFractionPortionFromDecimal(
			double numberToConvert, int outgoingRadix, int numberOfDecimalPlaces) {

		// string that will be returned
		String retVal = null;

		// base case
		// stop if the number is fully converted, or if the number is seven
		// radix places
		if (numberToConvert == 0 || numberOfDecimalPlaces == 7) {
			return "";
		}

		// multiply the fraction by the radix we are converting to
		double mult = numberToConvert * outgoingRadix;
		// turn it into a string
		String multString = "" + mult;

		// split upon the radix place
		String[] integerAndFraction = multString.split("\\.");

		// got to make it a double, so turn it into a string first (yes, it
		// seems backwards) need to add in a radix point because we are dealing
		// with fractions
		String toDouble = "." + integerAndFraction[1];

		// make it a double
		double fractionDouble = Double.parseDouble(toDouble);

		// if the number is ten or greater it means that we are converting to
		// hex and we need to convert that 10 to A, etc.
		int testIfgreaterThanTen = Integer.parseInt(integerAndFraction[0]);
		if (testIfgreaterThanTen >= 10) {
			retVal = Integer.toHexString(testIfgreaterThanTen);
		} else {
			retVal = "" + testIfgreaterThanTen;
		}

		// return a new fraction in a completely new base. This is the recursive
		// call as well, need to increment the numberOfDecimalPlaces
		return retVal
				+ convertFractionPortionFromDecimal(fractionDouble,
						outgoingRadix, ++numberOfDecimalPlaces);
	}
}
