package lab.test;
import java.util.Stack;

import lab.parser.*;

public class TreeBuilderTest {
	private static String equation;
	
	public TreeBuilderTest() {
		equation = "a*b-b*c-c*d-a*c*(b-d/e/f/g)-(g-h)-(i-j)";//(a+b)+c/d+e+(f/g+h+p) //(a*b)/(c+d-(4*e-2.09+3.1415))+(k+l)+1
		/*
		 * a+b+c+d+e+f+g+h
		 * a-b-c-d-e-f-g-h
		 * a+(b+c+d+(e+f)+g)+h
		 * 
		 * a-((b-c-d)-(e-f)-g)-h
		 * a/b/c/d/e/f/g/h
		 * a*b-b*c-c*d-a*c*(b-d/e/f/g)-(g-h)-(i-j) //Починить больше не для сдачи а для себя //Лучше делить только + и * на термы
		 * 
		 * 
		 * 5040/8/7/6/5/4/3/2 (1)
		 * 10-9-8-7-6-5-4-3-2-1 (-35)
		 * 64-(32-16)-8-(4-2-1) (39)
		 * 
		 * asd/1.0+0-0*k*h+2-4.8/2+1*e/2
		 */
	}
	
	public void test() {
		Stack<TreeNode> full = ExpressionTreeBuilder.buildExpressionTree(equation);
		//for(TreeNode h : full) ExpressionTreeBuilder.build(h);
	}
}
