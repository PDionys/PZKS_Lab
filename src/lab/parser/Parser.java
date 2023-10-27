/*
 * Это можна сделать по другому с использованием токенов
 * 1)Разбить строку на символы и пройтись по каждому символу
 * 2)Создать токен на каждую операцию типа '\\b\\d+(\\.\\d+){2,}\\b'
 * 3)У каждого символа проверять пердидущий и следующий токен на возможность стоять рядом с єтим токенном
 * expression.substring(currTokenIndex) 
 */
package lab.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {	
	public static void analyzeLexis(String expression) {
		System.out.println("Лексичний аналіз:");
		//помилки, пов’язані з неправильним написанням імен змінних
		detectLexis(expression);
		detectFlowingDot(expression);
    }
	
	public static void analyzeSyntax(String expression) {
		System.out.println("Синтаксичний аналіз:");
		//помилки на початку арифметичного виразу
		detectFirstChar(expression);
		//помилки, пов’язані з використанням дужок
		detectNotClosedBracket(expression);
		detectEmptyBrackets(expression);
		//помилки в середині виразу
		detectVariableAndBracket(expression);
		detectCloseOpenBracketsOperation(expression);
		detectOperationAfterBracket(expression);
		detectOperationBeforeBracket(expression);
		detectMultyOperations(expression);
		detectMathFunc(expression);
		//помилки у кінці виразу
		detectLastChar(expression);
	}
	
	private static void detectLexis(String expression) {
		String lexis = "[^a-zA-Z0-9+\\-*/()._]";
        Pattern pattern = Pattern.compile(lexis);
        match(pattern, " Неправильний параметр: ", expression);
	}
	
	private static void detectFlowingDot(String expression) {
		/*
		 * Точка в словах
		 */
		String flowingDot = "\\b\\d+(\\.\\d+){2,}\\b";
        Pattern pattern = Pattern.compile(flowingDot);
        match(pattern, " Неправильний параметр: ", expression);
	}
	
	private static void detectFirstChar(String expression) {
		if(expression.charAt(0) == ')' || 
				expression.charAt(0) == '*' || expression.charAt(0) == '/') {
			System.out.println("1 Помилки на початку виразу: " 
				+ expression.charAt(0));
		}
	}
	
	private static void detectLastChar(String expression) {
		if(expression.charAt(expression.length()-1) == '+' ||
				expression.charAt(expression.length()-1) == '-' ||
				expression.charAt(expression.length()-1) == '*' ||
				expression.charAt(expression.length()-1) == '/') {
			System.out.println(expression.length() + " Помилки у кінці виразу: " + 
				expression.charAt(expression.length()-1));
		}
	}
	
	private static void detectNotClosedBracket(String expression) {
		int open = countOpenBracket(expression);
		int close = countCloseBracket(expression);
		int N;
		
		if(open > close) { 
			N = open - close;
			System.out.println("Не вистачає " + N + " дужок: )");
		}else if(open < close) {
			N = close - open;
			System.out.println("Не вистачає " + N + " дужок: (");
		}
	}
	
	private static int countOpenBracket(String expression) {
		ArrayList<String> bracketOpen = new ArrayList<>();
		String syntax = "\\(";
		Pattern pattern = Pattern.compile(syntax);
		Matcher matcher = pattern.matcher(expression);
		while (matcher.find()) {
            String findingParam = matcher.group();
            bracketOpen.add(findingParam);
        }
		
		return bracketOpen.size();
	}
	
	private static int countCloseBracket(String expression) {
		ArrayList<String> bracketClose = new ArrayList<>();
		String syntax = "\\)";
		Pattern pattern = Pattern.compile(syntax);
		Matcher matcher = pattern.matcher(expression);
		while (matcher.find()) {
            String findingParam = matcher.group();
            bracketClose.add(findingParam);
        }
		
		return bracketClose.size();
	}
	
	private static void detectEmptyBrackets(String expression) {
		String syntax = "\\(\\)";
		Pattern pattern = Pattern.compile(syntax);
		match(pattern, " Неправильний параметр: ", expression);
	}
	
	private static void detectMultyOperations(String expression) {
		/*String syntaxadd = "\\b\\w+\\+{2,}\\w+\\b";
		String syntaxsubtract = "\\b\\w+\\-{2,}\\w+\\b";
		String syntaxmultiply = "\\b\\w+\\*{2,}\\w+\\b";
		String syntaxdivide = "\\b\\w+\\/{2,}\\w+\\b";
		*/
		String operation = "[+\\-*/]";
		String syntaxadd = "\\+" + operation;
		String syntaxsubtract = "\\-" + operation;
		String syntaxmultiply = "\\*" + operation;
		String syntaxdivide = "\\/" + operation;
		Pattern pattern = Pattern.compile(syntaxadd + "|" 
				+ syntaxsubtract + "|" + syntaxmultiply + "|" + syntaxdivide);
		match(pattern, " Неправильно використано операцію: ", expression);
	}
	
	private static void detectOperationAfterBracket(String expression) {
		String syntaxadd = "\\(\\+{1,}\\w+";
		String syntaxsubtract = "\\(\\-{1,}\\w+";
		String syntaxmultiply = "\\(\\*{1,}\\w+";
		String syntaxdivide = "\\(\\/{1,}\\w+";

		Pattern pattern = Pattern.compile(syntaxadd + "|" 
				+ syntaxsubtract + "|" + syntaxmultiply + "|" + syntaxdivide);
		match(pattern, " Неправильно використано операцію: ", expression);
	}
	
	private static void detectOperationBeforeBracket(String expression) {
		String syntaxadd = "\\+{1,}\\)";
		String syntaxsubtract = "\\-{1,}\\)";
		String syntaxmultiply = "\\*{1,}\\)";
		String syntaxdivide = "\\/{1,}\\)";
		
		Pattern pattern = Pattern.compile(syntaxadd + "|" 
				+ syntaxsubtract + "|" + syntaxmultiply + "|" + syntaxdivide);
		match(pattern, " Неправильно використано операцію: ", expression);
	}
	
	private static void detectCloseOpenBracketsOperation(String expression) {
		String syntax = "\\w+\\)\\(\\w+";
		Pattern pattern = Pattern.compile(syntax);
		match(pattern, " Пропущено операцію: ", expression);
	}
	
	private static void match(Pattern pattern, String text, String expression) {
		Matcher matcher = pattern.matcher(expression);
		while (matcher.find()) {
            String invalidParam = matcher.group();
            int position = matcher.start()+1;
            System.out.println(position + text + invalidParam);
        }
	}
	
	private static void detectVariableAndBracket(String expression) {
		// 1(
		String syntax = "\\b\\d+\\(";
		Pattern pattern = Pattern.compile(syntax);
		match(pattern, " Пропущено операцію: ", expression);
		// )1 or )a
		syntax = "\\)\\w+\\b";
		pattern = Pattern.compile(syntax);
		match(pattern, " Пропущено операцію: ", expression);
		// 1a
		syntax = "\\b\\d\\w[a-zA-Z]*\\b";
		pattern = Pattern.compile(syntax);
		match(pattern, " Пропущено операцію: ", expression);
		// a1
		syntax = "\\b\\[a-zA-Z]*\\d\\b";
		pattern = Pattern.compile(syntax);
		match(pattern, " Пропущено операцію: ", expression);
	}
	
	private static void detectMathFunc(String expression) {
		String syntax = "\\b\\w+\\(";
		Pattern pattern = Pattern.compile(syntax);
		Matcher matcher = pattern.matcher(expression);
		while (matcher.find()) {
            String findingParam = matcher.group();
            if(!(findingParam.equals("cos(") || findingParam.equals("sin(") 
            		|| findingParam.equals("tg(") || findingParam.equals("ctg(") 
            		|| findingParam.equals("abs(") || findingParam.equals("sqrt("))){
            	int position = matcher.start()+1;
                System.out.println(position + " Пропущено операцію: " + findingParam);
            }
        }
	}
}
