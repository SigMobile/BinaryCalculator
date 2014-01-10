package com.ACM.binarycalculator;

import android.os.Bundle;
import android.view.Window;

import com.actionbarsherlock.app.SherlockFragment;

public class CalculatorFloatingPointActivity extends SingleFragmentActivity
		implements FragmentDataPasser {

	@Override
	protected SherlockFragment createFragment() {
		// return a new blank fragment to be worked with.
		return new CalculatorFloatingPointFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// make it so there is NO title bar, we have to make this call in the
		// activity rather than in the fragment, and must be called before the
		// call to the super()
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDataPassed(String dataToBePassed,
			int fragmentNumberInPagerAdapter, int numbersBase, boolean cameFromBackspace) {
		// TODO Auto-generated method stub

	}

}
