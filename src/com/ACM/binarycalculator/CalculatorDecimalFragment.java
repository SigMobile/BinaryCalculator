package com.ACM.binarycalculator;

import java.math.BigInteger;
import java.util.StringTokenizer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 
 * @author James Van Gaasbeck, ACM at UCF <jjvg@knights.ucf.edu>
 * 
 * 
 */
public class CalculatorDecimalFragment extends Fragment {
	// this is a tag used for debugging purposes
	// private static final String TAG = "CalculatorHexFragment";

	// string constant for saving our workingTextViewText
	private static final String KEY_WORKINGTEXTVIEW_STRING = "workingTextString";
	private static final int VIEW_NUMBER = 1;
	// the radix number (base-number) to be used when parsing the string.
	private static final int VIEWS_RADIX = 10;

	// these are our member variables
	TextView mComputeTextView;
	TextView mWorkingTextView;
	private String mCurrentWorkingText;
	String mCurrentComputedValue;
	String mDataFromActivity;
	FragmentDataPasser mCallback;
	public static int numberOfOpenParenthesis;
	public static int numberOfClosedParenthesis;

	@Override
	// we need to inflate our View so let's grab all the View IDs and inflate
	// them.
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to make a view instance from our layout.
		View v = inflater.inflate(R.layout.fragment_calculator_decimal,
				container, false);

		// get the textViews by id, notice we have to reference them via the
		// view instance we just created.
		mComputeTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_decimal_computedTextView);
		mWorkingTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_decimal_workingTextView);

		// if the we saved something away, grab it!
		if (savedInstanceState != null) {
			mCurrentWorkingText = savedInstanceState
					.getString(KEY_WORKINGTEXTVIEW_STRING);
			// We need to check that we aren't accessing null data or else it
			// will crash upon turning the screen.
			if (mCurrentWorkingText == null) {
				mCurrentWorkingText = new String("");
			}
			// set the text to be what we saved away and just now retrieved.
			mWorkingTextView.setText(mCurrentWorkingText);

		}

		View.OnClickListener genericNumberButtonListener = new View.OnClickListener() {
			// when someone clicks a button that isn't "special" we are going to
			// add it to the workingTextView
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();

				if (mCurrentWorkingText.length() == 0) {
					mWorkingTextView.setText(textFromButton);
					mCurrentWorkingText = textFromButton;
				} else {
					StringTokenizer toke = new StringTokenizer(
							mCurrentWorkingText.concat(textFromButton),
							"-+/x)( ");
					String numberLengthTest = null;
					while (toke.hasMoreTokens()) {
						numberLengthTest = (String) toke.nextToken();
					}
					if (numberLengthTest.length() > 16) {
						return;
					}
					// if the working TextView isn't zero we need to append
					// the textFromButton to what is already there.
					mWorkingTextView.setText(mCurrentWorkingText
							+ textFromButton);
					mCurrentWorkingText = mWorkingTextView.getText().toString();
				}
				onPassData(mCurrentWorkingText);
			}
		};

		View.OnClickListener genericOperatorButtonListener = new View.OnClickListener() {
			// when someone clicks an operator "/x+" NOT "-", "-" is special so
			// it gets it's own listener. We can't have expressions with
			// adjacent operators "/+x" nor can we start with them.
			// We also cannot have a "." followed by an operator "+/x"
			// Nor can we have a "-" followed by an operator.
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();
				// see if the workingTextView is empty, if so DON'T add the
				// operator
				if (mCurrentWorkingText.length() == 0) {
					// do NOTHING because we can't start an expression with
					// "+/x" but we can with "-" which is why we are going to
					// give the minus/negative sign it's own listener.
				} else {
					// we can't have adjacent "+/x" nor can we have a "."
					// followed by "+/x"
					if (mCurrentWorkingText.endsWith("+ ")
							|| mCurrentWorkingText.endsWith("x ")
							|| mCurrentWorkingText.endsWith("/ ")
							|| mCurrentWorkingText.endsWith(".")
							|| mCurrentWorkingText.endsWith("- ")
							|| mCurrentWorkingText.endsWith("-")
							|| mCurrentWorkingText.endsWith("(")) {
						// do nothing because we can't have multiple adjacent
						// operators

					} else {
						// add it on up!
						mWorkingTextView.setText(mCurrentWorkingText + " "
								+ textFromButton + " ");
						mCurrentWorkingText = mWorkingTextView.getText()
								.toString();
					}
				}
				onPassData(mCurrentWorkingText);
			}
		};

		View.OnClickListener openParenthesisButtonListener = new View.OnClickListener() {
			// We can't have a "." followed by a "("
			// We also can't have something like this "6)"
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();

				if (mCurrentWorkingText.length() == 0) {
					mWorkingTextView.setText(textFromButton);
					mCurrentWorkingText = textFromButton;

					CalculatorDecimalFragment.numberOfOpenParenthesis++;
					CalculatorBinaryFragment.numberOfOpenParenthesis++;
					CalculatorHexFragment.numberOfOpenParenthesis++;
					CalculatorOctalFragment.numberOfOpenParenthesis++;
				} else {

					if (mCurrentWorkingText.endsWith(".")) {
						// do nothing
					} else {

						mWorkingTextView.setText(mCurrentWorkingText
								+ textFromButton);
						mCurrentWorkingText = mWorkingTextView.getText()
								.toString();

						CalculatorDecimalFragment.numberOfOpenParenthesis++;
						CalculatorBinaryFragment.numberOfOpenParenthesis++;
						CalculatorHexFragment.numberOfOpenParenthesis++;
						CalculatorOctalFragment.numberOfOpenParenthesis++;
					}
				}
				onPassData(mCurrentWorkingText);
			}
		};

		View.OnClickListener closeParenthesisButtonListener = new View.OnClickListener() {
			// We can't have any of these "./+-x" followed by a ")" nor can we
			// have something like this "()"
			// We also can't have something like this "6)" nor something like
			// "(4 x 4)9)"
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();

				if (mCurrentWorkingText.length() == 0) {
					// do nothing we can't start with ")"
				} else {

					if (((mCurrentWorkingText.endsWith(".")
							|| mCurrentWorkingText.endsWith("/ ")
							|| mCurrentWorkingText.endsWith("x ")
							|| mCurrentWorkingText.endsWith("+ ")
							|| mCurrentWorkingText.endsWith("- ")
							|| mCurrentWorkingText.endsWith("-") || mCurrentWorkingText
								.endsWith("(")))
							|| numberOfClosedParenthesis >= numberOfOpenParenthesis) {
						// do nothing
					} else {

						mWorkingTextView.setText(mCurrentWorkingText
								+ textFromButton);
						mCurrentWorkingText = mWorkingTextView.getText()
								.toString();

						CalculatorBinaryFragment.numberOfClosedParenthesis++;
						CalculatorDecimalFragment.numberOfClosedParenthesis++;
						CalculatorOctalFragment.numberOfClosedParenthesis++;
						CalculatorHexFragment.numberOfClosedParenthesis++;
					}
				}
				onPassData(mCurrentWorkingText);
			}
		};
		View.OnClickListener genericMinusButtonListener = new View.OnClickListener() {
			// we can't have more than 2 adjacent "-"
			// we also can't have something like this ".-3"
			// No cases like this "--3" BUT we can have "5--3"
			// No cases like this "(--3)
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();
				// see if the workingTextView is empty
				if (mCurrentWorkingText.length() == 0) {
					mWorkingTextView.setText(textFromButton);
					mCurrentWorkingText = textFromButton;
				} else if (mCurrentWorkingText.length() == 1
						&& mCurrentWorkingText.endsWith("-")) {
					// do nothing so we don't start out with something like this
					// "--2"
				} else {
					// we can't have more than 2 adjacent '-'. So get the last
					// two char's and check if it's "--"
					if (mCurrentWorkingText.endsWith(".")
							|| mCurrentWorkingText.endsWith("--")
							|| mCurrentWorkingText.endsWith("(-")) {
						// do nothing because we can't have more than 2
						// adjacent minus's
					} else {
						// otherwise, add it to the view
						if (mCurrentWorkingText.endsWith("0")
								|| mCurrentWorkingText.endsWith("1")
								|| mCurrentWorkingText.endsWith("2")
								|| mCurrentWorkingText.endsWith("3")
								|| mCurrentWorkingText.endsWith("4")
								|| mCurrentWorkingText.endsWith("5")
								|| mCurrentWorkingText.endsWith("6")
								|| mCurrentWorkingText.endsWith("7")
								|| mCurrentWorkingText.endsWith("8")
								|| mCurrentWorkingText.endsWith("9")) {
							mWorkingTextView.setText(mCurrentWorkingText + " "
									+ textFromButton + " ");
							mCurrentWorkingText = mWorkingTextView.getText()
									.toString();
						} else {
							mWorkingTextView.setText(mCurrentWorkingText
									+ textFromButton);
							mCurrentWorkingText = mWorkingTextView.getText()
									.toString();
						}
					}
				}
				// need to pass data to our call back so all fragments can be
				// updated with the new workingTextView
				onPassData(mCurrentWorkingText);
			}
		};

		View.OnClickListener backspaceButtonListener = new View.OnClickListener() {
			// remove the last thing to be inputed into the workingTextView,
			// also update the post fix stacks accordingly?
			@Override
			public void onClick(View v) {
				// need to check if the view has anything in it, because if it
				// doesn't the app will crash when trying to change a null
				// string.
				if (mCurrentWorkingText != null) {
					if (mCurrentWorkingText.length() != 0) {

						if (mCurrentWorkingText.endsWith(")")) {
							CalculatorDecimalFragment.numberOfClosedParenthesis--;
							CalculatorBinaryFragment.numberOfClosedParenthesis--;
							CalculatorHexFragment.numberOfClosedParenthesis--;
							CalculatorOctalFragment.numberOfClosedParenthesis--;
						} else if (mCurrentWorkingText.endsWith("(")) {
							CalculatorDecimalFragment.numberOfOpenParenthesis--;
							CalculatorBinaryFragment.numberOfOpenParenthesis--;
							CalculatorHexFragment.numberOfOpenParenthesis--;
							CalculatorOctalFragment.numberOfOpenParenthesis--;
						}
						// we need to delete the spaces around the operators
						// also, not just the last char added to the
						// workingTextView
						if (mCurrentWorkingText.endsWith(" + ")
								|| mCurrentWorkingText.endsWith(" - ")
								|| mCurrentWorkingText.endsWith(" x ")
								|| mCurrentWorkingText.endsWith(" / ")) {

							mCurrentWorkingText = mCurrentWorkingText
									.substring(0,
											mCurrentWorkingText.length() - 3);
							mWorkingTextView.setText(mCurrentWorkingText);
						} else {

							// if it's not an operator with spaces around it,
							// just delete the last char
							mCurrentWorkingText = mCurrentWorkingText
									.substring(0,
											mCurrentWorkingText.length() - 1);
							mWorkingTextView.setText(mCurrentWorkingText);
						}
					}
					onPassData(mCurrentWorkingText);
				}
			}
		};

		// get a reference to our TableLayout XML
		TableLayout tableLayout = (TableLayout) v
				.findViewById(R.id.fragment_calculator_decimal_tableLayout);

		// adds the values and listeners to the buttons and pretty much every
		// button except for a few
		//
		// this for loop could probably be cleaned up, because the views had
		// changed from the original and the for loop had to change as well,
		// making the for loop look like a logical mess.
		for (int i = tableLayout.getChildCount() - 1; i >= 0; i--) {
			// get the tableRow from the table layout
			TableRow row = (TableRow) tableLayout.getChildAt(i);
			for (int j = 0; j < row.getChildCount(); j++) {
				// get the button from the tableRow
				Button butt = (Button) row.getChildAt(j);
				// if we are in the first row (topmost), and on the first button
				// (leftmost), we want that button to be a '('
				if (i == 0 && j == 0) {
					butt.setText("(");
					butt.setOnClickListener(openParenthesisButtonListener);
				}
				// if we are on the topmost row and the second button, make the
				// button a ')'
				else if (i == 0 && j == 1) {
					butt.setText(")");
					butt.setOnClickListener(closeParenthesisButtonListener);
				} else {

					// this sets the button of the last column of every row
					if (i == tableLayout.getChildCount() - 1) {
						butt.setText("+");
						butt.setOnClickListener(genericOperatorButtonListener);
					} else if (i == tableLayout.getChildCount() - 2) {
						butt.setText("-");
						butt.setOnClickListener(genericMinusButtonListener);
					} else if (i == tableLayout.getChildCount() - 3) {
						butt.setText("x");
						butt.setOnClickListener(genericOperatorButtonListener);
					} else if (i == tableLayout.getChildCount() - 4) {
						butt.setText("/");
						butt.setOnClickListener(genericOperatorButtonListener);
					}
				}
			}
		} // closes for() loop

		// get a reference to the first (topmost) row so we can set the clear
		// all button manually, because it was annoying trying to work it in to
		// the for loop
		TableRow firstRow = (TableRow) tableLayout.getChildAt(0);
		// the clear all button was decided to be the third button in the
		// topmost row
		Button clearAllButton = (Button) firstRow.getChildAt(2);
		clearAllButton.setText("Clear All");
		clearAllButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// clear all the text in the working textView, AND maybe the
				// computed textView as well?
				// Also, might want to clear out the post fix expression stack
				mCurrentWorkingText = new String("");
				mWorkingTextView.setText(mCurrentWorkingText);
				// update the Static variable in our activity so we can use it
				// as a fragment argument
				mComputeTextView.setText("");

				CalculatorDecimalFragment.numberOfOpenParenthesis = 0;
				CalculatorBinaryFragment.numberOfOpenParenthesis = 0;
				CalculatorHexFragment.numberOfOpenParenthesis = 0;
				CalculatorOctalFragment.numberOfOpenParenthesis = 0;

				CalculatorDecimalFragment.numberOfClosedParenthesis = 0;
				CalculatorBinaryFragment.numberOfClosedParenthesis = 0;
				CalculatorHexFragment.numberOfClosedParenthesis = 0;
				CalculatorOctalFragment.numberOfClosedParenthesis = 0;

				onPassData(mCurrentWorkingText);
			}

		});

		Button backSpaceButton = (Button) firstRow.getChildAt(3);
		backSpaceButton.setText("<-");
		backSpaceButton.setOnClickListener(backspaceButtonListener);

		// get a reference to the second row of the table (AND, OR, NAND)
		TableRow secondRow = (TableRow) tableLayout.getChildAt(1);

		Button aButton = (Button) secondRow.getChildAt(0);
		aButton.setText("7");
		aButton.setOnClickListener(genericNumberButtonListener);

		Button bButton = (Button) secondRow.getChildAt(1);
		bButton.setText("8");
		bButton.setOnClickListener(genericNumberButtonListener);

		Button cButton = (Button) secondRow.getChildAt(2);
		cButton.setText("9");
		cButton.setOnClickListener(genericNumberButtonListener);

		TableRow thirdRow = (TableRow) tableLayout.getChildAt(2);
		// the NOR button
		Button dButton = (Button) thirdRow.getChildAt(0);
		dButton.setText("4");
		dButton.setOnClickListener(genericNumberButtonListener);
		// XOR button
		Button eButton = (Button) thirdRow.getChildAt(1);
		eButton.setText("5");
		eButton.setOnClickListener(genericNumberButtonListener);
		// XNOR button
		Button fButton = (Button) thirdRow.getChildAt(2);
		fButton.setText("6");
		fButton.setOnClickListener(genericNumberButtonListener);

		TableRow fourthRow = (TableRow) tableLayout.getChildAt(3);
		// button '1'
		Button sevenButton = (Button) fourthRow.getChildAt(0);
		sevenButton.setText("1");
		sevenButton.setOnClickListener(genericNumberButtonListener);
		// bitwise shift Left button
		Button eightButton = (Button) fourthRow.getChildAt(1);
		eightButton.setText("2");
		eightButton.setOnClickListener(genericNumberButtonListener);
		// bitwise shift Right button
		Button nineButton = (Button) fourthRow.getChildAt(2);
		nineButton.setText("3");
		nineButton.setOnClickListener(genericNumberButtonListener);

		// last row
		TableRow lastRow = (TableRow) tableLayout.getChildAt(tableLayout
				.getChildCount() - 1);

		Button equalsButton = (Button) lastRow.getChildAt(0);
		equalsButton.setText("=");
		equalsButton.setOnClickListener(new OnClickListener() {
			// EQUALS button on click listener
			@Override
			public void onClick(View v) {

			}
		});

		Button zeroButton = (Button) lastRow.getChildAt(1);
		zeroButton.setText("0");
		zeroButton.setOnClickListener(genericNumberButtonListener);

		Button decimalPointButton = (Button) lastRow.getChildAt(2);
		decimalPointButton.setText(".");
		decimalPointButton.setOnClickListener(new OnClickListener() {
			// we can't put a "." up there if there has already been one in
			// the current token (number)
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();

				// see if the workingTextView is empty, if so just add the '.'
				if (mCurrentWorkingText.length() == 0) {
					mWorkingTextView.setText(textFromButton);
					mCurrentWorkingText = textFromButton;
				} else {
					StringTokenizer toke = new StringTokenizer(
							mCurrentWorkingText, "+-/x)(", true);
					String currentElement = null;
					// get the current(last) token(number) so we can test if it
					// has a '.' in it.
					while (toke.hasMoreTokens()) {
						currentElement = toke.nextElement().toString();
					}
					// if the working TextView isn't zero we need to append
					// the
					// textFromButton to what is already there. AND we need to
					// check if the current token already has a '.' in it
					// because we can't have something like '2..2' or 2.2.33'
					if (mCurrentWorkingText.endsWith(".")
							|| currentElement.contains(".")) {
						// do nothing here so we don't end up with expressions
						// like "2..2" or "2.3.22"
					} else {
						// otherwise we're all good and just add the ".' up
						// there.
						mWorkingTextView.setText(mCurrentWorkingText
								+ textFromButton);
						mCurrentWorkingText = mWorkingTextView.getText()
								.toString();
					}
				}
				onPassData(mCurrentWorkingText);

			}
		});

		return v;
	}

	public static Fragment newInstance() {
		CalculatorDecimalFragment binFrag = new CalculatorDecimalFragment();
		return binFrag;
	}

	// method to save the state of the application during the activity life
	// cycle. This is so we can preserve the values in the textViews upon screen
	// rotation.
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Log.i(TAG, "onSaveInstanceState");
		outState.putString(KEY_WORKINGTEXTVIEW_STRING, mCurrentWorkingText);
	}

	// fragment life-cycle method
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// set our dataPasser interface up when the fragment is on the activity
		try {
			// hook the call back up to the activity it is attached to, should
			// do this in a try/catch because the parent activity must implement
			// the interface.
			mCallback = (FragmentDataPasser) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					activity.toString()
							+ " must implement the FragmentDataPasser interface so we can pass data between the fragments.");
		}
	}

	// callback method to send data to the activity so we can then update all
	// the fragments
	public void onPassData(String dataToBePassed) {
		mCallback.onDataPassed(dataToBePassed, VIEW_NUMBER, VIEWS_RADIX);
	}

	// method to receive the data from the activity/other-fragments and update
	// the textViews accordingly
	public void updateWorkingTextView(String dataToBePassed, int base) {
		if (dataToBePassed.length() != 0) {

			StringTokenizer toke = new StringTokenizer(dataToBePassed,
					"x+-/)( ", true);
			StringBuilder builder = new StringBuilder();

			while (toke.hasMoreElements()) {
				String aToken = (String) toke.nextElement().toString();
				if (aToken.equals("+") || aToken.equals("x")
						|| aToken.equals("-") || aToken.equals("/")
						|| aToken.equals("(") || aToken.equals(")")
						|| aToken.equals(" ")) {

					builder.append(aToken);

				}
				// if our token contains a "." in it then that means that we
				// need to do some conversion trickery
				else if (aToken.contains(".")) {
					if (aToken.endsWith(".")) {
						// don't do any conversions when the number is still
						// being
						// inputed and in the current state of something like
						// this
						// "5."
						return;
					} else {
						// split the string around the "." delimiter.
						String[] parts = aToken.split("\\.");
						StringBuilder tempBuilder = new StringBuilder();

						// add the portion of the number to the left of the "."
						// to our string this doesn't need any conversion
						// nonsense.
						tempBuilder.append(Integer.toString(Integer.parseInt(
								parts[0], base)));
						// convert the fraction portion
						String getRidOfZeroBeforePoint = null;

						getRidOfZeroBeforePoint = Fractions
								.convertFractionPortionToDecimal(parts[1], base, true);

						// the conversion returns just the fraction portion
						// with
						// a "0" to the left of the ".", so let's get rid of
						// that extra zero.
						getRidOfZeroBeforePoint = getRidOfZeroBeforePoint
								.substring(1, getRidOfZeroBeforePoint.length());

						tempBuilder.append(getRidOfZeroBeforePoint);

						// add that to the string that gets put on the textView
						// (this may be excessive) (I wrote this late at night
						// so stuff probably got a little weird)
						builder.append(tempBuilder.toString());
					}
				} else {
					BigInteger sizeTestBigInt = new BigInteger(aToken, base);
					if (sizeTestBigInt.bitLength() < 64) {
						mCurrentWorkingText = Long.toString(Long.parseLong(
								aToken, base));
						builder.append(mCurrentWorkingText);
					}
				}
				mCurrentWorkingText = builder.toString();

				mWorkingTextView.setText(mCurrentWorkingText);
			}
		} else {
			mCurrentWorkingText = "";
			mWorkingTextView.setText(mCurrentWorkingText);
		}
	}

}
