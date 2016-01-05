import java.util.*;

public class EulerThree {

	static long factors (long num) { // function will return list type
		ArrayList factors = new ArrayList<Long>();

		for (long i =2; i <= num; i++) {
			while (num % i == 0) {
				factors.add(i);
				num /= i;
			}
		}
		long i =  Collections.max(factors);
		return i;
	}


	public static void main (String [] args) {
		int i = 0;
		Scanner input = new Scanner(System.in);
		int testCase = input.nextInt();

		while (testCase != 0) {
			long n = input.nextInt();
			factors(n);
			System.out.println(i);
			testCase--;
		}
	}
}
