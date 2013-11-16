package com.ACM.binarycalculator;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.ACM.binarycalculator.CalculatorBinaryFragment.FragmentDataPasser;

public class CalculatorPagerActivity extends FragmentActivity implements
		FragmentDataPasser {
	private static final String TAG = "CalculatorPagerActivity";

	private static final String KEY_FRAG_ARGS = "dataToBePassed";
	private ViewPager mViewPager;
	private static final int NUMBER_OF_VIEWS = 2;
	public String fragmentArgumentsValue = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// get rid of the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// set the content view to our blank ViewPager layout
		setContentView(R.layout.activity_main);

		// activityDataPasser = (ActivityDatapasser) getApplicationContext();

		// set the ID of the viewPager because it needs a reference ID
		mViewPager = (ViewPager) findViewById(R.id.viewPager);

		// sets the margin to be a little wider and black so there is a
		// distinction between each individual view when page turning
		mViewPager.setPageMargin(30);
		mViewPager.setBackgroundColor(getApplication().getResources().getColor(
				R.color.Black));

		// get the supported fragment manager
		FragmentManager fm = getSupportFragmentManager();
		// set up the viewPager adapter, this is the dataSource/list of the
		// views that will be inflated when turning the page.
		mViewPager.setAdapter(new FragmentPagerAdapter(fm) {

			// these are the only two methods that are necessary to have a
			// working ViewPager.
			@Override
			public int getCount() {
				// return the number of views we have put in our list. If
				// adding/removing views need to update the integer-constant at
				// the top of this activity
				return NUMBER_OF_VIEWS;
			}

			@Override
			public Fragment getItem(int position) {
				switch (position) {
				case 0:
					Log.d(TAG, "---In getPosition(), position 0---");
					// if the position is 0 return the binary view

					// the newInstance() method is a work around to let up pass
					// arguments into each fragment. It is pretty much a
					// homemade constructor that calls the fragments constructor
					// and allows up to pass in data to the fragments.
					CalculatorBinaryFragment binFrag = (CalculatorBinaryFragment) CalculatorBinaryFragment
							.newInstance(fragmentArgumentsValue);
					return binFrag;
				case 1:
					Log.d(TAG, "---In getPosition(), posistion 1---");

					return CalculatorDecimalFragment
							.newInstance(fragmentArgumentsValue);

				default:
					Log.d(TAG, "---In getPosition(), DEFAULT---");

					return CalculatorBinaryFragment
							.newInstance(fragmentArgumentsValue);
				}

			}
		});

		//
		// this is a callback for the ViewPager
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:

					// activityDataPasser.dataFromActivity(fragmentArgumentsValue);
					// makes a Toast and shows it, but for only three-quarters
					// of
					// a second because the standard Toast.LENGTH_SHORT is too
					// long (2seconds)
					final Toast toastBin = Toast.makeText(
							getApplicationContext(), "Binary",
							Toast.LENGTH_SHORT);
					toastBin.show();

					Handler handBin = new Handler();
					handBin.postDelayed(new Runnable() {

						@Override
						public void run() {
							toastBin.cancel();

						}
					}, 750); // show for only 750milliseconds
					break;

				case 1:
					// activityDataPasser.dataFromActivity(fragmentArgumentsValue);

					final Toast toastDec = Toast.makeText(
							getApplicationContext(), "Decimal",
							Toast.LENGTH_SHORT);
					toastDec.show();

					Handler handDec = new Handler();
					handDec.postDelayed(new Runnable() {

						@Override
						public void run() {
							toastDec.cancel();

						}
					}, 750);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// nothing
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// nothing
			}
		});
	}

	//
	// The code below this is a work in progress, so are some of the variables
	// declared at the top of the class.
	//

	@Override
	public void onDataPassed(String dataToBePassed, int fragmentNumberInAdapter) {

		switch (fragmentNumberInAdapter) {
		case 0:
			CalculatorDecimalFragment decFrag = (CalculatorDecimalFragment) getSupportFragmentManager()
			.findFragmentByTag("android:switcher:2131296257:1");

			if (decFrag != null) {
				decFrag.updateWorkingTextView(dataToBePassed);
			}
			break;
		case 1:
			CalculatorBinaryFragment binaryFrag = (CalculatorBinaryFragment) getSupportFragmentManager()
			.findFragmentByTag("android:switcher:2131296257:0");
			if (binaryFrag != null) {
				binaryFrag.updateWorkingTextView(dataToBePassed);
			}
			break;

		default:
			break;
		}

	}

}
