package com.ACM.binarycalculator;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author James Van Gaasbeck, ACM at UCF <jjvg@knights.ucf.edu>
 */

public class PostfixEvaluator {

	
	public static String evaluate(String postfix){
		Stack<String> stack = new Stack<String>();
		// StringTokenizer to split up the expression
		StringTokenizer toke = new StringTokenizer(postfix,
				"x+-/)( \n", true);
		
		while(toke.hasMoreElements()){
			String currentToken = toke.nextElement().toString();
			if(!(currentToken.equals("+") || currentToken.equals("x")
					|| currentToken.equals("-") || currentToken.equals("/")
					|| currentToken.equals("\n") || currentToken.equals(" ")
					|| currentToken.equals("(") || currentToken.equals(")"))){
				
				stack.push(currentToken);
			}
			else if (currentToken.equals("+") || currentToken.equals("x")
					|| currentToken.equals("-") || currentToken.equals("/")
					|| currentToken.equals("\n")) {
				
		        String operand2 = stack.pop();
		        String operand1 = stack.pop();
		        String value = doMathStuff(operand1, operand2, currentToken);
		        stack.push(value);
			}
		}
		
		return stack.pop();
	}

	private static String doMathStuff(String operand1, String operand2,
			String currentToken) {
		
		double value1=Double.parseDouble(operand1);
	    double value2=Double.parseDouble(operand2);
	    double result=0;
	 
	    if (currentToken.equals("+"))
	    {
	      result = value1 + value2;
	    }
	    else if (currentToken.equals("-"))
	    {
	      result = value1 - value2;
	    }
	    else if (currentToken.equals("*"))
	    {
	      result = value1 * value2;
	    }
	    else if (currentToken.equals("/"))
	    {
	      result = value1 / value2;
	    }
	 
	    return "" + result;
	}

	
}
