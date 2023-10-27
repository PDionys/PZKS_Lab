package lab.test;
import java.util.Stack;

import lab.parser.*;

public class TreeBuilderTest {
	private static String equation;
	
	public TreeBuilderTest() {
		equation = "(a*b)/(c+d-(4*e-2.09+3.1415))+(k+l)+1";//(1+2)+3/4+5+(6/7+8+9)
	}
	
	public void test() {
		/*
		 * Нужно както сделать максимальной ширины минимальной длины
		 * Сделать чтобы принимал такие выражения как: 3.000, abc, A, etc.  V
		 * Сделать чтоби при A/B/C/D -> A/B*C/D etc.
		 * Функциями это операция 
		 */
		Stack<TreeNode> full = ExpressionTreeBuilder.buildExpressionTree(equation);
		for(TreeNode h : full) ExpressionTreeBuilder.build(h);
	}
}
