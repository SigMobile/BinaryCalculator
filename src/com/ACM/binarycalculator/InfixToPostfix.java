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
			
			//if the token is a number
			if (!(currentToken.equals("+") || currentToken.equals("x")
					|| currentToken.equals("-") || currentToken.equals("/")
					|| currentToken.equals("\n") || currentToken.equals(space)
					|| currentToken.equals("(") || currentToken.equals(")"))) {

				postfix += currentToken + space;

				//if the token is an open parenthesis
			} else if (currentToken.equals("(")) {
				theStack.push(currentToken);
				
				//if the token is an operator
			} else if (currentToken.equals("+") || currentToken.equals("x")
					|| currentToken.equals("-") || currentToken.equals("/")
					|| currentToken.equals("\n")) {

				while (!theStack.isEmpty()
						&& operatorPrecedence(theStack.peek()) >= operatorPrecedence(currentToken)) {

					postfix += theStack.pop() + space;
				}
				theStack.push(currentToken);
				
				//if the token is a closed parenthesis
			} else if (currentToken.equals(")")) {
				while (!theStack.peek().equals("(")) {
					postfix += theStack.pop() + space;
				}
				theStack.pop();
				
				//if the token is a space
			} else if (currentToken.equals(space)) {
				// do nothing
			}
		} // closes while()

		//get what's in the stack
		while (!theStack.isEmpty()) {
			postfix += theStack.pop() + space;
		}

		//return new post-fix expression
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
