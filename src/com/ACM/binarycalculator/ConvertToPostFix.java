package com.ACM.binarycalculator;

import java.util.Stack;

public class ConvertToPostFix {

	private Stack<Double> stack;
	private Stack<String> opStack;
	private String postfixEpression;
	private String inFixExpression;
	private double finalValue;
	private static final String TAG = "ConertToPostFix";
	private boolean initialAdd = true;

	public ConvertToPostFix(String expression) {

		// initial empty stacks and empty postfix expression
		this.stack = new Stack<Double>();
		this.opStack = new Stack<String>();
		this.postfixEpression = "";
		this.inFixExpression = expression;
		readString();
		solve();
	}

	private void solve() {
		// TODO Auto-generated method stub
		String[] tokens = this.postfixEpression.split("\\s");

		int count = 0;
		while (count < tokens.length) {
			if (isOp(tokens[count]))
				doOp(tokens[count]);
			else
				this.stack.push(Double.parseDouble(tokens[count]));
			count++;
		}
		this.finalValue = this.stack.pop();

	}

	private void doOp(String string) {
		// TODO Auto-generated method stub
		double first, second;
		second = this.stack.pop();
		first = this.stack.pop();

		if (string.compareTo("+") == 0)
			this.stack.push(first + second);
		else if (string.compareTo("-") == 0)
			this.stack.push(first - second);
		else if (string.compareTo("x") == 0)
			this.stack.push(first * second);
		else if (string.compareTo("/") == 0)
			this.stack.push(first / second);

	}

	private boolean isOp(String string) {
		// TODO Auto-generated method stub
		if (string.compareTo("+") == 0 || string.compareTo("-") == 0
				|| string.compareTo("x") == 0 || string.compareTo("/") == 0)
			return true;
		return false;
	}

	/**
	 * reads the input infix expression and breaks it down to its pieces builds
	 * postfix expression in the process
	 */
	private void readString() {
		String temp = "";
		int i = 0;
		// beginning of loop that will iterate through entire expression

		/*
		 * this loop builds token based off ascii values 0 - 9 = ascii values 49
		 * - 57 + = ascii value 43 - = ascii value 45 * = ascii value 42 / =
		 * ascii value 47 ( = ascii value 40 ) = ascii value 41 . = ascii value
		 * 46
		 */
		while (i < this.inFixExpression.length()) {
			// builds numbers
			if (this.inFixExpression.charAt(i) >= 49
					&& this.inFixExpression.charAt(i) <= 57) {
				while ((this.inFixExpression.charAt(i) >= 49 && this.inFixExpression
						.charAt(i) <= 57)
						|| this.inFixExpression.charAt(i) == 46) {
					temp += this.inFixExpression.charAt(i);
					i++;
					if (i == this.inFixExpression.length())
						break;
				}

				addNumber(Double.parseDouble(temp));
				temp = ""; // resets temp for next iteration
			} else {
				switch (this.inFixExpression.charAt(i)) {
				case 43:
					addOp("+");
					break;
				case 45:
					addOp("-");
					break;
				case 120:
					addOp("x");
					break;
				case 47:
					addOp("/");
					break;
				case 40:
					addOp("(");
					break;
				case 41:
					addOp(")");
					break;
				}
				i++;
			}

		}
		while (!this.opStack.empty())
			this.postfixEpression += this.opStack.pop().toString() + " ";
	}

	/**
	 * adds a entered number to the postfix expression
	 * 
	 * @param i
	 *            the number to added
	 */
	private void addNumber(double i) {
		this.postfixEpression += i + " ";
	}

	/**
	 * adds a entered number to the postfix expression
	 * 
	 * @param i
	 *            the number to added
	 */
	public void addNumber(int i) {
		this.postfixEpression += i + " ";
	}

	/**
	 * Adds op to opStack and checks the precedence If opStack is empty then add
	 * op to stack. else if op is lower precedence than op on top of opStack pop
	 * stack, add to postfix and then add new op to opStack
	 * 
	 * @param i
	 *            the op to be added
	 */
	public void addOp(String i) {

		if (this.opStack.isEmpty() || i.compareTo("(") == 0)
			this.opStack.push(i);
		else if (i.compareTo(")") == 0) {
			// pop everything till (
			while (this.opStack.peek().compareTo("(") != 0) {
				this.postfixEpression += this.opStack.pop().toString() + " ";
			}
			this.opStack.pop(); // removes the (
		} else if (checkPrecedence(i, this.opStack.peek())) {

			// pops the op from stack and adds it to the end of postfix
			// string
			this.postfixEpression += this.opStack.pop().toString() + " ";

			while (shoulPopAgain(i))
				this.postfixEpression += this.opStack.pop().toString() + " ";

			this.opStack.push(i);

		} else
			this.opStack.push(i);
	}

	private boolean shoulPopAgain(String i) {
		// TODO Auto-generated method stub
		if (this.initialAdd || this.opStack.empty()) {
			this.initialAdd = false;
			return false;
		}

		return checkPrecedence(i, this.opStack.peek());
	}

	/**
	 * this method checks the precedence of the current op to be added. when i
	 * is of higher precedence then return false. else return false
	 * 
	 * @param i
	 *            the current op to be added
	 * @param peek
	 *            the op currently on top of the stack
	 * @return
	 */
	private boolean checkPrecedence(String i, String peek) {
		// TODO Auto-generated method stub

		// if i and peek are equal pop stack
		if (i.compareTo(peek) == 0)
			return true;

		// handles when i is a * or /
		if ((i.compareTo("x") == 0 || i.compareTo("/") == 0)
				&& (peek.compareTo("x") == 0 || peek.compareTo("/") == 0))
			return true;

		// handles when i is an + or -
		if ((i.compareTo("+") == 0 || i.compareTo("-") == 0)
				&& (peek.compareTo("x") == 0 || peek.compareTo("/") == 0
						|| peek.compareTo("+") == 0 || peek.compareTo("-") == 0))
			return true;

		return false;
	}
	public Double getFinalAnswer(){
		return this.finalValue;
	}
}
