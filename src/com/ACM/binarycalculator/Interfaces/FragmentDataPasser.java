package com.ACM.binarycalculator.Interfaces;

/*
 * This is an interface that the activity will implement. 
 * The Fragments will use this interface to call back to the 
 * activity and send data to it so each and every fragment can
 * then be updated with the fragments data.
 * 
 * Fragments will make a callback variable like this:
 * 			FragmentDataPasser mCallbacks;
 * and then update the onDataPassed() method like this:
 * 			mCallbacks.onDataPassed(stringYouWantToPass);
 */
/**
 * 
 * @author James Van Gaasbeck <jjvg@knights.ucf.edu>
 * 
 * 
 */
public interface FragmentDataPasser {

	// Make a public method in the fragments that make a call to this method
	// passing the desired data along.
	//
	// The activity will implement this method receiving the data and then
	// passing it along to the fragments.
	public void onDataPassed(String dataToBePassed,
			int fragmentNumberInPagerAdapter, int numbersBase,
			boolean cameFromBackspace);

}
