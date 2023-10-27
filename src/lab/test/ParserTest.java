package lab.test;

import lab.parser.Parser;

public class ParserTest {
	private static String equation;
	
	public ParserTest() {
		equation = "cos(2+3)";
	}
	
	public void testCheckEquation() {
		Parser.analyzeLexis(equation);
		Parser.analyzeSyntax(equation);
	}
}
