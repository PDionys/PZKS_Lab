package lab.parser;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lab.parser.SmallTree;
import java.util.Collections;
import java.util.Comparator;
import lab.parser.Result;

public class ExpressionTreeBuilder {
	public static Stack<TreeNode> buildExpressionTree(String expression) {
		//-------Prepare expression-------
		String[] tokens = findall(expression);
		//tokens = DeleteOneZero(tokens);
		tokens = CalculateNumbers(tokens);
		if (tokens.length == 1) {
			for(String t : tokens) System.out.print(t);
			return null;
		}
		
		//Hardcode
		int kil = isValidStringPattern(tokens);
		switch(kil) {
		case 1:
			tokens = MinusOut(tokens);
			for(String t : tokens) System.out.print(t);
			break;
		case 2:
			tokens = BracketOut(tokens);
			for(String t : tokens) System.out.print(t);
			break;
		case 3:
			tokens = ChangeDivide(tokens);
			for(String t : tokens) System.out.print(t);
			break;
		case 4:
			tokens = BracketOutMinus(tokens);
			for(String t : tokens) System.out.print(t);
			break;
		default:
			break;	
		}
//		if(kil == 1) {
//			tokens = MinusOut(tokens);
//			for(String t : tokens) System.out.print(t);
//			//return null;
//		}
		
		//-----Separate terms-----
		System.out.println();
		for (String s : tokens) System.out.print(s + ',');
		List<SmallTree> smallexpression = divideOnSmallerExpres(tokens);
		smallexpression = updateTreeList(smallexpression);
		Collections.sort(smallexpression, Comparator.comparingInt(SmallTree::getPrior));
//		System.out.println();
//		for (SmallTree s : smallexpression) System.out.print(s.toString());
		
		//Stack<SmallTree> smalltrees = new Stack<>();
		//for(SmallTree small : smallexpression) smalltrees.push(buildTree(small.getValue()));
		
		/*do {
			TreeNode right_operand = smalltrees.pop();
			TreeNode left_operand = smalltrees.pop();
			
			TreeNode operator_node = new TreeNode("+");
			operator_node.left = left_operand;
			operator_node.right = right_operand;
			smalltrees = pushToTheBottom(smalltrees, operator_node);
		}while(smalltrees.size() != 1);
		
		System.out.println();
		for(TreeNode h : smalltrees) {
			build(h);
		}*/
		
		SmallTree temp;
		String firstoper, secondoper;
		do {
			temp = smallexpression.get(0);
			firstoper = temp.getKey();
			TreeNode right_operand = temp.getNode();
			smallexpression.remove(0);
			temp = smallexpression.get(0);
			secondoper = temp.getKey();
			TreeNode left_operand = temp.getNode();
			smallexpression.remove(0);
			
			TreeNode operator_node = new TreeNode(firstoper);
			operator_node.left = left_operand;
			operator_node.right = right_operand;
//			System.out.println("Builded tree:");
//			build(operator_node);
			//System.out.println("Second: " + secondoper);
			/*
			 * Это костыль 
			 * |
			 * V*/
			if(smallexpression.size() <= 1) {
				smallexpression.add(new SmallTree(operator_node, secondoper));
			}else {
				//smallexpression.add(new SmallTree(operator_node, secondoper));
				boolean trigger = true;
				int current_prior = smallexpression.get(0).getPrior();
				for(int i = 0; i < smallexpression.size(); i++) {
					if(i != 0) {
						if(smallexpression.get(i).getPrior() != current_prior) {
							smallexpression.add(i, 
									new SmallTree(operator_node, secondoper, current_prior));
							trigger = false; 
							break;
						}
					}
				}
				if(trigger) {
					smallexpression.add(new SmallTree(operator_node, secondoper, current_prior));
				}
			}
			//smallexpression.add(new SmallTree(operator_node, secondoper));
		}while(smallexpression.size() != 1);
		
		System.out.println();
		build(smallexpression.get(0).getNode());
		
		return null;
	}
	
	private static String[] DeleteOneZero(String[] tokens) {
		List<String> token = new ArrayList<>();
		for (String s : tokens) token.add(s);
		
		token = DeleteConstDivideOne(token);
		token = DeleteConstMultiplyOne(token);
		//token = DeleteZerotDivideConst(token);
		//token = DeleteZerotMultiplyConst(token);
		
		return token.toArray(new String[0]);
	}
	private static List<String> DeleteZerotDivideConst (List<String> token) {
		boolean triger;
		do {
			triger = false;
			for(int i = 0; i < token.size(); i++) {
				if (i != token.size()-1) {
					if((token.get(i).equals("0") || token.get(i).equals("0.0")) 
							&& token.get(i+1).equals("/")) {
						
	//					try {
	//						token.get(i+4);
	//						for(int j = 0; j < 4; j++)
	//							token.remove(i);
	//					}catch(Exception e){
	//						for(int j = 0; j < 3; j++)
	//							token.remove(i);
	//						token.remove(i-1);
	//					}
						for(int j = 0; j < 2; j++) {
							token.remove(i+1);
						}
						triger = true;
						break;
					}
				}
			}
		}while(triger);
		
		return token;
	}
	private static List<String> DeleteConstMultiplyOne (List<String> token) {
		boolean triger;
		do {
			triger = false;
			for(int i = 0; i < token.size(); i++) {
				if(i == 0) {
					if((token.get(i).equals("1") || token.get(i).equals("1.0")) 
							&& token.get(i+1).equals("*")) {
						token.remove(i+1);
						token.remove(i);
						triger = true;
						break;
					}
				}else {
					if((token.get(i).equals("1") || token.get(i).equals("1.0")) 
							&& token.get(i+1).equals("*")) {
						token.remove(i+1);
						token.remove(i);
						triger = true;
						break;
					}else if((token.get(i).equals("1") || token.get(i).equals("1.0")) 
							&& token.get(i-1).equals("*")) {
						token.remove(i);
						token.remove(i-1);
						triger = true;
						break;
					}
				}
			}
		}while(triger);
		
		return token;
	}
	private static List<String> DeleteConstDivideOne (List<String> token) {
		boolean triger;
		do {
			triger = false;
			for(int i = 0; i < token.size(); i++) {
				if (i != 0)
					if((token.get(i).equals("1") || token.get(i).equals("1.0")) 
							&& token.get(i-1).equals("/")) {
						token.remove(i);
						token.remove(i-1);
						triger = true;
						break;
					}
			}
		}while(triger);
		
		return token;
	}
	
	private static String[] CalculateNumbers (String[] tokens) {
		List<String> token = new ArrayList<>();
		Result result;
		for (String s : tokens) token.add(s);
		do {
			token = CalculateDivide(token);
			result = DeleteSingleNumberInBraket(token);
			token = result.getStringValue();
		}while(result.getBooleanValue());
		token = MarkMultiplyAndDivide(token);
		do {
			token = CalculateMultiply(token);
			result = DeleteSingleNumberInBraket(token);
			token = result.getStringValue();
		}while(result.getBooleanValue());
		
		do {		
			token = CalculateAddAndSubtract(token);
			result = DeleteSingleNumberInBraket(token);
			token = result.getStringValue();
		}while(result.getBooleanValue());
		
		return token.toArray(new String[0]);
	}
	private static List<String> MarkMultiplyAndDivide(List<String> token) {
		boolean triger;
		do {
			triger = false;
			for (int i = 0; i < token.size(); i++) {
				if (token.get(i).equals("*") || token.get(i).equals("/")) {
					if (!isNumber(token.get(i-1)) && isNumber(token.get(i+1))) {
						String temp = token.get(i+1) + "'";
						token.set(i+1, temp);
						triger = true;
						break;
					}else if (isNumber(token.get(i-1)) && !isNumber(token.get(i+1))) {
						String temp = token.get(i-1) + "'";
						token.set(i-1, temp);
						triger = true;
						break;
					}
				}
			}
		}while(triger);
		
		return token;
	}
	private static Result DeleteSingleNumberInBraket(List<String> token) {
		boolean resulttriger = false;
		boolean triger;
		do {
			triger = false;
			for (int i = 0; i < token.size(); i++) {
				if (!isOperator(token.get(i)) 
						&& !token.get(i).equals(")") && !token.get(i).equals("(")) {
					if (i != 0) {
						if (token.get(i-1).equals("(") && token.get(i+1).equals(")")) {
							token.remove(i-1);
							token.remove(i);
							triger = true;
							resulttriger = true;
							break;
						}
					}
				}
			}
		}while(triger);
		
		return new Result(token, resulttriger);
	}
	private static List<String> CalculateAddAndSubtract(List<String> token) {
		boolean triger;
		do {
			triger = false;
			for (int i = 0; i < token.size(); i++) {
				if (token.get(i).equals("+") || token.get(i).equals("-")) {
					if (isNumber(token.get(i-1)) && isNumber(token.get(i+1))){
						Double firtsoperand = Double.parseDouble(token.get(i-1));
						Double secondoperand = Double.parseDouble(token.get(i+1));
						Double result;
						
						switch(token.get(i)) {
						case "+":
							result = firtsoperand + secondoperand;
							token.set(i, result.toString());
							break;
						case "-":
							result = firtsoperand - secondoperand;
							token.set(i, result.toString());
							break;
						}
//						System.out.println();
//						for (String test : token) System.out.print(test);
						token.remove(i-1);
//						System.out.println();
//						for (String test : token) System.out.print(test);
						token.remove(i);
//						System.out.println();
//						for (String test : token) System.out.print(test);
//						System.out.println("Error: " + token.size());
						if(token.size() != 1) {
							triger = true;
							break;
						}else break;
					}
				}
			}
		}while(triger);
		
		return token;
	}
	private static List<String> CalculateMultiply(List<String> token) {
		boolean triger;
		do {
			triger = false;
			for (int i = 0; i < token.size(); i++) {
				if (token.get(i).equals("*")) {
					if (isNumber(token.get(i-1)) && isNumber(token.get(i+1))){
						Double firtsoperand = Double.parseDouble(token.get(i-1));
						Double secondoperand = Double.parseDouble(token.get(i+1));
						Double result = firtsoperand * secondoperand;
						
						token.set(i, result.toString());
//						System.out.println();
//						for (String test : token) System.out.print(test);
						token.remove(i-1);
//						System.out.println();
//						for (String test : token) System.out.print(test);
						token.remove(i);
//						System.out.println();
//						for (String test : token) System.out.print(test);
//						System.out.println("Error: " + token.size());
						if(token.size() != 1) {
							triger = true;
							break;
						}else break;
					}
				}
			}
		}while(triger);
		
		return token;
	}
	private static List<String> CalculateDivide(List<String> token) {
		boolean triger;
		do {
			triger = false;
			for (int i = 0; i < token.size(); i++) {
				if (token.get(i).equals("/")) {
					if (isNumber(token.get(i-1)) && isNumber(token.get(i+1))){
						Double firtsoperand = Double.parseDouble(token.get(i-1));
						Double secondoperand = Double.parseDouble(token.get(i+1));
						Double result = firtsoperand / secondoperand;
						
						token.set(i, result.toString());
//						System.out.println();
//						for (String test : token) System.out.print(test);
						token.remove(i-1);
//						System.out.println();
//						for (String test : token) System.out.print(test);
						token.remove(i);
//						System.out.println();
//						for (String test : token) System.out.print(test);
//						System.out.println("Error: " + token.size());
						if(token.size() != 1) {
							triger = true;
							break;
						}else break;
					}
				}
			}
		}while(triger);
		
		return token;
	}
	private static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	private static List<SmallTree> updateTreeList(List<SmallTree> smallexpression){
		for (int i = 0; i < smallexpression.size(); i++) {
			SmallTree temp = smallexpression.get(i);
			//checkflsimbol
			temp = checkflsimbol(temp);
			temp.setNode(buildTree(temp.getValue()));
			smallexpression.set(i, temp);
		}
		
		return smallexpression;
	}
	
	private static SmallTree checkflsimbol (SmallTree temp) {
		String[] splited = findall(temp.getValue());
		String str = "";
		
		for(int i = 0; i < splited.length; i++) {
			if(((i == 0 || i == splited.length - 1) && (splited[i].equals("+") || splited[i].equals("-") 
					|| splited[i].equals("/") || splited[i].equals("*")))) {
				temp.setKey(splited[i]);
			}else str += splited[i];
		}
		
		temp.setValue(str);
		return temp;
	}
	
	public static TreeNode buildTree(String expression) {
		String[] tokens = findall(expression);
		Stack<String> rnp_output = ShuntingYard(tokens);
		
		Stack<TreeNode> nodes = new Stack<>();
			if (rnp_output.size() >= 3) {
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
		}else {
			for(String token : rnp_output) {
				if (!token.equals("+") && !token.equals("-")) {
					TreeNode operator_node = new TreeNode(token);
					nodes.push(operator_node);
				}
			}
		}
		
		int i = 0;
		System.out.println();
		for(TreeNode h : nodes) {
			build(h);
		}
		
		return nodes.pop();
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
	
	private static List<SmallTree> divideOnSmallerExpres(String[] expression) {
		int maxprior = countBraket(expression);
		
		List<SmallTree> result = new ArrayList<>();
		int operator_count = 1, minus_counter = 1, divide_counter = 1;
		String str = "";
		for(String token : expression) {
			if (!token.equals("+") && !token.equals("-") 
					&& !token.equals("(") && !token.equals(")") 
					&& !token.equals("/") && !token.equals("*")) {
				str += token;
			}else if (token.equals("+") && operator_count < 2) {
				str += token;
				operator_count++;
			}else if (token.equals("-") && minus_counter < 2) {
				if (operator_count >= 2) {
					result.add(new SmallTree(str, "-", maxprior));
					operator_count = 1;
					str = "";
					minus_counter = 1;
				}else {
					str += token;
					operator_count++;
					minus_counter++;
				}
			}else if (token.equals("/") && divide_counter < 2) {
				str += token;
				operator_count++;
				divide_counter++;
			}else if (token.equals("*") && operator_count < 2) {
				str += token;
				operator_count++;
			}else if (token.equals("*") && operator_count >= 2) {
				result.add(new SmallTree(str, "*", maxprior));
				operator_count = 1;
				str = "";
			}else if(token.equals("-") && minus_counter >= 2) {
				if (operator_count >= 2) {
					result.add(new SmallTree(str, "+", maxprior));
					operator_count = 1;
					str = "";
					minus_counter = 1;
				}else {
					str += token;
					operator_count++;
					minus_counter++;
				}
			}else if (token.equals("/") && divide_counter >= 2) {
				if (operator_count >= 2) {
					result.add(new SmallTree(str, "*", maxprior));
					operator_count = 1;
					str = "";
					divide_counter = 1;
				}else {
					str += token;
					operator_count++;
					divide_counter++;
				}
			}else if (token.equals("(")) {
				operator_count = 1;
				minus_counter = 1;
				divide_counter = 1;
				//System.out.println("Error of breackit: " + str);
				if (!str.equals("") && !str.equals("-") && !str.equals("+") 
						&& !str.equals("/") && !str.equals("*")) {
					result.add(new SmallTree(str, "+", maxprior));
				}else if (str.equals("-") || str.equals("+") 
						|| str.equals("/") || str.equals("*")){
					//System.out.println("Im here!!");
					SmallTree temp = result.get(result.size()-1);
					temp.setKey(str);
					result.set(result.size()-1, temp);
				}
				str= "";
				maxprior--;
			}else if (token.equals(")")) {
				operator_count = 1;
				minus_counter = 1;
				divide_counter = 1;
				if (!str.equals("") && !str.equals("-") && !str.equals("+") 
						&& !str.equals("/") && !str.equals("*")) {
					result.add(new SmallTree(str, "+", maxprior));
				}
				str= "";
				maxprior++;
			}
			else {
				result.add(new SmallTree(str, token, maxprior));
				operator_count = 1;
				str = "";
			}
		}
		
		if (!str.equals("")) {
			result.add(new SmallTree(str, "+", maxprior));
			str = "";
		}
		
		System.out.println();
		for(SmallTree test : result) System.out.print(test.getValue() + ":" + "[" + test.getKey() + "]" + test.getPrior() + ", ");
		
		return result;
	}
	
	public static Stack<TreeNode> pushToTheBottom(Stack<TreeNode> stack, TreeNode bottomElement) {
		Stack<TreeNode> tempStack = new Stack<>();
        while (!stack.isEmpty()) {
            tempStack.push(stack.pop());
        }
        
        stack.push(bottomElement);
        while (!tempStack.isEmpty()) {
            stack.push(tempStack.pop());
        }
        
        return stack;
	}
	
	public static int countBraket(String[] expression) {
		int counter = 0;
		for (String token : expression) {
			if(token.equals("(")) counter++;
		}
		return counter;
	}
	
	private static int isValidStringPattern(String[] input) {
		boolean result = true, brekit = false;
		int iter = 1;
		String oper = "";
		do {
			switch(iter) {
			case 1:
				oper = "-";
				break;
			case 2:
				oper = "+";
				break;
			case 3:
				oper = "/";
				break;
			}
			
			for (String i : input)
				if (isOperator(i)) {
					if(!i.equals(oper)) {
						result = false;
						break;
					}
				}else if (i.equals("(")) {
					brekit = true;
				}
			if(result && !brekit) {
				return iter;
			}else if(result && brekit)
				if(iter == 1) {
					return 4;
				}else if(iter == 2) return 2;
			brekit = false;
			result = true;
			iter++;
		}while(iter != 4);
		
		return 0;
	}
	private static String[] MinusOut(String[] tokens) {
		List<String> new_tokens = new ArrayList<>();
		int minus_counter = 1, i = 0;
		for(String t : tokens) {
			if(isOperand(t) && i != tokens.length-1) {
				new_tokens.add(t);
				i++;
			}else if (t.equals("-") && minus_counter == 1) {
				new_tokens.add(t);
				new_tokens.add("(");
				minus_counter += 1;
				i++;
			}else if(i == tokens.length-1) {
				new_tokens.add(t);
				new_tokens.add(")");
				i++;
			}else if(t.equals("-") && minus_counter >= 2) {
				new_tokens.add("+");
				minus_counter += 1;
				i++;
			}
		}
		
		return new_tokens.toArray(new String[0]);
	}
	
	private static String[] BracketOut(String[] tokens) {	
		List<String> new_tokens = new ArrayList<>();
		for(String t : tokens) {
			if(t.equals("(") || t.equals(")"))
				continue;
			new_tokens.add(t);
		}
		
		return new_tokens.toArray(new String[0]);
	}
	private static String[] BracketOutMinus(String[] tokens) {	
		List<String> new_tokens = new ArrayList<>();
		boolean thisbrekit = false, firstbrekit = true;
		int maxprior = countBraket(tokens);
		String oper = "";
		
		
		do {
			for(int i = 0; i < tokens.length; i++) {
				if(tokens[i].equals("(") && firstbrekit) {
					oper = tokens[i-1];
					firstbrekit = false;
					thisbrekit = true;
					continue;
				}else if (tokens[i].equals("(") && !firstbrekit) {
					new_tokens.add(tokens[i]);
					thisbrekit = false;
				}
				
				if(tokens[i].equals(")") && thisbrekit) {
					thisbrekit = false;
					firstbrekit = true;
					maxprior--;
					continue;
				}else if (tokens[i].equals(")") && !thisbrekit){
					thisbrekit = true;
					new_tokens.add(tokens[i]);
				}
				
				if(!tokens[i].equals("(") && !tokens[i].equals(")") && thisbrekit) {
					if(oper.equals("-")) {
						if(isOperator(tokens[i])) { 
							if(tokens[i].equals("-")) {
								new_tokens.add("+");
							}else new_tokens.add("-");
						}else new_tokens.add(tokens[i]);
					}else if(oper.equals("+")) new_tokens.add(tokens[i]);
				}else if ((!tokens[i].equals("(") && !tokens[i].equals(")") && !thisbrekit))
					new_tokens.add(tokens[i]);
			}
			
			tokens = new_tokens.toArray(new String[0]);
			new_tokens.clear();
		}while(maxprior != 0);
		
		return tokens;
	}
	private static String[] ChangeDivide(String[] tokens) {
		List<String> new_tokens = new ArrayList<>();
		switch(tokens.length) {
		case 3:
			return tokens;
		case 5:
			for(int i = 0; i < tokens.length; i++) {
				if(i==2) {
					new_tokens.add("(");
					new_tokens.add(tokens[i]);
				}else if (i == 3) {
					new_tokens.add("*");
				}else if(i==4) {
					new_tokens.add(tokens[i]);
					new_tokens.add(")");
				}else {
					new_tokens.add(tokens[i]);
				}
			}
			break;
		case 7:
			for(int i = 0; i < tokens.length; i++) {
				if(i == 0 || i == 4) {
					new_tokens.add("(");
					new_tokens.add(tokens[i]);
				}else if (i == 2 || i == 6) {
					new_tokens.add(tokens[i]);
					new_tokens.add(")");
				}else if(i == 5) {
					new_tokens.add("*");
				}else {
					new_tokens.add(tokens[i]);
				}
			}
			break;
		case 9:
			for(int i = 0; i < tokens.length; i++) {
				if(i == 0) {
					new_tokens.add("(");
					new_tokens.add("(");
					new_tokens.add(tokens[i]);
				}else if (i == 2) {
					new_tokens.add(tokens[i]);
					new_tokens.add(")");
				}else if(i == 4) {
					new_tokens.add("(");
					new_tokens.add(tokens[i]);
				}else if(i == 5) {
					new_tokens.add("*");
				}else if (i == 6) {
					new_tokens.add(tokens[i]);
					new_tokens.add(")");
					new_tokens.add(")");
				}else {
					new_tokens.add(tokens[i]);
				}
			}
			break;
		default:
			for(int i = 0; i < tokens.length; i++) {
				if(i == 0) {
					new_tokens.add("(");
					new_tokens.add("(");
					new_tokens.add(tokens[i]);
				}else if (i == 2 || i == tokens.length-1) {
					new_tokens.add(tokens[i]);
					new_tokens.add(")");
				}else if(i == 4 || i == 8) {
					new_tokens.add("(");
					new_tokens.add(tokens[i]);
				}else if(i == 5) {
					new_tokens.add("*");
				}else if (i == 6) {
					new_tokens.add(tokens[i]);
					new_tokens.add(")");
					new_tokens.add(")");
				}else if (i >= 8 && i != tokens.length - 1) {
					if(isOperator(tokens[i])) {
						new_tokens.add("*");
					}else new_tokens.add(tokens[i]);
				}else {
					new_tokens.add(tokens[i]);
				}
			}
			break;
		}
		
		return new_tokens.toArray(new String[0]);
	}
}
