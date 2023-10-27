package lab.parser;
import java.util.ArrayList;
import java.util.Stack;

public class ExpressionTreeBuilder_old {
	public static TreeNode buildExpressionTree (String expression) {
		String[] tokens = tokenize(expression);
		Stack<TreeNode> nodeStack = new Stack<>();
		Stack<String> operatorStack = new Stack<>();
		
		for (String token : tokens) {
			if (isOperand(token)) {
				nodeStack.push(new TreeNode(token));
			}else if (isOperator(token)) {
				while (!operatorStack.isEmpty() && hasPrecedence(token, operatorStack.peek())) {
					TreeNode rightNode = nodeStack.pop();
					TreeNode leftNode = nodeStack.pop();
					String operator = operatorStack.pop();
					
					TreeNode newNode = new TreeNode(operator);
					newNode.left = leftNode;
					newNode.right = rightNode;
					nodeStack.push(newNode);
				}
				operatorStack.push(token);
			}else if (token.equals("(")) {
				operatorStack.push(token);
			}else if (token.equals(")")) {
				while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
					TreeNode rightNode = nodeStack.pop();
					System.out.print(nodeStack.toString());
					TreeNode leftNode = nodeStack.pop();
					String operator = operatorStack.pop();
					
					TreeNode newNode = new TreeNode(operator);
					newNode.left = leftNode;
					newNode.right = rightNode;
					nodeStack.push(newNode);
				}
				operatorStack.pop();
			}
		}
		
		while (!operatorStack.isEmpty()) {
			TreeNode rightNode = nodeStack.pop();
			TreeNode leftNode = nodeStack.pop();
			String operator = operatorStack.pop();
			
			TreeNode newNode = new TreeNode(operator);
			newNode.left = leftNode;
			newNode.right = rightNode;
			nodeStack.push(newNode);
		}
		
		return nodeStack.pop();
	}
	
	private static String[] tokenize(String expression) {
	    // Use regular expression to split the expression into tokens
	    String[] tokens = expression.split("(?=[()+*/-])|(?<=[()+*/-])");

	    // Remove any empty tokens resulting from consecutive separators
	    ArrayList<String> tokenList = new ArrayList<>();
	    for (String token : tokens) {
	        if (!token.trim().isEmpty()) {
	            tokenList.add(token);
	        }
	    }

	    return tokenList.toArray(new String[0]);
	}
	
	private static boolean isOperand(String token) {
		return token.matches("[0-9]+");
	}
	
	private static boolean isOperator(String token) {
		return token.matches("[+\\-*/]");
	}
	
	private static int precedence(String operator) {
		switch(operator) {
		case "+":
		case "-":
			return 1;
		case "*":
		case "/":
			return 2;
		}
		return 0;
	}
	
	private static boolean hasPrecedence(String op1, String op2) {
		return precedence(op1) >= precedence(op2);
	}
}
