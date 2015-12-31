import java.util.*;

public class EulerThree {

	static List<Long> factors (long num){ // function will return list type
		ArrayList factors = new ArrayList<Long>();

		for (long i =2; i <= num; i ++){

			while (num % i == 0){
				factors.add(i);
				num /= i;
			}
		}
		return factors;
	}


	public static void main (String [] args) {
		int i = 0;
		Scanner input = new Scanner(System.in);
		int testCase = input.nextInt();

		while (testCase != 0) {
			int n = input.nextInt();
			for (long lon: factors(n)){
				System.out.println(lon);
				// code to print the largest number
			}
			testCase--;
		}
	}
}
