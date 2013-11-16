package com.ACM.binarycalculator;

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

public class CalculatorOctalFragment extends Fragment {
	// this is a tag used for debugging purposes
	private static final String TAG = "CalculatorOctalFragment";
	// string constant for saving our workingTextViewText
	private static final String KEY_WORKINGTEXTVIEW_STRING = "workingTextString";
	private static final String KEY_FRAGMENT_ARGUMENTS_STRING = "fragmentArguments";

	// these are our member variables
	TextView mComputeTextView;
	TextView mWorkingTextView;
	static String mCurrentWorkingText;
	String mCurrentComputedValue;
	String mDataFromActivity;
	static boolean isOnScreen;

	@Override
	// we need to inflate our View so let's grab all the View IDs and inflate
	// them.
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to make a view instance from our layout.
		View v = inflater.inflate(R.layout.fragment_calculator_octal,
				container, false);

		// get the textViews by id, notice we have to reference them via the
		// view instance we just created.
		mComputeTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_octal_computedTextView);
		mWorkingTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_octal_workingTextView);
		// get the fragment arguments if there were any and set the working
		// textView to it
		// IGNORE THIS
		String argString = getArguments().getString(
				KEY_FRAGMENT_ARGUMENTS_STRING);
		if (argString.length() != 0) {
			Log.d(TAG, "Trying to set the frag args to:" + argString);
			// Integer workingTextViewInteger = Integer.parseInt(argString);
			// byte workingTextViewBytes = workingTextViewInteger.byteValue();
			mWorkingTextView.setText("" + argString);
		} else {
			Log.d(TAG, "Couldn't set frag args to: " + argString);
		}

		// if the we saved something away, grab it!
		if (savedInstanceState != null) {
			mCurrentWorkingText = savedInstanceState
					.getString(KEY_WORKINGTEXTVIEW_STRING);
			// set the text to be what we saved away and just now retrieved.
			mWorkingTextView.setText(mCurrentWorkingText);
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
				.findViewById(R.id.fragment_calculator_octal_tableLayout);

		// adds the values and listeners to the buttons and pretty much every
		// button except for a few
		//
		// this for loop could probably be cleaned up, because the views had
		// changed from the original and the for loop had to change as well,
		// making the for loop look like a logical mess.
		for (int i = tableLayout.getChildCount() - 2; i >= 0; i--) {
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
					if (i == tableLayout.getChildCount() - 2) {
						butt.setText("-");
						butt.setOnClickListener(genericButtonListener);
					} else if (i == tableLayout.getChildCount() - 3) {
						butt.setText("x");
						butt.setOnClickListener(genericButtonListener);
					} else if (i == tableLayout.getChildCount() - 4) {
						butt.setText("/");
						butt.setOnClickListener(genericButtonListener);
					} else if (i == tableLayout.getChildCount() - 5) {
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

			@Override
			public void onClick(View v) {
				// clear all the text in the working textView, AND maybe the
				// computed textView as well?
				// Also, might want to clear out the post fix expression stack
				mWorkingTextView.setText("");
				mCurrentWorkingText = "";
				mComputeTextView.setText("");
				
			}
		});

		// get a reference to the second row of the table (AND, OR, NAND)
		TableRow secondRow = (TableRow) tableLayout.getChildAt(1);

		Button sevenButton = (Button) secondRow.getChildAt(0);
		sevenButton.setText("7");
		sevenButton.setOnClickListener(genericButtonListener);

		Button blankButton2 = (Button) secondRow.getChildAt(1);
		blankButton2.setText("Wildcard Bitches");
		blankButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Blank Button

			}
		});

		Button blankButton = (Button) secondRow.getChildAt(2);
		blankButton.setText("");
		blankButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Blank Button

			}
		});

		// get a reference to the third row (NOR, XOR, XNOR)
		TableRow thirdRow = (TableRow) tableLayout.getChildAt(2);
		// the NOR button
		Button fourButton = (Button) thirdRow.getChildAt(0);
		fourButton.setText("4");
		fourButton.setOnClickListener(genericButtonListener);
		// XOR button
		Button fiveButton = (Button) thirdRow.getChildAt(1);
		fiveButton.setText("5");
		fiveButton.setOnClickListener(genericButtonListener);
		// XNOR button
		Button sixButton = (Button) thirdRow.getChildAt(2);
		sixButton.setText("6");
		sixButton.setOnClickListener(genericButtonListener);

		// fourth row (1, <<, >>)
		TableRow fourthRow = (TableRow) tableLayout.getChildAt(3);
		// button '1'
		Button oneButton = (Button) fourthRow.getChildAt(0);
		oneButton.setText("1");
		oneButton.setOnClickListener(genericButtonListener);
		// bitwise shift Left button
		Button twoButton = (Button) fourthRow.getChildAt(1);
		twoButton.setText("2");
		twoButton.setOnClickListener(genericButtonListener);
		// bitwise shift Right button
		Button threeButton = (Button) fourthRow.getChildAt(2);
		threeButton.setText("3");
		threeButton.setOnClickListener(genericButtonListener);

		// now we need to get the last row of buttons and get them to the
		// screen.
		TableRow lastRow = (TableRow) tableLayout.getChildAt(tableLayout
				.getChildCount() - 1);
		// set the decimal button
		Button zeroButton = (Button) lastRow.getChildAt(2);
		zeroButton.setText(".");
		zeroButton.setOnClickListener(genericButtonListener);
		// set the zero button
		Button decimalPointButton = (Button) lastRow.getChildAt(1);
		decimalPointButton.setText("0");
		decimalPointButton.setOnClickListener(genericButtonListener);
		// set the plus button
		Button plusButton = (Button) lastRow.getChildAt(3);
		plusButton.setText("+");
		plusButton.setOnClickListener(genericButtonListener);
		// set the equals button, it will have it's own separate listener to
		// compute the inputed value
		Button equalsButton = (Button) lastRow.getChildAt(0);
		equalsButton.setText("=");
		equalsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO The arithmetic for the inputed numbers. Post fix?

			}
		});
		return v;
	}

	public static Fragment newInstance(String fragmentArgumentsValue) {
		CalculatorOctalFragment binFrag = new CalculatorOctalFragment();
		Bundle bun = new Bundle();
		bun.putString(KEY_FRAGMENT_ARGUMENTS_STRING, fragmentArgumentsValue);
		binFrag.setArguments(bun);
		return binFrag;
	}

	// method to save the state of the application during the activity life
	// cycle. This is so we can preserve the values in the textViews upon screen
	// rotation.
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState");
		outState.putString(KEY_WORKINGTEXTVIEW_STRING, mCurrentWorkingText);
	}

	//
	// The code below this is a work in progress, so are some of the variables
	// declared at the top of the class.
	//

	// fragment life-cycle method
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		// set our dataPasser interface up when the fragment is on the activity
//		dataPasser = (FragmentDataPasser) activity;
//	}
//
//	// interface to pass data along from each fragment.
//	public interface FragmentDataPasser {
//		public void passData(String theData);
//	}
//
//	public void passTheData(String outData) {
//		dataPasser.passData(outData);
//	}
//
//	@Override
//	public void dataFromActivity(String inData) {
//		this.mDataFromActivity = inData;
//	}

}

