package com.ACM.binarycalculator;

import java.util.Locale;
import java.util.StringTokenizer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.content.pm.ActivityInfo;

/**
 * 
 * @author James Van Gaasbeck, ACM at UCF <jjvg@knights.ucf.edu>
 * 
 * 
 */
public class CalculatorHexFragment extends Fragment {
	// this is a tag used for debugging purposes
	private static final String TAG = "CalculatorHexFragment";
	// string constant for saving our workingTextViewText
	private static final String KEY_WORKINGTEXTVIEW_STRING = "workingTextString";
	private static final int VIEW_NUMBER = 3;
	// the radix number (base-number) to be used when parsing the string.
	private static final int VIEWS_RADIX = 16;

	// these are our member variables
	TextView mComputeTextView;
	TextView mWorkingTextView;
	static String mCurrentWorkingText;
	String mCurrentComputedValue;
	String mDataFromActivity;
	FragmentDataPasser mCallback;

	@Override
	// we need to inflate our View so let's grab all the View IDs and inflate
	// them.
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to make a view instance from our layout.
		View v = inflater.inflate(R.layout.fragment_calculator_hex, container,
				false);

		// get the textViews by id, notice we have to reference them via the
		// view instance we just created.
		mComputeTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_hex_computedTextView);
		mWorkingTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_hex_workingTextView);

		// if the we saved something away, grab it!
		if (savedInstanceState != null) {
			mCurrentWorkingText = savedInstanceState
					.getString(KEY_WORKINGTEXTVIEW_STRING);
			// set the text to be what we saved away and just now retrieved.
			mWorkingTextView.setText(mCurrentWorkingText.toUpperCase(Locale
					.getDefault()));
		}

		View.OnClickListener genericButtonListener = new View.OnClickListener() {
			// when someone clicks a button that isn't "special" we are going to
			// add it to the workingTextView
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				mCurrentWorkingText = mWorkingTextView.getText().toString();
				String textFromButton = textView.getText().toString();
				boolean inputTextIsOperator = false, inputIsPeriod = false;
				if (textFromButton == "+" || textFromButton == "-"
						|| textFromButton == "x" || textFromButton == "/") {
					inputTextIsOperator = true;
				} else if (textFromButton == ".") {
					inputIsPeriod = true;
				}

				// if the button was just a number a put it on textView
				if (!inputTextIsOperator && !inputIsPeriod) {
					// see if the workingTextView is empty
					if (mCurrentWorkingText.length() == 0) {
						mWorkingTextView.setText(textFromButton);
						mCurrentWorkingText = textFromButton;
					} else {
						// if the working TextView isn't zero we need to append
						// the
						// textFromButton to what is already there.
						mWorkingTextView.setText(mCurrentWorkingText
								+ textFromButton);
						mCurrentWorkingText = mWorkingTextView.getText()
								.toString();
					}
				} else if (mCurrentWorkingText.length() == 0
						&& (!inputIsPeriod || inputTextIsOperator)) {
					// Do nothing
				}
				// if the button was an operator AND the last inputed button
				// was an operator, don't all it to go on the textView
				else if ((mCurrentWorkingText.endsWith("+")
						|| mCurrentWorkingText.endsWith("-")
						|| mCurrentWorkingText.endsWith("x")
						|| mCurrentWorkingText.endsWith("/") || mCurrentWorkingText
						.endsWith("."))) {
					// Do nothing for this case.
				}
				// otherwise add it to the textView
				else {
					// see if the workingTextView is empty
					if (mCurrentWorkingText.length() == 0) {
						mWorkingTextView.setText(textFromButton);
						mCurrentWorkingText = textFromButton;
					} else {
						// if the working TextView isn't zero we need to append
						// the
						// textFromButton to what is already there.
						mWorkingTextView.setText(mCurrentWorkingText
								+ textFromButton);
						mCurrentWorkingText = mWorkingTextView.getText()
								.toString();
					}
				}
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
				if (mCurrentWorkingText.length() != 0) {
					mCurrentWorkingText = mCurrentWorkingText.substring(0,
							mCurrentWorkingText.length() - 1);
					mWorkingTextView.setText(mCurrentWorkingText);
				}
				onPassData(mCurrentWorkingText);
			}
		};

		View.OnClickListener openParenthesesButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Open Parentheses
			}
		};

		View.OnClickListener closedParenthesesButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Closed Parentheses
			}
		};

		// get a reference to our TableLayout XML
		TableLayout tableLayout = (TableLayout) v
				.findViewById(R.id.fragment_calculator_hex_tableLayout);

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
					butt.setOnClickListener(genericButtonListener);
				}
				// if we are on the topmost row and the second button, make the
				// button a ')'
				else if (i == 0 && j == 1) {
					butt.setText(")");
					butt.setOnClickListener(genericButtonListener);
				} else {

					// this sets the button of the last column of every row
					if (i == tableLayout.getChildCount() - 1) {
						butt.setText("+");
						butt.setOnClickListener(genericButtonListener);
					} else if (i == tableLayout.getChildCount() - 2) {
						butt.setText("-");
						butt.setOnClickListener(genericButtonListener);
					} else if (i == tableLayout.getChildCount() - 3) {
						butt.setText("x");
						butt.setOnClickListener(genericButtonListener);
					} else if (i == tableLayout.getChildCount() - 4) {
						butt.setText("/");
						butt.setOnClickListener(genericButtonListener);
					} else if (i == tableLayout.getChildCount() - 7) {
						butt.setText("<-");
						butt.setOnClickListener(backspaceButtonListener);
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
			public void onClick(View v) {
				mWorkingTextView.setText("");
				mCurrentWorkingText = "";
				mComputeTextView.setText("");

				onPassData(mCurrentWorkingText);
			}

		});

		// get a reference to the second row of the table (AND, OR, NAND)
		TableRow secondRow = (TableRow) tableLayout.getChildAt(1);

		Button aButton = (Button) secondRow.getChildAt(0);
		aButton.setText("A");
		aButton.setOnClickListener(genericButtonListener);

		Button bButton = (Button) secondRow.getChildAt(1);
		bButton.setText("B");
		bButton.setOnClickListener(genericButtonListener);

		Button cButton = (Button) secondRow.getChildAt(2);
		cButton.setText("C");
		cButton.setOnClickListener(genericButtonListener);

		// get a reference to the third row (NOR, XOR, XNOR)
		TableRow thirdRow = (TableRow) tableLayout.getChildAt(2);
		// the NOR button
		Button dButton = (Button) thirdRow.getChildAt(0);
		dButton.setText("D");
		dButton.setOnClickListener(genericButtonListener);
		// XOR button
		Button eButton = (Button) thirdRow.getChildAt(1);
		eButton.setText("E");
		eButton.setOnClickListener(genericButtonListener);
		// XNOR button
		Button fButton = (Button) thirdRow.getChildAt(2);
		fButton.setText("F");
		fButton.setOnClickListener(genericButtonListener);

		// fourth row (1, <<, >>)
		TableRow fourthRow = (TableRow) tableLayout.getChildAt(3);
		// button '1'
		Button sevenButton = (Button) fourthRow.getChildAt(0);
		sevenButton.setText("7");
		sevenButton.setOnClickListener(genericButtonListener);
		// bitwise shift Left button
		Button eightButton = (Button) fourthRow.getChildAt(1);
		eightButton.setText("8");
		eightButton.setOnClickListener(genericButtonListener);
		// bitwise shift Right button
		Button nineButton = (Button) fourthRow.getChildAt(2);
		nineButton.setText("9");
		nineButton.setOnClickListener(genericButtonListener);

		// now we need to get the last row of buttons and get them to the
		// screen.
		TableRow fifthRow = (TableRow) tableLayout.getChildAt(4);
		// set the decimal button
		Button fourButton = (Button) fifthRow.getChildAt(0);
		fourButton.setText("4");
		fourButton.setOnClickListener(genericButtonListener);
		// set the zero button
		Button fiveButton = (Button) fifthRow.getChildAt(1);
		fiveButton.setText("5");
		fiveButton.setOnClickListener(genericButtonListener);
		// set the plus button
		Button sixButton = (Button) fifthRow.getChildAt(2);
		sixButton.setText("6");
		sixButton.setOnClickListener(genericButtonListener);
		// set the equals button, it will have it's own separate listener to
		// compute the inputed value

		TableRow sixthRow = (TableRow) tableLayout.getChildAt(5);

		Button oneButton = (Button) sixthRow.getChildAt(0);
		oneButton.setText("1");
		oneButton.setOnClickListener(genericButtonListener);

		Button twoButton = (Button) sixthRow.getChildAt(1);
		twoButton.setText("2");
		twoButton.setOnClickListener(genericButtonListener);

		Button threeButton = (Button) sixthRow.getChildAt(2);
		threeButton.setText("3");
		threeButton.setOnClickListener(genericButtonListener);

		TableRow lastRow = (TableRow) tableLayout.getChildAt(6);

		Button equalsButton = (Button) lastRow.getChildAt(0);
		equalsButton.setText("=");
		equalsButton.setOnClickListener(genericButtonListener);

		Button zeroButton = (Button) lastRow.getChildAt(1);
		zeroButton.setText("0");
		zeroButton.setOnClickListener(genericButtonListener);

		Button decimalPointButton = (Button) lastRow.getChildAt(2);
		decimalPointButton.setText(".");
		decimalPointButton.setOnClickListener(genericButtonListener);

		return v;
	}

	public static Fragment newInstance() {
		CalculatorHexFragment binFrag = new CalculatorHexFragment();
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
			StringTokenizer toke = new StringTokenizer(dataToBePassed, "x+-/.)(",
					true);
			StringBuilder builder = new StringBuilder();

			while (toke.hasMoreElements()) {
				String aToken = (String) toke.nextElement().toString();
				if (aToken.equals("+") || aToken.equals("x")
						|| aToken.equals("-") || aToken.equals("/")
						|| aToken.equals(".") || aToken.equals("(")
						|| aToken.equals(")")) {

					builder.append(aToken);

				} else {
					mCurrentWorkingText = Long.toHexString(Long.parseLong(
							aToken, base));
					builder.append(mCurrentWorkingText);
				}
			}
			mCurrentWorkingText = builder.toString();

			mWorkingTextView.setText(mCurrentWorkingText.toUpperCase(Locale
					.getDefault()));
		} else {
			mCurrentWorkingText = "";
			mWorkingTextView.setText(mCurrentWorkingText);
		}
	}

}
