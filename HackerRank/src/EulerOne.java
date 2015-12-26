import java.io.*;
import java.util.*;

public class EulerOne {
public static float sumMupltiple(float num, float limit) {
		float numOfMulitples = (limit - 1) / num;
		return num * (numOfMulitples * (numOfMulitples + 1) / 2);
	}

	public static void main(String[] args) {
		int i = 0;
		Scanner input = new Scanner (System.in);
		float testCases = input.nextInt();
		while(i < testCases) {
			float limit = input.nextInt();
			System.out.println(sumMupltiple(3, limit) + sumMupltiple(5, limit) - sumMupltiple(15, limit));
			i++;
		}
	}
}