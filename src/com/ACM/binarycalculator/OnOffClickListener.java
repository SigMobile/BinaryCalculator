package com.ACM.binarycalculator;

import android.view.View;
import android.view.View.OnClickListener;

//I found this code online to restrict the speed that used can click a button.
//When the user clicks a lot of buttons relatively quickly they start changing to 
//the textViews content to completely wrong numbers. 
public abstract class OnOffClickListener implements OnClickListener {

	private boolean clickable = true;

	/**
	 * Override onOneClick() instead.
	 */
	@Override
	public final void onClick(View v) {
		if (clickable) {
			clickable = false;
			onOneClick(v);
			// reset(); // uncomment this line to reset automatically
		}
	}

	/**
	 * Override this function to handle clicks. reset() must be called after
	 * each click for this function to be called again.
	 * 
	 * @param v
	 */
	public abstract void onOneClick(View v);

	/**
	 * Allows another click.
	 */
	public void reset() {
		clickable = true;
	}
}
