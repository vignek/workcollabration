import java.io.*;
import java.util.*;

public class EulerOne {
	public static int sumOne(int num, int limit) {
		int numOfMulitples = (limit - 1) / num;
		return num * (numOfMulitples * (numOfMulitples + 1) / 2);
	}

	public static void main(String[] args) {
		int i = 0;
		Scanner input = new Scanner (System.in);
		int testCases = input.nextInt();
		while(i < testCases) {
			int limit = input.nextInt();
			System.out.println(sumOne(3, limit) + sumOne(5, limit) - sumOne(15, limit));
			i++;
		}
	}
}