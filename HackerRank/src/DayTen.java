import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class DayTen {

    public static void main(String[] args) {
        
    	int[] bn = new int[100];
    	int q = 0;
    	Scanner sc = new Scanner(System.in);
    	int T = sc.nextInt();
    	
    	for (int i = 0; i < T; i++) {
    
    		int k = 0; 
    		int n = sc.nextInt();
    		q = n;
    		
    		while (q !=0) {
    			bn[k++] = q % 2;
    			q = q / 2;
    		}
    		
    		for(int j = k - 1; j >= 0; j--){
    			System.out.print(""+bn[j]);
    		}
    		
    	}
    
    }
}
