// even fibonacci series
import java.util.*;
public class EluerTwo {
	public static void main(String args[]){

		int i = 0;
		Scanner input = new Scanner (System.in);
		int testCases = input.nextInt();
		
		while(i < testCases) {
			int limit = input.nextInt();
			int sum = 0;
			int a = 0;
			int b = 1;
			int c = a + b;
			while (c < limit){
				if (c % 2 == 0){
					sum = sum + c;
				}
				a = b;
				b = c;
				c = a + b;
			}
			System.out.println(sum);
		i++;
		}
	}
}

