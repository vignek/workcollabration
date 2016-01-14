import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class DaySix {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		int h = sc.nextInt();

		for (int l = 1; l <= h; l++) {

			for (int j = 0 ; j < l; j++) {
				if (j == 0) {
					for (int k = 0; k < h -l ; k++) {
						System.out.print(" ");
					}
				}
				System.out.print("#");
			}
			System.out.println("");
		}
	}
}
