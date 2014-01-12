package com.ACM.binarycalculator.DataModels;

import java.util.ArrayList;

/**
 * 
 * @author James Van Gaasbeck, ACM at UCF <jjvg@knights.ucf.edu>
 * 
 *         This is our data model class. It is used to hold all the expressions.
 *         It extends type ArrayList so we can save the list easily upon screen
 *         rotation.
 * 
 */
public class ExpressionHouse extends ArrayList<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1882699854378946219L;
	ArrayList<String> listOfAllExpressions;

	/**
	 * Constructor
	 */
	public ExpressionHouse() {
		listOfAllExpressions = new ArrayList<String>();
	}

	/**
	 * 
	 * @param expressionToBeAdded
	 *            - The expression/answer that is to be added to the list. This
	 *            method is also called when the backspace button was hit.
	 */
	public void updateExpressions(String expressionToBeAdded) {

		// if the expression contains a new line then that means it was an
		// answer so we need to insert it into a new index in the arrayList
		// and then create a new index after the answer.
		if (expressionToBeAdded.contains("\n")) {
			this.listOfAllExpressions.add(expressionToBeAdded);
			// add a blank index to the end of the list to be overwritten when
			// the next
			// expression is inputed
			this.listOfAllExpressions.add("");
		} else {

			if (this.listOfAllExpressions.size() == 0) {
				this.listOfAllExpressions.add(0, expressionToBeAdded);
			} else {
				// otherwise overwrite the last index of the list because the
				// current expression is being updated
				this.listOfAllExpressions.set(
						this.listOfAllExpressions.size() - 1,
						expressionToBeAdded);
			}
		}
	}

	/**
	 * Method to clear all the expressions.
	 */
	public void clearAllExpressions() {
		// create a new blank arrayList if the user hits clearAll
		this.listOfAllExpressions = new ArrayList<String>();
	}

	/**
	 * 
	 * @return - Returns the current/last expression added. If the last
	 *         expression was an answer, it will return a new blank
	 *         expression/index.
	 */
	public String getCurrentExpression() {
		// if size is zero then just return a blank expression.
		if (this.listOfAllExpressions.size() == 0)
			return "";
		else
			return this.listOfAllExpressions.get(this.listOfAllExpressions
					.size() - 1);
	}

	/**
	 * 
	 * @return - Returns the current/last answer added. Returns a blank string
	 *         if there has not been an answer yet.
	 */
	public String getMostRecentAnswer() {
		if (this.listOfAllExpressions.size() <= 2)
			return "";
		else
			return this.listOfAllExpressions.get(this.listOfAllExpressions
					.size() - 2);
	}

	/**
	 * 
	 * @return - Returns a string of all the elements in the list. This method
	 *         is used for filling the TextViews.
	 */
	public String printAllExpressions() {
		StringBuilder retVal = new StringBuilder();

		for (String s : this.listOfAllExpressions)
			retVal.append(s);

		return retVal.toString();
	}
}
