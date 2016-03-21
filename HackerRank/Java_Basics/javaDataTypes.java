import java.util.*;
import java.math.*;
class javaDataTypes {

    public static void main (String[] args) {

        Scanner input = new Scanner (System.in);
        int t = input.nextInt();
       
        while (t > 0) {
         
            long x = input.nextInt();
            
            try {
                
                System.out.println(x+" can be fitted in:");
                if(x>=-128 && x <=127) {
                    System.out.println("* byte");
                }
                if (x >=-32768 && x <=32767) {
                    System.out.println("* short");
                }
                if (x>=-2147483648 && x<=2147483647) { // How did one find the lenght of Long ?
                    System.out.println("* int");
                }
                if  (x >=-9223372036854775808L && x <=9223372036854775807L) {
                    System.out.println("* long\n");
                }
            }
            catch(Exception e){
                System.out.println(x+" can't be fitted anywhere");
            }
            t--;
        }
    }
} 
