package com.ACM.binarycalculator;

import java.util.Stack;

public class ConvertToPostFix {

	private Stack<Double> stack;
	private Stack<String> opStack;
	private String postfixEpression;
	private double finalValue;

	public ConvertToPostFix() {

		// initial empty stack and empty postfix expression
		this.stack = new Stack<Double>();
		this.postfixEpression = "";
	}

	/**
	 * adds a entered number to the postfix expression
	 * 
	 * @param i
	 *            the number to added
	 */
	public void addNumber(double i) {
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
		if (this.opStack.isEmpty())
			this.opStack.add(i);
		else if (i.compareTo(")") == 0) {
			// pop everything till (
			while (this.opStack.peek().compareTo("(") != 0) {
				this.postfixEpression += this.opStack.pop().toString() + " ";
			}
			this.opStack.pop(); // removes the (
		} else if (checkPrecedence(i, this.opStack.peek())) {
			// pops the op from stack and adds it to the end of postfix string
			this.postfixEpression += this.opStack.pop().toString() + " ";
			this.opStack.add(i);
		} else
			this.opStack.add(i);
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

		// if i is ) then pop everything till (
		if (i.compareTo(")") == 0)
			// pop till (
			return true;
		// if i and peek are equal pop stack
		if (i.compareTo(peek) == 0)
			return true;

		// handles when i is a * or /
		if ((i.compareTo("*") == 0 || i.compareTo("/") == 0)
				&& (peek.compareTo("*") == 0 || peek.compareTo("/") == 0
						|| peek.compareTo("+") == 0 || peek.compareTo("-") == 0))
			return true;

		// handles when i is an + or -
		if ((i.compareTo("+") == 0 || i.compareTo("-") == 0)
				&& (peek.compareTo("+") == 0 || peek.compareTo("-") == 0))
			return true;

		return false;
	}
}
