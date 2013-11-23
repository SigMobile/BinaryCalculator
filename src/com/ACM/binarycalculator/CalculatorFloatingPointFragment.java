package com.ACM.binarycalculator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 
 * @author James Van Gaasbeck, ACM at UCF <jjvg@knights.ucf.edu>
 * 
 * 
 */
public class CalculatorFloatingPointFragment extends Fragment  {
	// this is a tag used for debugging purposes
	private static final String TAG = "CalculatorFloatingPointFragment";
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
	static String mCurrentWorkingText;

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
		mWorkingTextView = (TextView) v
				.findViewById(R.id.fragment_calculator_floatingpoint_workingTextView);

		// if the we saved something away to handle the activity life cycle,
		// grab it!
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

		// get a reference to our TableLayout XML
		TableLayout tableLayout = (TableLayout) v
				.findViewById(R.id.fragment_calculator_floatingpoint_tableLayout2);

		// adds the values and listeners to the buttons and pretty much every
		// button except for a few
		//
		// this for loop could probably be cleaned up, because the views had
		// changed from the original and the for loop had to change as well,
		// making the for loop look like a logical mess.
		int i, j;
		 for(i=0; i<tableLayout.getChildCount(); i++){
			 TableRow row = (TableRow) tableLayout.getChildAt(i);
			 for(j=0; j<row.getChildCount(); j++){
				 LinearLayout button = (LinearLayout) row.getChildAt(j);
				 TextView bottomTextView = (TextView) button.findViewById(R.id.secondTextView);
				 if(i == 0){
					 int num = 31-j;
					 bottomTextView.setText(""+num);
				 }
				 else{
					 int num = 15-j;
					 bottomTextView.setText(""+num);
				 }
				 
			 }	 
		 }
		 
		 TableLayout tableLayout2 = (TableLayout) v.findViewById(R.id.fragment_calculator_floatingpoint_tableLayout1);
		 TableRow row = (TableRow) tableLayout2.getChildAt(0);
		 for(i=0; i<row.getChildCount(); i++){
			 
			 Button butt = (Button) row.getChildAt(i);
			 
			 if(i == 0){
				 butt.setText("<<");
			 }
			 else if(i == 1){
				 butt.setText(">>");
			 }
			 else if(i == 2){
				 butt.setText("2's");
			 }
			 else if(i == 3){
				 butt.setText("1's");
			 }
			 else if(i == 4){
				 butt.setText("+");
			 }
			 else if(i == 5){
				 butt.setText("-");
			 }
			 else if(i == 6){
				 butt.setText("x");
			 }
			 else if(i == 7){
				 butt.setText("/");
			 }
			 else{
				 butt.setText("=");
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
		Log.i(TAG, "onSaveInstanceState");
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
			Long dataInt = Long.parseLong(dataToBePassed, base);
			mCurrentWorkingText = "" + dataInt;
			mWorkingTextView.setText(mCurrentWorkingText);
		} else {
			//if the data is blank set the textView to nothing
			mCurrentWorkingText = "";
			mWorkingTextView.setText(mCurrentWorkingText);
		}
	}
	
	

}

