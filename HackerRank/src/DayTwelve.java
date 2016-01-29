import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class DayTwelve {

    public static void main(String[] args) {
        int max = -64;
        Scanner in = new Scanner(System.in);
        int a[][] = new int[6][6];
        for(int i=0; i < 6; i++){
            for(int j=0; j < 6; j++){
                a[i][j] = in.nextInt();
            }
        }
        for(int i=0;i<6;i++)
            int m;
            for(int j=0;j<6;j++){
            if(i+2<6 && j+2<6){
            m = a[i][j]+a[i+1][j]+a[i+2][j]+a[i+1][j+1]+a[i][j+2]+a[i+1][j+2]+a[i+2][j+2];   }
            if(max<m)   max=m;
        }
        System.out.println(max);
    }
}
