package com.ACM.binarycalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Window;

public class CalculatorPagerActivity extends FragmentActivity {
	private static final String TAG = "CalculatorPagerActivity";

	private ViewPager mViewPager;
	private static final int NUMBER_OF_VIEWS = 2;
	public static String fragmentArgumentsValue = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "---In onCreate()---");
		super.onCreate(savedInstanceState);
		// get rid of the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// set the content view to our blank ViewPager layout
		setContentView(R.layout.activity_main);

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
				// the
				// top of this activity
				Log.d(TAG, "---In getCount()---");
				return NUMBER_OF_VIEWS;
			}

			@Override
			public Fragment getItem(int position) {
				Log.d(TAG, "---In getItem()---");
				switch (position) {
				case 0:
					Log.d(TAG, "---In getPosition(), position 0---");
					// if the position is 0 return the binary view

					// the newInstance() method is a work around to let up pass
					// arguments into each fragment. It is pretty much a
					// homemade constructor that calls the fragments constructor
					// and allows up to pass in data to the fragments.
					return CalculatorBinaryFragment
							.newInstance(fragmentArgumentsValue);

				case 1:
					Log.d(TAG, "---In getPosition(), posistion 1---");

					return CalculatorDecimalFragment
							.newInstance(fragmentArgumentsValue);

				default:
					Log.d(TAG, "---In getPosition(), DEFAULT---");

					return CalculatorDecimalFragment
							.newInstance(fragmentArgumentsValue);
				}

			}
		});

		// this is a callback for the ViewPager, this will allow us to change
		// the textView values upon page turning.
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
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
}
