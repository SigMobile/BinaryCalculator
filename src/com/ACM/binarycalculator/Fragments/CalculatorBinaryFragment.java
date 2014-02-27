package com.ACM.binarycalculator.Fragments;

import java.math.BigInteger;
import java.util.StringTokenizer;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ACM.binarycalculator.R;
import com.ACM.binarycalculator.Activities.CalculatorFloatingPointActivity;
import com.ACM.binarycalculator.DataModels.ExpressionHouse;
import com.ACM.binarycalculator.Interfaces.FragmentDataPasser;
import com.ACM.binarycalculator.Utilities.BitwiseEvaluator;
import com.ACM.binarycalculator.Utilities.Fractions;
import com.ACM.binarycalculator.Utilities.InfixToPostfix;
import com.ACM.binarycalculator.Utilities.PostfixEvaluator;
//import android.R;

/**
 * 
 * @author James Van Gaasbeck, ACM at UCF <jjvg@knights.ucf.edu>
 * 
 * 
 */
public class CalculatorBinaryFragment extends Fragment {
	// this is a tag used for debugging purposes
	// private static final String TAG = "CalculatorBinaryFragment";

	// string constant for saving our workingTextViewText
	private static final String KEY_WORKINGTEXTVIEW_STRING = "workingTextString";
	private static final String KEY_VIEW_NUMBER = "com.ACM.binarycalculator.Fragments.Binary.ViewNumber";
	private static final String KEY_RADIX = "com.ACM.binarycalculator.Fragments.Binary.Radix";

	//mWorkingTextView is the actual textview displaying the text.
	private TextView mWorkingTextView;
	/*
	 * The mCurrentWorkingText stringBuilder variable is the current expression, not
	 * the entire list.
	 */
	private StringBuilder mCurrentWorkingText;
	/*
	 * mExpressins is the list of all the expressions
	 */
	private ExpressionHouse mExpressions;
	private int positionInPager;
	private static int viewsRadix;
	private FragmentDataPasser mCallback;
	public static int numberOfOpenParenthesis;
	public static int numberOfClosedParenthesis;
	public static int numberOfOperators;
	private ScrollView scrollView;
	private View.OnClickListener genericBinaryNumberButtonListener;

	// we need to inflate our View so let's grab all the View IDs and inflate
	// them.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to make a view instance from our layout.
		View v = inflater.inflate(R.layout.fragment_calculator_binary,
				container, false);

		positionInPager = getArguments().getInt(KEY_VIEW_NUMBER);
		viewsRadix = getArguments().getInt(KEY_RADIX);

		scrollView = (ScrollView) v
				.findViewById(R.id.fragment_calculator_binary_scrollView);

		// get the textViews by id, notice we have to reference them via the
		// view instance we just created.
		mWorkingTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_binary_workingTextView);

		// initialize variables that need to be
		mCurrentWorkingText = new StringBuilder("");
		mExpressions = new ExpressionHouse();

		// if the we saved something away, grab it!
		if (savedInstanceState != null) {
			mExpressions = (ExpressionHouse) savedInstanceState
					.getStringArrayList(KEY_WORKINGTEXTVIEW_STRING);

			// set the text to be what we saved away and just now retrieved.
			mWorkingTextView.setText(mExpressions.printAllExpressions());
			mCurrentWorkingText.append(mExpressions.getCurrentExpression());
		}

		genericBinaryNumberButtonListener = new View.OnClickListener() {
			// when someone clicks a button that isn't "special" we are going to
			// add it to the workingTextView
			@Override
			public void onClick(View v) {

				TextView textView = (TextView) v;
				CharSequence textFromButton = textView.getText();

				StringBuilder textViewBuilder = new StringBuilder(
						mWorkingTextView.getText());
				// StringBuilder currentWorkingTextBuilder = new StringBuilder(
				// mCurrentWorkingText);

				if (mCurrentWorkingText.length() <= 47) {
					String tokeString = mCurrentWorkingText.toString()
							+ textFromButton.toString();
					StringTokenizer toke = new StringTokenizer(tokeString,
							"-+/x)( ");
					String numberLengthTest = null;
					while (toke.hasMoreTokens()) {
						numberLengthTest = (String) toke.nextToken().toString();
					}
					if (numberLengthTest.length() > 11) {
						return;
					}

					// Log.d(TAG,
					// "**TextView before: "
					// + mWorkingTextView.getText().toString()
					// + " CurrentWorkingText: "
					// + mCurrentWorkingText.toString()
					// + " TxtFromButton: " + textFromButton
					// + " **");

					CharSequence newTextViewText = (CharSequence) textViewBuilder
							.append(textFromButton);
					mWorkingTextView.setText(newTextViewText);
					mCurrentWorkingText.append(textFromButton);

					// Log.d(TAG,
					// "**TextView After: "
					// + mWorkingTextView.getText().toString()
					// + " CurrentWorkingText: "
					// + mCurrentWorkingText.toString());

				}

				onPassData(mCurrentWorkingText.toString(), false);
				mExpressions.updateExpressions(mCurrentWorkingText.toString());
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
				CharSequence textFromButton = textView.getText();

				StringBuilder textViewBuilder = new StringBuilder(
						mWorkingTextView.getText());

				// see if the workingTextView is empty, if so DON'T add the
				// operator
				if (mCurrentWorkingText.length() == 0) {
					if (mExpressions.getMostRecentAnswer() != null
							&& mExpressions.getMostRecentAnswer().length() > 0) {

						CharSequence newTextViewText = (CharSequence) textViewBuilder
								.append(mExpressions.getMostRecentAnswer())
								.append(" ").append(textFromButton).append(" ");

						mWorkingTextView.setText(newTextViewText);
						mCurrentWorkingText.append(mExpressions.getMostRecentAnswer())
						.append(" ").append(textFromButton).append(" ");
					}
				} else {

					if (mCurrentWorkingText.toString().length() <= 47) {
						// we can't have adjacent "+/x" nor can we have a "."
						// followed by "+/x"
						if (mCurrentWorkingText.toString().endsWith("+ ")
								|| mCurrentWorkingText.toString()
										.endsWith("x ")
								|| mCurrentWorkingText.toString()
										.endsWith("/ ")
								|| mCurrentWorkingText.toString().endsWith(".")
								|| mCurrentWorkingText.toString()
										.endsWith("- ")
								|| mCurrentWorkingText.toString().endsWith("-")
								|| mCurrentWorkingText.toString()
										.endsWith("( ")
								|| mCurrentWorkingText.toString().contains("O")
								|| mCurrentWorkingText.toString().contains("N")) {
							// do nothing because we can't have multiple
							// adjacent
							// operators

						} else {
							// we're safe to add the operator to the expression

							if (mCurrentWorkingText.toString().endsWith(" ")) {
								// if the last char in the currentExpression was
								// a space then don't add the space at the
								// beginning, because there will be an extra
								// space there making it look weird and mess up
								// the calculations.

								CharSequence newTextViewText = (CharSequence) textViewBuilder
										.append(textFromButton).append(" ");

								mWorkingTextView.setText(newTextViewText);
								mCurrentWorkingText.append(textFromButton)
										.append(" ");

								// CalculatorDecimalFragment.numberOfOperators++;
								// CalculatorOctalFragment.numberOfOperators++;
								// CalculatorBinaryFragment.numberOfOperators++;
								// CalculatorHexFragment.numberOfOperators++;

							} else {

								CharSequence newTextViewText = (CharSequence) textViewBuilder
										.append(" ").append(textFromButton)
										.append(" ");

								mWorkingTextView.setText(newTextViewText);

								mCurrentWorkingText.append(" ")
										.append(textFromButton).append(" ");

								// CalculatorDecimalFragment.numberOfOperators++;
								// CalculatorOctalFragment.numberOfOperators++;
								// CalculatorBinaryFragment.numberOfOperators++;
								// CalculatorHexFragment.numberOfOperators++;
							}
						}
					}
				}
				// Log.d(TAG, "**Operator, number of operators: "
				// + numberOfOperators);
				onPassData(mCurrentWorkingText.toString(), false);
				mExpressions.updateExpressions(mCurrentWorkingText.toString());
			}
		};

		View.OnClickListener openParenthesisButtonListener = new View.OnClickListener() {
			// We can't have a "." followed by a "("
			// We also can't have something like this "6)"
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;

				CharSequence textFromButton = textView.getText();
				CharSequence newTextViewText = null;

				StringBuilder textViewBuilder = new StringBuilder(
						mWorkingTextView.getText());

				if (mCurrentWorkingText.toString().length() == 0) {
					// if the first thing is a "(" then don't add the
					// unnecessary space at the front of it.
					newTextViewText = (CharSequence) textViewBuilder.append(
							textFromButton).append(" ");

					mWorkingTextView.setText(newTextViewText);
					mCurrentWorkingText.append(textFromButton).append(" ");

					CalculatorDecimalFragment.numberOfOpenParenthesis++;
					CalculatorBinaryFragment.numberOfOpenParenthesis++;
					CalculatorHexFragment.numberOfOpenParenthesis++;
					CalculatorOctalFragment.numberOfOpenParenthesis++;
				} else {

					if (mCurrentWorkingText.toString().endsWith(".")
							|| mCurrentWorkingText.toString().endsWith("D ")
							|| mCurrentWorkingText.toString().endsWith("R ")) {
						// do nothing
					} else {
						if (mCurrentWorkingText.length() <= 47) {

							// add an implied 'x' behind the scenes for cases
							// like this "4 ( 4 )"
							if (mCurrentWorkingText.length() > 0) {
								Character isAnumberTest = mCurrentWorkingText
										.charAt(mCurrentWorkingText.length() - 1);
								if (isOperand(isAnumberTest.toString())
										|| mCurrentWorkingText.toString()
												.endsWith(") ")) {

									if (mCurrentWorkingText.toString()
											.endsWith(") ")) {

										newTextViewText = (CharSequence) textViewBuilder
												.append(" ")
												.append(textFromButton)
												.append(" ");

										mWorkingTextView
												.setText(newTextViewText);
										mCurrentWorkingText.append(" ")
												.append(textFromButton)
												.append(" ");

									} else {

										newTextViewText = (CharSequence) textViewBuilder
												.append(" ")
												.append(textFromButton)
												.append(" ");

										mWorkingTextView
												.setText(newTextViewText);
										mCurrentWorkingText.append(" ")
												.append(textFromButton)
												.append(" ");
									}

									// CalculatorDecimalFragment.numberOfOperators++;
									// CalculatorBinaryFragment.numberOfOperators++;
									// CalculatorHexFragment.numberOfOperators++;
									// CalculatorOctalFragment.numberOfOperators++;
								} else {

									newTextViewText = (CharSequence) textViewBuilder
											.append(textFromButton).append(" ");

									mWorkingTextView.setText(newTextViewText);
									mCurrentWorkingText.append(textFromButton)
											.append(" ");
								}
							} else {
								newTextViewText = (CharSequence) textViewBuilder
										.append(textFromButton).append(" ");

								mWorkingTextView.setText(newTextViewText);
								mCurrentWorkingText.append(textFromButton)
										.append(" ");
							}

							CalculatorDecimalFragment.numberOfOpenParenthesis++;
							CalculatorBinaryFragment.numberOfOpenParenthesis++;
							CalculatorHexFragment.numberOfOpenParenthesis++;
							CalculatorOctalFragment.numberOfOpenParenthesis++;
						}
					}

				}
				// Log.d(TAG, "**OpenParenthesis, number of operators: "
				// + numberOfOperators);
				onPassData(mCurrentWorkingText.toString(), false);
				mExpressions.updateExpressions(mCurrentWorkingText.toString());
			}

		};

		View.OnClickListener closeParenthesisButtonListener = new View.OnClickListener() {
			// We can't have any of these "./+-x" followed by a ")" nor can we
			// have something like this "()"
			// We also can't have something like this "6)" nor something like
			// "(4x4)9)"
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;

				CharSequence textFromButton = textView.getText();
				CharSequence newTextViewText = null;

				StringBuilder textViewBuilder = new StringBuilder(
						mWorkingTextView.getText());

				if (mCurrentWorkingText.toString().length() == 0) {
					// do nothing we can't start with ")"
				} else {

					if (mCurrentWorkingText.length() <= 47) {
						if (((mCurrentWorkingText.toString().endsWith(".")
								|| mCurrentWorkingText.toString()
										.endsWith("/ ")
								|| mCurrentWorkingText.toString()
										.endsWith("x ")
								|| mCurrentWorkingText.toString()
										.endsWith("+ ")
								|| mCurrentWorkingText.toString()
										.endsWith("- ")
								|| mCurrentWorkingText.toString().endsWith("-") || mCurrentWorkingText
								.toString().endsWith("( ")))
								|| numberOfClosedParenthesis >= numberOfOpenParenthesis) {
							// do nothing
						} else {

							newTextViewText = (CharSequence) textViewBuilder
									.append(" ").append(textFromButton)
									.append(" ");

							mWorkingTextView.setText(newTextViewText);
							mCurrentWorkingText.append(" ")
									.append(textFromButton).append(" ");

							CalculatorBinaryFragment.numberOfClosedParenthesis++;
							CalculatorDecimalFragment.numberOfClosedParenthesis++;
							CalculatorOctalFragment.numberOfClosedParenthesis++;
							CalculatorHexFragment.numberOfClosedParenthesis++;
						}
					}
				}
				// Log.d(TAG, "**ClosedParenthesis, number of operators: "
				// + numberOfOperators);
				onPassData(mCurrentWorkingText.toString(), false);
				mExpressions.updateExpressions(mCurrentWorkingText.toString());
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

				CharSequence textFromButton = textView.getText();
				CharSequence newTextViewText = null;

				StringBuilder textViewBuilder = new StringBuilder(
						mWorkingTextView.getText());

				// see if the workingTextView is empty
				if (mCurrentWorkingText.toString().length() == 0) {

					newTextViewText = (CharSequence) textViewBuilder
							.append(textFromButton);

					mWorkingTextView.setText(newTextViewText);
					mCurrentWorkingText.append(textFromButton);

				} else if (mCurrentWorkingText.length() == 1
						&& mCurrentWorkingText.toString().endsWith("-")) {
					// do nothing so we don't start out with something like this
					// "--2"
				} else {

					if (mCurrentWorkingText.length() <= 47) {
						// we can't have more than 2 adjacent '-'. So get the
						// last
						// two char's and check if it's "--"
						if (mCurrentWorkingText.toString().endsWith(".")
								|| mCurrentWorkingText.toString().endsWith("-")
								|| mCurrentWorkingText.toString()
										.endsWith("(-")
								|| mCurrentWorkingText.toString().contains("O")
								|| mCurrentWorkingText.toString().contains("N")) {
							// do nothing because we can't have more than 2
							// adjacent minus's
						} else {
							// otherwise, add it to the view
							if (mCurrentWorkingText.toString().endsWith("0")
									|| mCurrentWorkingText.toString().endsWith(
											"1")
									|| mCurrentWorkingText.toString().endsWith(
											"2")
									|| mCurrentWorkingText.toString().endsWith(
											"3")
									|| mCurrentWorkingText.toString().endsWith(
											"4")
									|| mCurrentWorkingText.toString().endsWith(
											"5")
									|| mCurrentWorkingText.toString().endsWith(
											"6")
									|| mCurrentWorkingText.toString().endsWith(
											"7")
									|| mCurrentWorkingText.toString().endsWith(
											"8")
									|| mCurrentWorkingText.toString().endsWith(
											"9")
									|| mCurrentWorkingText.toString().endsWith(
											") ")) {

								// if the last thing was a parenthesis make sure
								// that we don't add in an extraneous space.
								if (mCurrentWorkingText.toString().endsWith(
										") ")) {

									newTextViewText = (CharSequence) textViewBuilder
											.append(textFromButton).append(" ");

									mWorkingTextView.setText(newTextViewText);
									mCurrentWorkingText.append(textFromButton)
											.append(" ");

								} else {

									newTextViewText = (CharSequence) textViewBuilder
											.append(" ").append(textFromButton)
											.append(" ");

									mWorkingTextView.setText(newTextViewText);
									mCurrentWorkingText.append(" ")
											.append(textFromButton).append(" ");
								}
							} else {
								// this represents a negative sign, not a minus
								// sign

								newTextViewText = (CharSequence) textViewBuilder
										.append(textFromButton);

								mWorkingTextView.setText(newTextViewText);
								mCurrentWorkingText.append(textFromButton);
							}
						}
					}
				}
				// need to pass data to our call back so all fragments can
				// be
				// updated with the new workingTextView
				// Log.d(TAG, "**Negative/Minus, number of operators: "
				// + numberOfOperators);
				onPassData(mCurrentWorkingText.toString(), false);
				mExpressions.updateExpressions(mCurrentWorkingText.toString());
			}
		};

		// logic in here is pretty messy because there are a lot of cases to
		// check for
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

						if (mCurrentWorkingText.toString().endsWith(") ")) {
							CalculatorDecimalFragment.numberOfClosedParenthesis--;
							CalculatorBinaryFragment.numberOfClosedParenthesis--;
							CalculatorHexFragment.numberOfClosedParenthesis--;
							CalculatorOctalFragment.numberOfClosedParenthesis--;
						} else if (mCurrentWorkingText.toString().endsWith("(")
								|| mCurrentWorkingText.toString()
										.endsWith("( ")
								|| mCurrentWorkingText.toString().endsWith(
										" ( ")) {
							CalculatorDecimalFragment.numberOfOpenParenthesis--;
							CalculatorBinaryFragment.numberOfOpenParenthesis--;
							CalculatorHexFragment.numberOfOpenParenthesis--;
							CalculatorOctalFragment.numberOfOpenParenthesis--;
						}

						if (mCurrentWorkingText.toString().endsWith(" + ( ")
								|| mCurrentWorkingText.toString().endsWith(
										" - ( ")
								|| mCurrentWorkingText.toString().endsWith(
										" x ( ")
								|| mCurrentWorkingText.toString().endsWith(
										" / ( ")) {

							// this deletes the last 2 char's
							mCurrentWorkingText.delete(
									mCurrentWorkingText.length() - 2,
									mCurrentWorkingText.length());

							mWorkingTextView
									.setText(mWorkingTextView
											.getText()
											.toString()
											.substring(
													0,
													mWorkingTextView.length() - 2));
						}

						else if (mCurrentWorkingText.toString().endsWith(" ( ")) {

							// this deletes the last 3 char's
							mCurrentWorkingText.delete(
									mCurrentWorkingText.length() - 3,
									mCurrentWorkingText.length());

							mWorkingTextView
									.setText(mWorkingTextView
											.getText()
											.toString()
											.substring(
													0,
													mWorkingTextView.length() - 3));

						}

						else if (mCurrentWorkingText.toString().endsWith(
								" AND ")
								|| mCurrentWorkingText.toString().endsWith(
										" NOR ")
								|| mCurrentWorkingText.toString().endsWith(
										" XOR ")) {

							// this deletes the bitwise operation and spaces
							mCurrentWorkingText.delete(
									mCurrentWorkingText.length() - 5,
									mCurrentWorkingText.length());

							mWorkingTextView
									.setText(mWorkingTextView
											.getText()
											.toString()
											.substring(
													0,
													mWorkingTextView.length() - 5));
						}

						else if (mCurrentWorkingText.toString().endsWith(
								" NAND ")
								|| mCurrentWorkingText.toString().endsWith(
										" XNOR ")) {

							// this deletes the bitwise operation and spaces
							mCurrentWorkingText.delete(
									mCurrentWorkingText.length() - 6,
									mCurrentWorkingText.length());

							mWorkingTextView
									.setText(mWorkingTextView
											.getText()
											.toString()
											.substring(
													0,
													mWorkingTextView.length() - 6));
						}

						else if (mCurrentWorkingText.toString()
								.endsWith(" OR ")) {

							// this deletes the bitwise operation and spaces
							mCurrentWorkingText.delete(
									mCurrentWorkingText.length() - 4,
									mCurrentWorkingText.length());

							mWorkingTextView
									.setText(mWorkingTextView
											.getText()
											.toString()
											.substring(
													0,
													mWorkingTextView.length() - 4));
						}

						// we need to delete the spaces around the operators
						// also, not just the last char added to the
						// workingTextView
						else if (mCurrentWorkingText.toString().endsWith(" + ")
								|| mCurrentWorkingText.toString().endsWith(
										" - ")
								|| mCurrentWorkingText.toString().endsWith(
										" x ")
								|| mCurrentWorkingText.toString().endsWith(
										" / ")
								|| mCurrentWorkingText.toString()
										.endsWith(") ")
								|| mCurrentWorkingText.toString().endsWith(
										" ( ")) {

							if (mCurrentWorkingText.toString().endsWith(") x ")
									|| mCurrentWorkingText.toString().endsWith(
											") + ")
									|| mCurrentWorkingText.toString().endsWith(
											") - ")
									|| mCurrentWorkingText.toString().endsWith(
											") / ")) {

								mCurrentWorkingText.delete(
										mCurrentWorkingText.length() - 2,
										mCurrentWorkingText.length());

								mWorkingTextView.setText(mWorkingTextView
										.getText()
										.toString()
										.substring(0,
												mWorkingTextView.length() - 2));
							} else {

								// this deletes the last three char's
								mCurrentWorkingText.delete(
										mCurrentWorkingText.length() - 3,
										mCurrentWorkingText.length());

								mWorkingTextView.setText(mWorkingTextView
										.getText()
										.toString()
										.substring(0,
												mWorkingTextView.length() - 3));
							}

						} else if (mCurrentWorkingText.toString()
								.endsWith("( ")) {
							// only delete two chars if the user started
							// with an
							// open parenthesis
							mCurrentWorkingText.delete(
									mCurrentWorkingText.length() - 2,
									mCurrentWorkingText.length());

							mWorkingTextView
									.setText(mWorkingTextView
											.getText()
											.toString()
											.substring(
													0,
													mWorkingTextView.length() - 2));

						} else {

							mCurrentWorkingText.delete(
									mCurrentWorkingText.length() - 1,
									mCurrentWorkingText.length());

							mWorkingTextView
									.setText(mWorkingTextView
											.getText()
											.toString()
											.substring(
													0,
													mWorkingTextView.length() - 1));
						}
					} else {
						return;
					}
				}
				// Log.d(TAG, "**Backspace, number of operators: "
				// + numberOfOperators);
				onPassData(mCurrentWorkingText.toString(), true);
				mExpressions.updateExpressions(mCurrentWorkingText.toString());

			}
		};

		// View.OnClickListener floatingPointListener = new
		// View.OnClickListener() {
		// // We want to start a new activity with the floating point view
		// // inside of it.
		// @Override
		// public void onClick(View v) {
		// Intent startFloatingPoint = new Intent(getSherlockActivity(),
		// CalculatorFloatingPointActivity.class);
		// startActivity(startFloatingPoint);
		// getSherlockActivity().getSupportFragmentManager()
		// .beginTransaction().addToBackStack(null).commit();
		// }
		// };

		// get a reference to our TableLayout XML
		TableLayout tableLayout = (TableLayout) v
				.findViewById(R.id.fragment_calculator_binary_tableLayout);

		// get a reference to the first (topmost) row so we can set the clear
		// all button manually, because it was annoying trying to work it in to
		// the for loop
		TableRow firstRow = (TableRow) tableLayout.getChildAt(0);
		// the clear all button was decided to be the third button in the

		Button floatintPointButt = (Button) firstRow.getChildAt(0);
		floatintPointButt.setText("(");
		floatintPointButt.setOnClickListener(openParenthesisButtonListener);
		Button twosCompButt = (Button) firstRow.getChildAt(1);
		twosCompButt.setText(")");
		twosCompButt.setOnClickListener(closeParenthesisButtonListener);

		// topmost row
		Button clearAllButton = (Button) firstRow.getChildAt(2);
		clearAllButton.setText("AC");
		clearAllButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// clear all the text in the working textView, AND maybe the
				// computed textView as well?
				// Also, might want to clear out the post fix expression stack
				mWorkingTextView.setText("");
				mCurrentWorkingText = new StringBuilder("");
				mExpressions.clearAllExpressions();

				CalculatorDecimalFragment.numberOfOpenParenthesis = 0;
				CalculatorBinaryFragment.numberOfOpenParenthesis = 0;
				CalculatorHexFragment.numberOfOpenParenthesis = 0;
				CalculatorOctalFragment.numberOfOpenParenthesis = 0;

				CalculatorDecimalFragment.numberOfClosedParenthesis = 0;
				CalculatorBinaryFragment.numberOfClosedParenthesis = 0;
				CalculatorHexFragment.numberOfClosedParenthesis = 0;
				CalculatorOctalFragment.numberOfClosedParenthesis = 0;

				CalculatorDecimalFragment.numberOfOperators = 0;
				CalculatorBinaryFragment.numberOfOperators = 0;
				CalculatorHexFragment.numberOfOperators = 0;
				CalculatorOctalFragment.numberOfOperators = 0;

				onPassData(mCurrentWorkingText.toString(), false);
				mExpressions.updateExpressions(mCurrentWorkingText.toString());
			}
		});

		View.OnClickListener bitWiseListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				TextView textView = (TextView) v;

				CharSequence textFromButton = textView.getText();
				CharSequence newTextViewText = null;

				StringBuilder textViewBuilder = new StringBuilder(
						mWorkingTextView.getText());

				if (mCurrentWorkingText.length() != 0
						&& !(mCurrentWorkingText.toString().contains("N")
								|| mCurrentWorkingText.toString().contains("O")
								|| mCurrentWorkingText.toString().contains(".")
								|| mCurrentWorkingText.toString().contains("+")
								|| mCurrentWorkingText.toString().contains("-")
								|| mCurrentWorkingText.toString().contains("x")
								|| mCurrentWorkingText.toString().contains("/")
								|| mCurrentWorkingText.toString().contains("(") || mCurrentWorkingText
								.toString().contains(")"))) {

					newTextViewText = (CharSequence) textViewBuilder
							.append(" ").append(textFromButton).append(" ");

					mWorkingTextView.setText(newTextViewText);
					mCurrentWorkingText.append(" ").append(textFromButton)
							.append(" ");
				}
				onPassData(mCurrentWorkingText.toString(), false);
				mExpressions.updateExpressions(mCurrentWorkingText.toString());
			}
		};

		ImageButton backspaceButton = (ImageButton) firstRow.getChildAt(3);
		backspaceButton.setOnClickListener(backspaceButtonListener);

		// get a reference to the second row of the table (AND, OR, NAND)
		TableRow secondRow = (TableRow) tableLayout.getChildAt(1);

		Button andButton = (Button) secondRow.getChildAt(0);
		andButton.setText("AND");
		andButton.setOnClickListener(bitWiseListener);
		Button orButton = (Button) secondRow.getChildAt(1);
		orButton.setText("OR");
		orButton.setOnClickListener(bitWiseListener);
		Button nandButton = (Button) secondRow.getChildAt(2);
		nandButton.setText("NAND");
		nandButton.setOnClickListener(bitWiseListener);

		Button divedeButt = (Button) secondRow.getChildAt(3);
		divedeButt.setText("/");
		divedeButt.setOnClickListener(genericOperatorButtonListener);

		// get a reference to the third row (NOR, XOR, XNOR)
		TableRow thirdRow = (TableRow) tableLayout.getChildAt(2);
		// the NOR button
		Button norButton = (Button) thirdRow.getChildAt(0);
		norButton.setText("NOR");
		norButton.setOnClickListener(bitWiseListener);
		// XOR button
		Button xorButton = (Button) thirdRow.getChildAt(1);
		xorButton.setText("XOR");
		xorButton.setOnClickListener(bitWiseListener);
		// XNOR button
		Button xnorButton = (Button) thirdRow.getChildAt(2);
		xnorButton.setText("XNOR");
		xnorButton.setOnClickListener(bitWiseListener);

		Button multButt = (Button) thirdRow.getChildAt(3);
		multButt.setText("x");
		multButt.setOnClickListener(genericOperatorButtonListener);

		// fourth row (1, <<, >>)
		TableRow fourthRow = (TableRow) tableLayout.getChildAt(3);
		// button '1'
		Button oneButton = (Button) fourthRow.getChildAt(0);
		oneButton.setText("1");
		oneButton.setOnClickListener(genericBinaryNumberButtonListener);
		// bitwise shift Left button
		Button floatingPointButton = (Button) fourthRow.getChildAt(1);
		floatingPointButton.setText("IEEE");
		floatingPointButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), CalculatorFloatingPointActivity.class);
				startActivity(myIntent);
				return;
			}
		});

		// bitwise shift Right button
		Button bitwiseShiftRightButton = (Button) fourthRow.getChildAt(2);
		bitwiseShiftRightButton.setText("");
		bitwiseShiftRightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// do nothing
				return;
			}
		});

		Button minusButt = (Button) fourthRow.getChildAt(3);
		minusButt.setText("-");
		minusButt.setOnClickListener(genericMinusButtonListener);

		// now we need to get the last row of buttons and get them to the
		// screen.
		TableRow lastRow = (TableRow) tableLayout.getChildAt(tableLayout
				.getChildCount() - 1);
		// set the decimal button
		Button zeroButton = (Button) lastRow.getChildAt(2);
		zeroButton.setText(".");
		zeroButton.setOnClickListener(new OnClickListener() {
			// we can't put a "." up there if there has already been one in
			// the current token (number)
			@Override
			public void onClick(View v) {

				TextView textView = (TextView) v;

				CharSequence textFromButton = textView.getText();
				CharSequence newTextViewText = null;

				StringBuilder textViewBuilder = new StringBuilder(
						mWorkingTextView.getText());

				// see if the workingTextView is empty, if so just add the '.'
				if (mCurrentWorkingText.length() == 0) {

					newTextViewText = (CharSequence) textViewBuilder
							.append(textFromButton);

					mWorkingTextView.setText(newTextViewText);
					mCurrentWorkingText.append(textFromButton);

				} else {

					if (mCurrentWorkingText.length() <= 47) {
						StringTokenizer toke = new StringTokenizer(
								mCurrentWorkingText.toString(), "+-/x)(", true);
						String currentElement = null;
						// get the current(last) token(number) so we can test if
						// it
						// has a '.' in it.
						while (toke.hasMoreTokens()) {
							currentElement = toke.nextElement().toString();
						}
						// if the working TextView isn't zero we need to append
						// the
						// textFromButton to what is already there. AND we need
						// to
						// check if the current token already has a '.' in it
						// because we can't have something like '2..2' or
						// 2.2.33'
						if (mCurrentWorkingText.toString().endsWith(".")
								|| currentElement.contains(".")
								|| mCurrentWorkingText.toString().contains("O")
								|| mCurrentWorkingText.toString().contains("N")) {
							// do nothing here so we don't end up with
							// expressions
							// like "2..2" or "2.3.22"
						} else {
							// otherwise we're all good and just add the ".' up
							// there.
							newTextViewText = (CharSequence) textViewBuilder
									.append(textFromButton);

							mWorkingTextView.setText(newTextViewText);
							mCurrentWorkingText.append(textFromButton);
						}
					}
				}

				onPassData(mCurrentWorkingText.toString(), false);
				mExpressions.updateExpressions(mCurrentWorkingText.toString());
			}
		});
		// set the zero button
		Button decimalPointButton = (Button) lastRow.getChildAt(1);
		decimalPointButton.setText("0");
		decimalPointButton
				.setOnClickListener(genericBinaryNumberButtonListener);
		// set the plus button
		Button plusButton = (Button) lastRow.getChildAt(3);
		plusButton.setText("+");
		plusButton.setOnClickListener(genericOperatorButtonListener);
		// set the equals button, it will have it's own separate listener to
		// compute the inputed value
		Button equalsButton = (Button) lastRow.getChildAt(0);
		equalsButton.setText("=");
		equalsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mCurrentWorkingText.toString().endsWith("-")) {
					Toast.makeText(getActivity(),
							"That is not a valid expression.",
							Toast.LENGTH_SHORT).show();

					CalculatorDecimalFragment.numberOfOperators = 0;
					CalculatorBinaryFragment.numberOfOperators = 0;
					CalculatorHexFragment.numberOfOperators = 0;
					CalculatorOctalFragment.numberOfOperators = 0;

					return;
				}

				String answer = null;
				// if the text contains a bitwise operator use the bitwise
				// evaluator over the postFix evaluator.
				if (mCurrentWorkingText.toString().contains("O")
						|| mCurrentWorkingText.toString().contains("N")) {

					String[] expressionCheck = mCurrentWorkingText.toString()
							.split(" ");
					if (expressionCheck.length != 3) {
						Toast.makeText(getActivity(),
								"Not a valid bitwise expression.",
								Toast.LENGTH_SHORT).show();

						CalculatorDecimalFragment.numberOfOperators = 0;
						CalculatorBinaryFragment.numberOfOperators = 0;
						CalculatorHexFragment.numberOfOperators = 0;
						CalculatorOctalFragment.numberOfOperators = 0;

						return;
					}

					answer = BitwiseEvaluator.Evaluate(
							mCurrentWorkingText.toString(), getActivity());
					// if the expression is nothing it means that it was not a
					// valid bitwise expression. So not show anything and let
					// the user know.
					if (answer.equals("") || answer.length() == 0) {
						Toast.makeText(getActivity(),
								"Not a valid bitwise expression.",
								Toast.LENGTH_SHORT).show();

						CalculatorDecimalFragment.numberOfOperators = 0;
						CalculatorBinaryFragment.numberOfOperators = 0;
						CalculatorHexFragment.numberOfOperators = 0;
						CalculatorOctalFragment.numberOfOperators = 0;

						return;
					}

					answer = "\n" + "\t" + "\t" + answer + "\n";
				} else {

					// need to convert the mCurrentWorkingText (the current
					// expression) to base10 before we do any evaluations.
					StringTokenizer toke = new StringTokenizer(
							mCurrentWorkingText.toString(), "x+-/)( \n\t", true);
					StringBuilder builder = new StringBuilder();

					while (toke.hasMoreElements()) {
						String aToken = (String) toke.nextElement().toString();
						if (aToken.equals("+") || aToken.equals("x")
								|| aToken.equals("-") || aToken.equals("/")
								|| aToken.equals("(") || aToken.equals(")")
								|| aToken.equals(" ") || aToken.equals("\n")
								|| aToken.equals("\t")) {

							builder.append(aToken);

						}
						// if our token contains a "." in it then that means
						// that we
						// need to do some conversion trickery
						else if (aToken.contains(".")) {
							if (aToken.endsWith(".")) {
								// don't do anything if a token ends with "." we
								// don't want cases like ".5 + 5."
								return;
							}
							// split the string around the "." delimiter.
							String[] parts = aToken.split("\\.");
							StringBuilder tempBuilder = new StringBuilder();

							if (aToken.charAt(0) == '.') {
								// so it doesn't break on cases like ".5"
							} else {
								// add the portion of the number to the left of
								// the
								// "."
								// to our string, this doesn't need any
								// conversion
								// nonsense because it is a whole number.
								tempBuilder.append(Long.toString(Long
										.parseLong(parts[0], viewsRadix)));
							}
							// convert the fraction portion
							String getRidOfZeroBeforePoint = null;

							// convert just the fraction portion of the number
							// to
							// base10. This method doesn't take in the "." with
							// the
							// fraction.
							getRidOfZeroBeforePoint = Fractions
									.convertFractionPortionToDecimal(parts[1],
											viewsRadix);

							// the conversion returns just the fraction
							// portion
							// with
							// a "0" to the left of the ".", so let's get
							// rid of
							// that extra zero.
							getRidOfZeroBeforePoint = getRidOfZeroBeforePoint
									.substring(1,
											getRidOfZeroBeforePoint.length());

							tempBuilder.append(getRidOfZeroBeforePoint);

							builder.append(tempBuilder.toString());
						}// closes the "." case
						else {
							// if it's just a regular good ol' fashioned whole
							// number, use java's parseInt method to convert to
							// base10
							builder.append(Long.parseLong(aToken, viewsRadix));
						}
					} // closes while() loop

					// /Now convert the base10 expression into post-fix
					String postfix = InfixToPostfix.convertToPostfix(
							builder.toString(), getActivity());

					if (postfix.equals("") || postfix.length() == 0) {
						return;
					}

					// tokenize to see if the expression is in fact a valid
					// expression, i.e contains an operator, contains the
					// correct
					// operand to operator ratio
					StringTokenizer toker = new StringTokenizer(
							mCurrentWorkingText.toString(), "+-/x )(");
					// Log.d(TAG, "Number of operands: " + toker.countTokens()
					// + " NumberOfOperators: " + numberOfOperators);

					// the number of operators should be one less than the
					// number of
					// operands/tokens
					if (numberOfOperators != toker.countTokens() - 1
							|| numberOfOperators == 0) {
						Toast.makeText(getActivity(),
								"That is not a valid expression.",
								Toast.LENGTH_SHORT).show();

						CalculatorDecimalFragment.numberOfOperators = 0;
						CalculatorBinaryFragment.numberOfOperators = 0;
						CalculatorHexFragment.numberOfOperators = 0;
						CalculatorOctalFragment.numberOfOperators = 0;

						return;
					}

					String theAnswerInDecimal = null;
					if (postfix != null && postfix.length() > 0) {
						if (!(postfix.contains("+") || postfix.contains("-")
								|| postfix.contains("x") || postfix
								.contains("/"))) {
							// don't evaluate if there is an expression with no
							// operators
							Toast.makeText(
									getActivity(),
									"There are no operators in the expression.",
									Toast.LENGTH_LONG).show();

							CalculatorDecimalFragment.numberOfOperators = 0;
							CalculatorBinaryFragment.numberOfOperators = 0;
							CalculatorHexFragment.numberOfOperators = 0;
							CalculatorOctalFragment.numberOfOperators = 0;

							return;
						} else if (numberOfOpenParenthesis != numberOfClosedParenthesis) {
							// don't evaluate if the number of closed and open
							// parenthesis aren't equal.
							Toast.makeText(
									getActivity(),
									"The number of close parentheses is not equal to the number of open parentheses.",
									Toast.LENGTH_LONG).show();

							CalculatorDecimalFragment.numberOfOperators = 0;
							CalculatorBinaryFragment.numberOfOperators = 0;
							CalculatorHexFragment.numberOfOperators = 0;
							CalculatorOctalFragment.numberOfOperators = 0;

							return;
						}
						// Do the evaluation if it's safe to.
						theAnswerInDecimal = PostfixEvaluator.evaluate(postfix);
					} else {
						// don't evaluate if the expression is null or empty
						// Toast.makeText(getSherlockActivity(),
						// "The expression is empty.", Toast.LENGTH_LONG)
						// .show();

						CalculatorDecimalFragment.numberOfOperators = 0;
						CalculatorBinaryFragment.numberOfOperators = 0;
						CalculatorHexFragment.numberOfOperators = 0;
						CalculatorOctalFragment.numberOfOperators = 0;

						return;
					}

					// Log.d(TAG, "**Postfix: " + postfix + " AnswerInDecimal: "
					// + theAnswerInDecimal);

					String[] answerParts = theAnswerInDecimal.split("\\.");
					StringBuilder answerInCorrectBase = null;
					if (answerParts[0].contains("-")) {
						String[] parseOutNegativeSign = answerParts[0]
								.split("-");
						answerInCorrectBase = new StringBuilder(Long
								.toBinaryString(Long
										.parseLong(parseOutNegativeSign[1])));

						answerInCorrectBase.insert(0, "-");

					} else {
						answerInCorrectBase = new StringBuilder(Long
								.toBinaryString(Long.parseLong(answerParts[0])));
					}

					String fractionPart = null;
					if (answerParts.length > 1) {
						fractionPart = Fractions
								.convertFractionPortionFromDecimal("."
										+ answerParts[1], viewsRadix);
						if (!fractionPart.equals("")) {
							answerInCorrectBase.append("." + fractionPart);
						}
					}

					answer = "\n" + "\t" + "\t"
							+ answerInCorrectBase.toString() + "\n";
				}

				mWorkingTextView.setText(mWorkingTextView.getText().toString()
						.concat(answer));

				mExpressions.updateExpressions(answer);
				onPassData(answer, false);

				mCurrentWorkingText = new StringBuilder("");

				CalculatorDecimalFragment.numberOfOpenParenthesis = 0;
				CalculatorBinaryFragment.numberOfOpenParenthesis = 0;
				CalculatorHexFragment.numberOfOpenParenthesis = 0;
				CalculatorOctalFragment.numberOfOpenParenthesis = 0;

				CalculatorDecimalFragment.numberOfClosedParenthesis = 0;
				CalculatorBinaryFragment.numberOfClosedParenthesis = 0;
				CalculatorHexFragment.numberOfClosedParenthesis = 0;
				CalculatorOctalFragment.numberOfClosedParenthesis = 0;

				CalculatorDecimalFragment.numberOfOperators = 0;
				CalculatorBinaryFragment.numberOfOperators = 0;
				CalculatorHexFragment.numberOfOperators = 0;
				CalculatorOctalFragment.numberOfOperators = 0;

				scrollView.post(new Runnable() {

					@Override
					public void run() {
						scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					}
				});
			}

		});

		return v;
	}

	public static Fragment newInstance(int positionInViewPager, int radix) {
		CalculatorBinaryFragment binFrag = new CalculatorBinaryFragment();
		Bundle arg = new Bundle();
		arg.putInt(KEY_VIEW_NUMBER, positionInViewPager);
		arg.putInt(KEY_RADIX, radix);
		binFrag.setArguments(arg);
		return binFrag;
	}

	// method to save the state of the application during the activity life
	// cycle. This is so we can preserve the values in the textViews upon screen
	// rotation.
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Log.i(TAG, "onSaveInstanceState");
		// outState.putString(KEY_WORKINGTEXTVIEW_STRING, mSavedStateString);
		outState.putStringArrayList(KEY_WORKINGTEXTVIEW_STRING, mExpressions);
	}

	// need to make sure the fragment life cycle complies with the
	// actionBarSherlock support library
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
	private void onPassData(String dataToBePassed, boolean cameFromBackspace) {
		mCallback.onDataPassed(dataToBePassed, positionInPager, viewsRadix,
				cameFromBackspace);
	}

	// method to receive the data from the activity/other-fragments and update
	// the textViews accordingly
	public void updateWorkingTextView(String dataToBePassed, int base,
			boolean cameFromBackspace) {
		
		//scroll to the bottom
		scrollView.post(new Runnable() {

			@Override
			public void run() {
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});

		if (base == viewsRadix)
			return;

		if (dataToBePassed.length() != 0 || cameFromBackspace) {
			if (dataToBePassed.length() != 0) {

				StringTokenizer toke = new StringTokenizer(dataToBePassed,
						"x+-/)( \n\t", true);
				StringBuilder builder = new StringBuilder();

				while (toke.hasMoreElements()) {
					String aToken = (String) toke.nextElement().toString();
					if (aToken.equals("+") || aToken.equals("x")
							|| aToken.equals("-") || aToken.equals("/")
							|| aToken.equals("(") || aToken.equals(")")
							|| aToken.equals(" ") || aToken.equals("\n")
							|| aToken.equals("\t") || aToken.contains("N")
							|| aToken.contains("O")) {

						builder.append(aToken);

					}
					// if our token contains a "." in it then that means that we
					// need to do some conversion trickery
					else if (aToken.contains(".")) {
						if (aToken.endsWith(".")) {
							// don't do any conversions when the number is still
							// being
							// inputed and in the current state of something
							// like
							// this
							// "5."
							return;
						}
						// split the string around the "." delimiter.
						String[] parts = aToken.split("\\.");
						StringBuilder tempBuilder = new StringBuilder();

						if (aToken.charAt(0) == '.') {

						} else {

							// add the portion of the number to the left of the
							// "."
							// to our string this doesn't need any conversion
							// nonsense.
							tempBuilder.append(Long.toBinaryString(Long
									.parseLong(parts[0], base)));
						}
						// convert the fraction portion
						String getRidOfZeroBeforePoint = null;

						if (base == 10) {
							String fractionWithRadixPoint = "." + parts[1];
							String converted = Fractions
									.convertFractionPortionFromDecimal(
											fractionWithRadixPoint, viewsRadix);
							parts = converted.split("\\.");
							tempBuilder.append(".").append(parts[0]);
						} else {

							getRidOfZeroBeforePoint = Fractions
									.convertFractionPortionToDecimal(parts[1],
											base);

							// the conversion returns just the fraction
							// portion
							// with
							// a "0" to the left of the ".", so let's get
							// rid of
							// that extra zero.
							getRidOfZeroBeforePoint = getRidOfZeroBeforePoint
									.substring(1,
											getRidOfZeroBeforePoint.length());
							String partsAgain[] = getRidOfZeroBeforePoint
									.split("\\.");

							String converted = Fractions
									.convertFractionPortionFromDecimal(
											getRidOfZeroBeforePoint, viewsRadix);

							partsAgain = converted.split("\\.");
							tempBuilder.append(".").append(partsAgain[0]);
						}

						// add that to the string that gets put on the textView
						// (this may be excessive) (I wrote this late at night
						// so stuff probably got a little weird)
						builder.append(tempBuilder.toString());

					} else {
						BigInteger sizeTestBigInt = new BigInteger(aToken, base);
						if (sizeTestBigInt.bitLength() < 64) {
							String tokenInCorrectBase = Long
									.toBinaryString(Long
											.parseLong(aToken, base));
							builder.append(tokenInCorrectBase);
						}
					}
					mCurrentWorkingText = builder;
				}
			} else {
				mCurrentWorkingText = new StringBuilder("");
			}
			mExpressions.updateExpressions(mCurrentWorkingText.toString());
			if (mCurrentWorkingText.toString().contains("\n")) {
				mCurrentWorkingText = new StringBuilder("");
			}
			mWorkingTextView.setText(mExpressions.printAllExpressions());
		} else {
			mExpressions.clearAllExpressions();
			mCurrentWorkingText = new StringBuilder("");
			mWorkingTextView.setText(mCurrentWorkingText);
		}
	}

	// method to tell us if a string is a number or not
	public static boolean isOperand(String s) {
		double a = 0;
		try {
			a = Integer.parseInt(s, viewsRadix);
		} catch (Exception ignore) {
			return false;
		}
		return true;
	}

}
