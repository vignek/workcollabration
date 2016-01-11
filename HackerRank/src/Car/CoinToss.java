package Car;

import  java.util.*;

public class CoinToss {

	public int Tossing() {
		Random rand = new Random();
		int toss = Math.abs(rand.nextInt())%2; // if divisible by 2 then 0 else 1
		return toss;
	}

	public static void main (String [] args) {

		Scanner in = new Scanner(System.in);
		int TestCase = in.nextInt();
		 
		CoinToss game = new CoinToss();

		for(int i = 0 ; i < TestCase; i++) {
			if(game.Tossing() ==0) {
				System.out.println("Heads");
			}
			else {
				System.out.println("Tails");
			}

		}
	}

}
