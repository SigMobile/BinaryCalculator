package com.ACM.binarycalculator;

import java.util.Stack;
import java.util.StringTokenizer;

public class InfixToPostfix {

	public static String convertToPostfix(String infixExpression) {

		Stack<String> theStack = new Stack<String>();
		String postfix = new String("");
		String space = new String(" ");
		StringTokenizer toke = new StringTokenizer(infixExpression,
				"x+-/)( \n", true);

		while (toke.hasMoreElements()) {
			String currentToken = toke.nextElement().toString();

			if (!(currentToken.equals("+") || currentToken.equals("x")
					|| currentToken.equals("-") || currentToken.equals("/")
					|| currentToken.equals("\n") || currentToken.equals(space))) {

				postfix += currentToken + space;

			} else if (currentToken.equals("(")) {
				theStack.push(currentToken);
			} else if (currentToken.equals("+") || currentToken.equals("x")
					|| currentToken.equals("-") || currentToken.equals("/")
					|| currentToken.equals("\n")) {

				while (!theStack.isEmpty()
						&& operatorPrecedence(theStack.peek()) >= operatorPrecedence(currentToken)) {

					postfix += theStack.pop() + space;
				}
				theStack.push(currentToken);
			} else if (currentToken.equals(")")) {
				while (!theStack.peek().equals("(")) {
					postfix += theStack.pop() + space;
				}
				theStack.pop();
			} else if (currentToken.equals(space)) {
				// do nothing
			}
		} // closes while()

		while (!theStack.isEmpty()) {
			postfix += theStack.pop() + space;
		}

		return postfix;
	}

	private static int operatorPrecedence(String operator) {
		int precedence = 0;
		if (operator.equals("+")) {
			precedence = 1;
		} else if (operator.equals("-")) {
			precedence = 1;
		} else if (operator.equals("x")) {
			precedence = 2;
		} else if (operator.equals("/")) {
			precedence = 2;
		}
		return precedence;
	}

}
