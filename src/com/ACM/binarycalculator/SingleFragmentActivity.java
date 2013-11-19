package com.ACM.binarycalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

//abstract class for creating a single fragment activity, contains boiler plate code so we've put it all in an
//abstract class to save us writing in the future when creating fragments
public abstract class SingleFragmentActivity extends FragmentActivity {
	// abstract method to return a new fragment
	protected abstract Fragment createFragment();

	// method to be overridden so we can choose which layout to inflate at
	// runtime.
	protected int getLayoutResId() {
		return R.layout.activity_fragment;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the content view to be the activity fragment
		setContentView(getLayoutResId());

		// set the fragment manager
		FragmentManager fm = getSupportFragmentManager();
		// set the fragment container
		Fragment frag = fm.findFragmentById(R.id.fragmentContainer);
//jorgeis the best ahhhahhaha
		
		// if the fragment is null createFragment()
		if (frag == null) {
			frag = createFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, frag).commit();
		}
	}

}
