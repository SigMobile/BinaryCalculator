package com.ACM.binarycalculator;

import java.util.ArrayList;

public class ExpressionHouse extends ArrayList<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1882699854378946219L;
	ArrayList<String> listOfAllExpressions;

	public ExpressionHouse() {
		listOfAllExpressions = new ArrayList<String>();
	}

	public void updateExpressions(String expressionToBeAdded) {

		// if the expression contains a new line then that means it was an
		// answer so we need to insert it into a new index in the arrayList
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

	public void clearAllExpressions() {
		// create a new blank arrayList if the user hits clearAll
		this.listOfAllExpressions = new ArrayList<String>();
	}

	public String getCurrentExpression() {
		return this.listOfAllExpressions
				.get(this.listOfAllExpressions.size() - 1);
	}

	// public void removeLastCharacter(String expressionToBeUpdated) {
	// // instead of actually removing the expression from the list lets just
	// // update the last index with the currentExpression after the last char
	// // was deleted in the front end
	// this.listOfAllExpressions.set(this.listOfAllExpressions.size() - 1,
	// expressionToBeUpdated);
	// }

	public String printAllExpressions() {
		StringBuilder retVal = new StringBuilder();

		for (String s : this.listOfAllExpressions)
			retVal.append(s);

		return retVal.toString();
	}
}
