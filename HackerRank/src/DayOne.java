import java.util.*;
import java.io.*;

public class DayOne { //DataTypesPractice

	Double one = Double.valueOf(5.35);
	Character two = Character.valueOf('a');
	Boolean three = Boolean.valueOf(false);
	Integer four = Integer.valueOf(100);
	String five = "I am a code monkey";
	Boolean six =Boolean.valueOf(true);
	Double seven =Double.valueOf(17.3);
	Character eight =Character.valueOf('c');
	String nine = "derp";

	public void printOne() {
		//System.out.println("Primitive : "+number.getClass().getSimpleName());
		System.out.println("Primitive : double");//one.getClass().getSimpleName());
		System.out.println("Primitive : char");//+two.getClass().getSimpleName());
		System.out.println("Primitive : boolean");//+three.getClass().getSimpleName());
		System.out.println("Primitive : int");//+four.getClass().getSimpleName());
		System.out.println("Reference : string");//+five.getClass().getSimpleName());
		System.out.println("Primitive : boolean");//+six.getClass().getSimpleName());
		System.out.println("Primitive : double");//+seven.getClass().getSimpleName());
		System.out.println("Primitive : char");//+eight.getClass().getSimpleName());
		System.out.println("Reference : string");//+nine.getClass().getSimpleName());
	}
	public static void main (String[] args) {

		DayOne test = new DayOne();
		test.printOne();


	}
}
