package com.ACM.binarycalculator.Fragments;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ACM.binarycalculator.R;
import com.ACM.binarycalculator.Activities.CalculatorPagerActivity;
import com.ACM.binarycalculator.Interfaces.FragmentDataPasser;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * 
 * @author James Van Gaasbeck, ACM at UCF <jjvg@knights.ucf.edu>
 * @author Michael Pittman, ACM at UCF <michael.pittman.ucf@gmail.com>
 * 
 * 
 */
public class CalculatorFloatingPointFragment extends SherlockFragment {
	// this is a tag used for debugging purposes
	 private static final String TAG = "rickJ";
	// string constant for saving our workingTextViewText
	private static final String KEY_WORKINGTEXTVIEW_STRING = "workingTextString";
	// the views number in the view pagers, pager adapter
	private static final int VIEW_NUMBER = 1;
	// the radix number (base-number) to be used when parsing the string.
	private static final int VIEWS_RADIX = 10;

	// these are our member variables
	TextView mComputeTextView;
	TextView mWorkingTextView;
	FragmentDataPasser mCallback;
	String mCurrentWorkingText;
	String savedComputeTextView = "";

	// we need to inflate our View so let's grab all the View IDs and inflate
	// them.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to make a view instance from our layout.
		View v = inflater.inflate(R.layout.fragment_calculator_floatingpoint,
				container, false);

		// get the textViews by id, notice we have to reference them via the
		// view instance we just created.
		mComputeTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_floatingpoint_computedTextView);
	//	mComputeTextView.setOnEditorActionListener(l));
		mWorkingTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_floatingpoint_workingTextView);
		
		mWorkingTextView.setText("0 00000000 00000000000000000000000");
		mComputeTextView.setTextSize(20);
		mComputeTextView.setText("");

		// if the we saved something away to handle the activity life cycle,
		// grab it!
		if (savedInstanceState != null) {
			mCurrentWorkingText = "0 00000000 000000000000000000000000";
			// set the text to be what we saved away and just now retrieved.
			mWorkingTextView.setText(mCurrentWorkingText);
		}

		final TableLayout tableLayout = (TableLayout) v
				.findViewById(R.id.fragment_calculator_floatingpoint_tableLayout2);
		
	
		
		mComputeTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				String number = mComputeTextView.getText().toString();
				
				if(number.trim() == null || number.trim().length() <= 0 || number.trim().equals("")){
					mWorkingTextView.setText("0 00000000 00000000000000000000000");
					setButtonBits("0 00000000 00000000000000000000000", tableLayout);
					return false;
				}
				
				if(number.contains(" - ") || number.contains(" + ") || number.contains(" / ") 
						|| number.contains(" x ")){
					String[] tokens = number.split(" ");
				//	BigDecimal num1 = new BigDecimal(tokens[0]);
					BigDecimal num2 = new BigDecimal(tokens[2]);
					
					mWorkingTextView.setText(decimalToFloatingPoint(num2));
					setButtonBits(decimalToFloatingPoint(num2), tableLayout);
				}
				else {
				
					BigDecimal num = new BigDecimal(number);
					mWorkingTextView.setText(decimalToFloatingPoint(num));
					setButtonBits(decimalToFloatingPoint(num), tableLayout);
				}
				
				return false;
			}
		});
		
		View.OnClickListener genericOperatorListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Button operator = (Button) v;
				String op = operator.getText().toString();
				String temp = mComputeTextView.getText().toString();
				
				if(op == "x" && temp.length() > 0 && (temp.contains(" x ") || temp.contains(" / ")
						|| temp.contains(" + ") || temp.contains(" - ")) == false){
					mComputeTextView.setText(temp + " x ");
					mWorkingTextView.setText("0 00000000 00000000000000000000000");
					resetBitsToZero(tableLayout);
					savedComputeTextView = mComputeTextView.getText().toString();
				}
				
				else if(op == "/" && temp.length() > 0 &&(temp.contains(" x ") || temp.contains(" / ")
						|| temp.contains(" + ") || temp.contains(" - ")) == false){
					mComputeTextView.setText(temp + " / ");
					mWorkingTextView.setText("0 00000000 00000000000000000000000");
					resetBitsToZero(tableLayout);
					savedComputeTextView = mComputeTextView.getText().toString();
				}
				
				else if(op == "+" && temp.length() > 0 && (temp.contains(" x ") || temp.contains(" / ")
						|| temp.contains(" + ") || temp.contains(" - ")) == false){
					mComputeTextView.setText(temp + " + ");
					mWorkingTextView.setText("0 00000000 00000000000000000000000");
					resetBitsToZero(tableLayout);
					savedComputeTextView = mComputeTextView.getText().toString();
				}
				
				else if(op == "-" && temp.length() > 0 && (temp.contains(" x ") || temp.contains(" / ")
						|| temp.contains(" + ") || temp.contains(" - ")) == false){
					mComputeTextView.setText(temp + " - ");
					mWorkingTextView.setText("0 00000000 00000000000000000000000");
					resetBitsToZero(tableLayout);
					savedComputeTextView = mComputeTextView.getText().toString();
				}
							
			}
		};
		
		View.OnClickListener allClearListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mWorkingTextView.setText("0 00000000 00000000000000000000000");
				resetBitsToZero(tableLayout);
				
				savedComputeTextView = "";
				mComputeTextView.setText("");

				
			}
		};
		
		View.OnClickListener bitListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				LinearLayout button = (LinearLayout) v;
				TextView textViewFromButton1 = (TextView) button.findViewById(R.id.firstTextView);
				TextView textViewFromButton2 = (TextView) button.findViewById(R.id.secondTextView);
				String bitNumber = textViewFromButton2.getText().toString();
				int number = Integer.parseInt(bitNumber);
				
				String textFromButton = textViewFromButton1.getText().toString();
				if(textFromButton.compareTo("0") == 0){
					textViewFromButton1.setText("1");
					mCurrentWorkingText = mWorkingTextView.getText().toString();
					
					if(number == 0){
						mWorkingTextView.setText(mCurrentWorkingText.substring(0, 33) + "1");
					}
					else if(number > 0 && number <= 22){
						String newNumber = mCurrentWorkingText.substring(0, 33-number) + "1" + mCurrentWorkingText.substring((33-number)+1,33) 
								+ mCurrentWorkingText.charAt(33);
						mWorkingTextView.setText(newNumber);
					}
					else if(number >= 23 && number <= 30){
						String newNumber = mCurrentWorkingText.substring(0, (33-number)-1) + "1" + mCurrentWorkingText.substring((33-number), 33)
								+ mCurrentWorkingText.charAt(33);
						mWorkingTextView.setText(newNumber);
					}
					else{
						String newNumber = "1" + mCurrentWorkingText.substring(1, 33) + mCurrentWorkingText.charAt(33);
						mWorkingTextView.setText(newNumber);
					}
					
					mCurrentWorkingText = mWorkingTextView.getText().toString();
					String temp = mWorkingTextView.getText().toString();
					//mComputeTextView.setText("");
					mComputeTextView.setText(savedComputeTextView + Double.toString(floatingPointToDecimal(temp)));
				}
				else{
					textViewFromButton1.setText("0"); 

					if(number == 0){
						mWorkingTextView.setText(mCurrentWorkingText.substring(0, 33) + "0");
					}
					
					else if(number >= 0 && number <= 22){
						String newNumber = mCurrentWorkingText.substring(0, 33-number) + "0" + mCurrentWorkingText.substring((33-number)+1,33)
								+ mCurrentWorkingText.charAt(33);
						mWorkingTextView.setText(newNumber);
					}
					else if(number >= 23 && number <= 30){
						String newNumber = mCurrentWorkingText.substring(0, (33-number)-1) + "0" + mCurrentWorkingText.substring((33-number), 33)
								+ mCurrentWorkingText.charAt(33);
						mWorkingTextView.setText(newNumber);
					}
					else{
						String newNumber = "0" + mCurrentWorkingText.substring(1, 33) + mCurrentWorkingText.charAt(33);
						mWorkingTextView.setText(newNumber);
					}
					
					mCurrentWorkingText = mWorkingTextView.getText().toString();
					String temp = mWorkingTextView.getText().toString();
					mComputeTextView.setText(savedComputeTextView + Double.toString(floatingPointToDecimal(temp)));
					
				}
				
				
					
			}
		};
 
		View.OnClickListener equalsButtonListener = new View.OnClickListener(){
			
			@Override
			public void onClick(View v){
				Log.d(TAG, "equal button hit");
				
				savedComputeTextView = "";
				String expression = mComputeTextView.getText().toString();
				BigDecimal answer = new BigDecimal(0);
	
				if(expression.contains(" - ") == false && expression.contains("+") == false && expression.contains("/") == false
						&& expression.contains("x") ==  false){
					return;
				}
				
				if(expression.endsWith("- ") || expression.endsWith("+ ") || expression.endsWith("/ ") || expression.endsWith("x ")
						== true)
					return;
				
				
				String[] tokens;
				
				if(expression.contains(" - ") == false){
					tokens = expression.split("[+x/]");
					tokens[0] = tokens[0].substring(0, tokens[0].length()-1);
					tokens[1] = tokens[1].substring(1, tokens[1].length()-1) + tokens[1].substring(tokens[1].length()-1);
				}
				else{
					tokens = expression.split(" - ");	
				}
				
				 
				
				if(expression.contains("x")){
					Log.d(TAG, "multiplication recognized");
					BigDecimal num1 = new BigDecimal(tokens[0]);
					BigDecimal num2 = new BigDecimal(tokens[1]);
					
					answer = num1.multiply(num2);
					Log.d(TAG, "multiplication answer calculated");
				}
				
				else if(expression.contains("/")){
					Log.d(TAG, "division recognized");
					BigDecimal num1 = new BigDecimal(tokens[0]);
					BigDecimal num2 = new BigDecimal(tokens[1]);
					
					answer = num1.divide(num2, 10, RoundingMode.HALF_UP);
					
					Log.d(TAG, "division answer calculated");				
				}
				
				else if(expression.contains("+")){
					Log.d(TAG, "addition recognized");
					BigDecimal num1 = new BigDecimal(tokens[0]);
					BigDecimal num2 = new BigDecimal(tokens[1]);
					
					answer = num1.add(num2);
					Log.d(TAG, "addition answer calculated");
				}
				
				else if (expression.contains(" - ")) {
					Log.d(TAG, "subtraction recognized");
					BigDecimal num1 = new BigDecimal(tokens[0]);
					BigDecimal num2 = new BigDecimal(tokens[1]);
					
					answer = num1.subtract(num2);
					Log.d(TAG, "subtraction answer calculated");	
				} 
				
				else
					return;
					
				
				
				mComputeTextView.setText(Double.toString(answer.doubleValue()));
				setButtonBits(decimalToFloatingPoint(answer), tableLayout);
				mWorkingTextView.setText(decimalToFloatingPoint(answer));
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
	//			onPassData(mCurrentWorkingText, true);
			}
		};

		// get a reference to our TableLayout XML
	//	TableLayout tableLayout = (TableLayout) v
	//			.findViewById(R.id.fragment_calculator_floatingpoint_tableLayout2);

		// adds the values and listeners to the buttons and pretty much every
		// button except for a few
		//
		// this for loop could probably be cleaned up, because the views had
		// changed from the original and the for loop had to change as well,
		// making the for loop look like a logical mess.
		int i, j;
		for (i = 0; i < tableLayout.getChildCount(); i++) {
			TableRow row = (TableRow) tableLayout.getChildAt(i);
			for (j = 0; j < row.getChildCount(); j++) {
				LinearLayout button = (LinearLayout) row.getChildAt(j);
				TextView bottomTextView = (TextView) button
						.findViewById(R.id.secondTextView);
				if (i == 0) {
					int num = 31 - j;
					bottomTextView.setText("" + num);
					button.setOnClickListener(bitListener);
				} else {
					int num = 15 - j;
					bottomTextView.setText("" + num);
					button.setOnClickListener(bitListener);
				}

			}
		}

		TableLayout tableLayout2 = (TableLayout) v
				.findViewById(R.id.fragment_calculator_floatingpoint_tableLayout1);
		TableRow row = (TableRow) tableLayout2.getChildAt(0);
		for (i = 0; i < row.getChildCount(); i++) {

			Button butt = (Button) row.getChildAt(i);

			if (i == 0) {
				butt.setText("BACK");
				butt.setTextSize(20);
				butt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent myIntent = new Intent(v.getContext(), CalculatorPagerActivity.class);
						startActivity(myIntent);
						return;
					}
				});
			} else if (i == 1) {
				//butt.setText("=");
				//butt.setOnClickListener(equalsButtonListener);
			} else if (i == 2) {
				butt.setText("");
				//butt.setOnClickListener(genericOperatorListener);
			} else if (i == 3) {
				butt.setText("");
				//butt.setOnClickListener(genericOperatorListener);
			} else if (i == 4) {
				//butt.setText("+");
				//butt.setOnClickListener(genericOperatorListener);
			} else if (i == 5) {
				//butt.setText("-");
				//butt.setOnClickListener(genericOperatorListener);
			} else if (i == 6) {
				//butt.setText("x");
				//butt.setOnClickListener(genericOperatorListener);
			} else if (i == 7) {
				//butt.setText("/");
				//butt.setOnClickListener(genericOperatorListener);
			} else {
				butt.setText("AC");
				butt.setOnClickListener(allClearListener);
			}

		}

		return v;
	}

	public static Fragment newInstance() {
		CalculatorFloatingPointFragment fpFrag = new CalculatorFloatingPointFragment();
		return fpFrag;
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
/*	public void onPassData(String dataToBePassed, boolean cameFromBackspace) {
		mCallback.onDataPassed(dataToBePassed, VIEW_NUMBER, VIEWS_RADIX,
				cameFromBackspace);
	}*/

	// method to receive the data from the activity/other-fragments and update
	// the textViews accordingly
/*	public void updateWorkingTextView(String dataToBePassed, int base) {
		if (dataToBePassed.length() != 0) {
			Long dataInt = Long.parseLong(dataToBePassed, base);
			mCurrentWorkingText = "" + dataInt;
			mWorkingTextView.setText(mCurrentWorkingText);
		} else {
			// if the data is blank set the textView to nothing
			mCurrentWorkingText = "";
			mWorkingTextView.setText(mCurrentWorkingText);
		}
	}*/
	
	public void resetBitsToZero(TableLayout tableLayout){
		int i, j;
		for(i=0; i<tableLayout.getChildCount(); i++){
			TableRow row = (TableRow) tableLayout.getChildAt(i);
			for(j=0; j<row.getChildCount(); j++){
				LinearLayout button = (LinearLayout) row.getChildAt(j);
				TextView topTextView = (TextView) button
						.findViewById(R.id.firstTextView);
				
				topTextView.setText("0");
			}
		}
	}
	
	public void setButtonBits(String floatingPoint, TableLayout tableLayout){
		Log.d(TAG, "set button bits: start");
		int i, j;
		String tokens[] = floatingPoint.split(" ");
		if(tokens[2].length() < 23)
			for(i=tokens[2].length(); i<23; i++){
				tokens[2] = tokens[2] + "0";
			}
		
		
		for(i=0; i<tableLayout.getChildCount(); i++){
			TableRow row = (TableRow) tableLayout.getChildAt(i);
			for(j=0; j<row.getChildCount(); j++){
				LinearLayout button = (LinearLayout) row.getChildAt(j);
				TextView topTextView = (TextView) button
						.findViewById(R.id.firstTextView);
				
				TextView bottomTextView = (TextView) button.findViewById(R.id.secondTextView);
				
				if(bottomTextView.getText().toString().equals("31")){
					topTextView.setText(tokens[0]);
				}
				else if(Integer.parseInt(bottomTextView.getText().toString()) <= 30 
							&& Integer.parseInt(bottomTextView.getText().toString()) >= 23){
					int index = 30 - Integer.parseInt(bottomTextView.getText().toString());
					char[] array = tokens[1].toCharArray();
					topTextView.setText(Character.toString(array[index]));
				}
				else if(Integer.parseInt(bottomTextView.getText().toString()) <= 22 
						&& Integer.parseInt(bottomTextView.getText().toString()) >= 0){
					int index = 22 - Integer.parseInt(bottomTextView.getText().toString());
					char[] array = tokens[2].toCharArray();
					topTextView.setText(Character.toString(array[index]));
				}
					
			}
		}
		Log.d(TAG, "set button bits: finish");
	}
	
	
	
	public double floatingPointToDecimal(String floatingPointNumber){
		String mantissaString = floatingPointNumber.substring(11, 33) + floatingPointNumber.charAt(33);
		Double mantissa = calculateFractionalBits(mantissaString);
		mantissa += 1;
		String exponentString = floatingPointNumber.substring(2, 10);
		int sign = Integer.parseInt(floatingPointNumber.substring(0, 1));
		int exponent = Integer.parseInt(exponentString, 2);
					
		exponent -= 127;
		
		double answer = (double) Math.pow(2, exponent) * mantissa;
		if(sign == 1)
			answer = answer * -1; 
		
		return answer;
	}
	
	public double calculateFractionalBits(String mantissa){
		char mArray[] = mantissa.toCharArray();
		int length = mantissa.length();
		int i;
		double sum = 0;
		for(i=0; i<length; i++){
			if(mArray[i] == '1')
				sum = sum + (1 / (Math.pow(2, i+1)));
		}
		
		return sum;
	}
	
	public String decimalToFloatingPoint(BigDecimal decimalNumber){
		Log.d(TAG, "decimal to floatingPoint: start");
		
		float decimalNumber1 = decimalNumber.floatValue();
		
		int bits = Float.floatToIntBits(decimalNumber1);
		String bitString = Integer.toBinaryString(bits);
		
		
		
		if(bitString.length() < 32){
			int i;
			for(i=bitString.length(); i<32; i++)
				bitString = "0" + bitString;
		}
		
		bitString = bitString.substring(0, 1) + " " + bitString.substring(1, 9) + " " + bitString.substring(9);
		
		Log.d(TAG, "decimalToFloatingPoint: finish");
		return bitString;
		
	}
	
}
	

