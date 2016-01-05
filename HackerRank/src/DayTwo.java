import java.io.*;
import java.util.*;

public class DayTwo {

	public static void main (String [] args) {

		Scanner sc = new Scanner(System.in);
		double M = sc.nextDouble(); // original meal price
		int T = sc.nextInt(); // tip percentage
		int X = sc.nextInt(); // tax percentage

		// y/200 = 25/100

		// Cross multiply to get y × 100 = 200 × 25

		double T1 = (T * M)/100;
		
		double X1 = (X * T1)/100;
		
		
		int total = (int) Math.round(M+T1+X1);

		System.out.println("The final price of the meal is $"+total+".");
	}
}
