package com.ACM.binarycalculator.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Toast;

import com.ACM.binarycalculator.R;
import com.ACM.binarycalculator.Fragments.CalculatorBinaryFragment;
import com.ACM.binarycalculator.Fragments.CalculatorDecimalFragment;
import com.ACM.binarycalculator.Fragments.CalculatorHexFragment;
import com.ACM.binarycalculator.Fragments.CalculatorOctalFragment;
import com.ACM.binarycalculator.Interfaces.FragmentDataPasser;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * 
 * @author James Van Gaasbeck, ACM at UCF <jjvg@knights.ucf.edu>
 * 
 * 
 */
public class CalculatorPagerActivity extends SherlockFragmentActivity implements
		FragmentDataPasser {
	// private static final String TAG = "CalculatorPagerActivity";

	private ViewPager mViewPager;
	private static final int NUMBER_OF_VIEWS = 4;
	// array of the names of the view, putting them in an array makes it easier
	// when setting. If ever adding/switching views around then we need to
	// update this array.
	private final String[] viewNames = { "Binary", "Hex", "Octal", "Decimal" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// we need to get a reference to our support action bar
		final com.actionbarsherlock.app.ActionBar supportActionBar = getSupportActionBar();
		super.onCreate(savedInstanceState);

		// set the content view to our blank ViewPager layout
		setContentView(R.layout.activity_main);

		// set the ID of the viewPager because it needs a reference ID
		mViewPager = (ViewPager) findViewById(R.id.viewPager);

		// sets the margin to be a little wider so there is a
		// distinction between each individual view when page turning
		mViewPager.setPageMargin(30);

		// transition animation makes it useless.
		mViewPager.setBackgroundColor(getApplication().getResources().getColor(
				R.color.Black));
		// actionBar.setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#EAEAAE")));

		// we need to set the number of pages that stay in memory so that all
		// the pages/fragments are updated rather than just the adjacent pages.
		mViewPager.setOffscreenPageLimit(NUMBER_OF_VIEWS - 1);

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
			public SherlockFragment getItem(int position) {
				switch (position) {
				case 0:
					// Log.d(TAG, "---In getPosition(), position 0---");
					// if the position is 0 return the binary view

					// the newInstance() method is a work around to let up pass
					// arguments into each fragment. It is pretty much a
					// homemade constructor that calls the fragments constructor
					// and allows up to pass in data to the fragments.
					CalculatorBinaryFragment binFrag = (CalculatorBinaryFragment) CalculatorBinaryFragment
							.newInstance(position, 2);
					return binFrag;
				case 1:
					// Log.d(TAG, "---In getPosition(), posistion 1---");

					return CalculatorHexFragment.newInstance(position, 16);

				case 2:
					// Log.d(TAG, "---In getPosition(), position 2---");
					return CalculatorOctalFragment.newInstance(position, 8);
				case 3:
					// Log.d(TAG, "---In getPosition(), position 3---");
					return CalculatorDecimalFragment.newInstance(position, 10);

				default:
					// Log.d(TAG, "---In getPosition(), DEFAULT---");

					return CalculatorBinaryFragment.newInstance(position, 2);
				}

			}
		});

		//
		// this is a callback for the ViewPager
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			/*
			 * If changing the views ordering remember to update the Toasts.
			 */
			@Override
			public void onPageSelected(int position) {

				// set the action bars tab to the correct page when the user
				// swipes the tab
				getSupportActionBar().setSelectedNavigationItem(position);
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

		/*
		 * Tabbed action bar sherlock code
		 */
		// set that the tabs will be in the action bar
		supportActionBar
				.setNavigationMode(com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_TABS);

		// we need a Tab listener
		com.actionbarsherlock.app.ActionBar.TabListener tabListener = new com.actionbarsherlock.app.ActionBar.TabListener() {

			@Override
			public void onTabSelected(
					com.actionbarsherlock.app.ActionBar.Tab tab,
					android.support.v4.app.FragmentTransaction ft) {
				// set the viewPager to the correct view
				mViewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(
					com.actionbarsherlock.app.ActionBar.Tab tab,
					android.support.v4.app.FragmentTransaction ft) {
				// Could probably do nothing here cause the logic is handled
				// when a tab is selected.
			}

			@Override
			public void onTabReselected(
					com.actionbarsherlock.app.ActionBar.Tab tab,
					android.support.v4.app.FragmentTransaction ft) {
				// Do nothing
			}
		};

		// add/set the text of the tabs
		for (int i = 0; i < NUMBER_OF_VIEWS; i++) {
			supportActionBar.addTab(supportActionBar.newTab()
					.setText(viewNames[i]).setTabListener(tabListener));
		}

	} // closes onCreate()

	// the callback that will receive info from the fragments and then update
	// all the fragments
	@Override
	public void onDataPassed(String dataToBePassed,
			int fragmentNumberInPagerAdapter, int numbersBase,
			boolean cameFromBackspace) {

		CalculatorBinaryFragment binaryFrag = null;
		CalculatorDecimalFragment decFrag = null;
		CalculatorOctalFragment octalFrag = null;
		CalculatorHexFragment hexFrag = null;

		switch (fragmentNumberInPagerAdapter) {
		case 0:
			numbersBase = 2;
			break;

		case 1:
			numbersBase = 16;
			break;

		case 2:
			numbersBase = 8;
			break;

		case 3:
			numbersBase = 10;
			break;
		default:
			break;
		}
		//
		// finding a fragment by tag is kind of tricky at first, because it's
		// not intuitively simple to set the tag, and the tag it is assigned by
		// default can seem pretty cryptic. But if you single step the
		// application, or google, you will
		// see that the tag that's assigned to the fragment follows these lines:
		// "android:switcher:theIdNumberOftheViewContainer:theNumberIntheAdapter".
		// the ID number of the container is the mViewPagerID number in our
		// case.
		//
		// Find each of the fragments by Tag and then call their public method
		// we made to update the workingTextView.

		binaryFrag = (CalculatorBinaryFragment) getSupportFragmentManager()
				.findFragmentByTag("android:switcher:" + R.id.viewPager + ":0");
		if (binaryFrag != null) {
			binaryFrag.updateWorkingTextView(dataToBePassed, numbersBase,
					cameFromBackspace);
		}

		hexFrag = (CalculatorHexFragment) getSupportFragmentManager()
				.findFragmentByTag("android:switcher:" + R.id.viewPager + ":1");
		if (hexFrag != null) {
			hexFrag.updateWorkingTextView(dataToBePassed, numbersBase,
					cameFromBackspace);
		}

		octalFrag = (CalculatorOctalFragment) getSupportFragmentManager()
				.findFragmentByTag("android:switcher:" + R.id.viewPager + ":2");
		if (octalFrag != null) {
			octalFrag.updateWorkingTextView(dataToBePassed, numbersBase,
					cameFromBackspace);
		}

		decFrag = (CalculatorDecimalFragment) getSupportFragmentManager()
				.findFragmentByTag("android:switcher:" + R.id.viewPager + ":3");
		if (decFrag != null) {
			decFrag.updateWorkingTextView(dataToBePassed, numbersBase,
					cameFromBackspace);
		}

		// switch (fragmentNumberInPagerAdapter) {
		// /*
		// * If changing the ordering of the views, this whole thing will need
		// to
		// * be reconfigured.
		// */
		// case 0:
		//
		// decFrag = (CalculatorDecimalFragment) getSupportFragmentManager()
		// .findFragmentByTag(
		// "android:switcher:" + R.id.viewPager + ":3");
		// if (decFrag != null) {
		// decFrag.updateWorkingTextView(dataToBePassed, numbersBase,
		// cameFromBackspace);
		// }
		//
		// octalFrag = (CalculatorOctalFragment) getSupportFragmentManager()
		// .findFragmentByTag(
		// "android:switcher:" + R.id.viewPager + ":2");
		// if (octalFrag != null) {
		// octalFrag.updateWorkingTextView(dataToBePassed, numbersBase,
		// cameFromBackspace);
		// }
		//
		// hexFrag = (CalculatorHexFragment) getSupportFragmentManager()
		// .findFragmentByTag(
		// "android:switcher:" + R.id.viewPager + ":1");
		// if (hexFrag != null) {
		// hexFrag.updateWorkingTextView(dataToBePassed, numbersBase,
		// cameFromBackspace);
		// }
		// break;
		//
		// case 1:
		// binaryFrag = (CalculatorBinaryFragment) getSupportFragmentManager()
		// .findFragmentByTag(
		// "android:switcher:" + R.id.viewPager + ":0");
		// if (binaryFrag != null) {
		// binaryFrag.updateWorkingTextView(dataToBePassed, numbersBase,
		// cameFromBackspace);
		// }
		//
		// octalFrag = (CalculatorOctalFragment) getSupportFragmentManager()
		// .findFragmentByTag(
		// "android:switcher:" + R.id.viewPager + ":2");
		// if (octalFrag != null) {
		// octalFrag.updateWorkingTextView(dataToBePassed, numbersBase,
		// cameFromBackspace);
		// }
		//
		// decFrag = (CalculatorDecimalFragment) getSupportFragmentManager()
		// .findFragmentByTag(
		// "android:switcher:" + R.id.viewPager + ":3");
		// if (decFrag != null) {
		// decFrag.updateWorkingTextView(dataToBePassed, numbersBase,
		// cameFromBackspace);
		// }
		// break;
		//
		// case 2:
		// binaryFrag = (CalculatorBinaryFragment) getSupportFragmentManager()
		// .findFragmentByTag(
		// "android:switcher:" + R.id.viewPager + ":0");
		// if (binaryFrag != null) {
		// binaryFrag.updateWorkingTextView(dataToBePassed, numbersBase,
		// cameFromBackspace);
		// }
		//
		// hexFrag = (CalculatorHexFragment) getSupportFragmentManager()
		// .findFragmentByTag(
		// "android:switcher:" + R.id.viewPager + ":1");
		// if (hexFrag != null) {
		// hexFrag.updateWorkingTextView(dataToBePassed, numbersBase,
		// cameFromBackspace);
		// }
		//
		// decFrag = (CalculatorDecimalFragment) getSupportFragmentManager()
		// .findFragmentByTag(
		// "android:switcher:" + R.id.viewPager + ":3");
		// if (decFrag != null) {
		// decFrag.updateWorkingTextView(dataToBePassed, numbersBase,
		// cameFromBackspace);
		// }
		// break;
		//
		// case 3:
		// binaryFrag = (CalculatorBinaryFragment) getSupportFragmentManager()
		// .findFragmentByTag(
		// "android:switcher:" + R.id.viewPager + ":0");
		// if (binaryFrag != null) {
		// binaryFrag.updateWorkingTextView(dataToBePassed, numbersBase,
		// cameFromBackspace);
		// }
		//
		// octalFrag = (CalculatorOctalFragment) getSupportFragmentManager()
		// .findFragmentByTag(
		// "android:switcher:" + R.id.viewPager + ":2");
		// if (octalFrag != null) {
		// octalFrag.updateWorkingTextView(dataToBePassed, numbersBase,
		// cameFromBackspace);
		// }
		//
		// hexFrag = (CalculatorHexFragment) getSupportFragmentManager()
		// .findFragmentByTag(
		// "android:switcher:" + R.id.viewPager + ":1");
		// if (hexFrag != null) {
		// hexFrag.updateWorkingTextView(dataToBePassed, numbersBase,
		// cameFromBackspace);
		// }
		//
		// break;
		//
		// default:
		// // shouldn't happen
		// break;
		// }

	}
}
