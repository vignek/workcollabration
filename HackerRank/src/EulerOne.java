import java.util.*;

public class EulerOne {

	public static void main (String [] args) {

		int total = 0;

		Scanner input = new Scanner(System.in);
		int i = input.nextInt();
		int one = input.nextInt();
		int two = input.nextInt();

		for (int j = 0; i < one; i++) {
			if(j %3 == 0  || j %5 ==0) {
				total = total +j;
			}
		}
		System.out.println("");



	}
}
