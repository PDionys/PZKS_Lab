package lab.parser;

import lab.test.TreeBuilderTest;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
//		boolean trigger = true;
//		Scanner scanner = new Scanner(System.in);
//		String filepath = "D:/Eclipse/eclipse-workspace/Equation Parser/src/expression.txt";
//		
//		do {
//			//ParserTest test = new ParserTest();
//			//test.testCheckEquation();
//			try (BufferedReader reader = new BufferedReader(new FileReader(filepath))){
//				String line;
//				while ((line = reader.readLine()) != null) {
//					Parser.analyzeLexis(line);
//					Parser.analyzeSyntax(line);
//				}
//			}catch(IOException e) {
//	            e.printStackTrace();
//	        }
//			
//			System.out.print("Для завершення натисніть [c]: ");
//			char symbol = scanner.next().charAt(0);
//			if(symbol == 'c')
//				trigger = false;
//		}while (trigger);
//		
//		scanner.close();
		
		TreeBuilderTest test = new TreeBuilderTest();
		test.test();
	}
}
