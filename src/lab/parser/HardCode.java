package lab.parser;
import java.util.ArrayList;
import java.util.List;

public class HardCode {
	public static void main(String[] args) {
		List<Integer> test = new ArrayList<>();
		
		test.add(1);
		test.add(2);
		test.add(4);
		
		test.add(2, 3);
		
		for (Integer i : test) System.out.println(i);
	}
}
