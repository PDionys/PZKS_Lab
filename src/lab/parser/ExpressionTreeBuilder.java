package lab.parser;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class ExpressionTreeBuilder {
	public static Stack<TreeNode> buildExpressionTree(String expression) {
		String[] tokens = findall(expression);
		Stack<String> rnp_output = ShuntingYard(tokens);
		
		Stack<TreeNode> nodes = new Stack<>();
		for(String token : rnp_output) {
			if (isOperand(token)) {
				nodes.push(new TreeNode(token));
			}else if(isOperator(token)) {
				TreeNode right_operand = nodes.pop();
				TreeNode left_operand = nodes.pop();
				
				TreeNode operator_node = new TreeNode(token);
				operator_node.left = left_operand;
				operator_node.right = right_operand;
				nodes.push(operator_node);
			}
		}
		
		return nodes;
	}
	
	public static void build(TreeNode root) {
		build(root, "");
	}
	
	public static void build (TreeNode node, String prefix) {
		if (node != null) {
            System.out.println(prefix + "|__ " + node.data);
            build(node.left, prefix + "|   ");
            build(node.right, prefix + "|   ");
        }
	}
	
	private static Stack<String> ShuntingYard(String[] tokens) {
		Stack<String> operators = new Stack<>();
		Stack<String> output = new Stack<>();
		
		for(String token : tokens) {
			if (isOperand(token)) {
				//System.out.println("It is an operand: " + token);
				output.push(token);
			}else if (isOperator(token)) {
				//System.out.println("It is an operator: " + token);
				while (!operators.isEmpty() && hasPrecedence(token, operators.peek())) {
					output.push(operators.pop());
				}
				operators.push(token);
			}else if(token.equals("(")) {
				operators.push(token);
			}else if (token.equals(")")) {
				while(!operators.peek().equals("(")) {
					output.push(operators.pop());
				}
				operators.pop();
			}
		}
		
		while(!operators.isEmpty()) output.push(operators.pop());
		
		return output;
	}
	
	private static boolean isOperand(String token) {
		return token.matches("[a-zA-Z_][a-zA-Z0-9_]*|\\d+\\.\\d+|\\d+");
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
		if (op2 != "(") {
			return precedence(op2) >= precedence(op1);
		}
		return false;
	}
	
	private static String[] findall(String expression) {
		String pattern = "[+\\-*/=()]|[a-zA-Z_][a-zA-Z0-9_]*|\\d+\\.\\d+|\\d+";
		Pattern regexPattern = Pattern.compile(pattern);
		Matcher matcher = regexPattern.matcher(expression);
		List<String> tokens = new ArrayList<>();
		
		while (matcher.find()) {
			String token = matcher.group();
			tokens.add(token);
		}
		
		return tokens.toArray(new String[0]);
	}
}
