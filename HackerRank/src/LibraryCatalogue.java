package libraryOne;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.SetOfIntegerSyntax;

public class LibraryCatalogue {


	// Properties / Fields / Global Variables

	// Book Collection
	Map <String,Book> bookCollection = new HashMap<String,Book>(); 
	// Current Day
	int currentDay = 0;
	// Length of Checkout Period
	int lenghtOfCheckOut = 7;
	// Initial late fee
	double initialLateFee = 0.50;
	// Per day late fee
	double perDayLateFee = 1.00;

	// Constructor

	public LibraryCatalogue(Map<String,Book> collection) {

		this.bookCollection = collection;
	}

	public LibraryCatalogue(Map<String,Book> collection, int lenghtofCheckOut, double initialLateFee, double perDayLateFee) {

		this.bookCollection = collection;
		this.lenghtOfCheckOut = lenghtofCheckOut;
		this.initialLateFee = initialLateFee;
		this.perDayLateFee = perDayLateFee;
	}

	// Getters 

	public Map<String,Book> getBookCollection() {
		return this.bookCollection;
	}

	public Book getbook (String bookTitle) {
		return getBookCollection().get(bookTitle);
	}

	public int getCurrentDay() {
		return this.currentDay;
	}

	public int getLengthOfCheckOut(){
		return this.lenghtOfCheckOut;
	}

	public double getinitialLateFee() {
		return this.initialLateFee;
	}

	public double getperDayLateFee() {
		return this.perDayLateFee;
	}

	// Setters
	public void nextDay() {
		currentDay++;
	}

	public void setDay(int day) {
		currentDay = day;
	}

	// Instance Methods

	public void checkOutBook(String title) {
		Book book = getbook(title);
		if(book.getIsCheckedOut()) {
			sorryThatBookIsOut(book);
		}
		else {
			book.setIsCheckout(true, currentDay);
			System.out.println(title+" is now checked out");
			System.out.println("Due date is"+getCurrentDay()+lenghtOfCheckOut); // lenghtOfCheckOutPeriod  ??
			// I want to have user list - of the people who checked out.
		}
	}

	public void returnBook(String title) {
		Book book = getbook(title); 
		int daysLate = currentDay - (book.getDayCheckout() + getLengthOfCheckOut());
		if(daysLate > 0) {
			System.out.println("You owe "+getinitialLateFee() + daysLate * getperDayLateFee());
			System.out.println("No of Days Late"+daysLate);
		}
		else {
			System.out.println("Thank you");
		}
		book.setIsCheckout(false, -1);
	}

	public void sorryThatBookIsOut(Book book) {
		System.out.println("The book "+book.getTitle()+" is not available. Please come back on"+(book.getDayCheckout() + getLengthOfCheckOut())+".");
	}

	public static void main(String[] args) {

		Map<String,Book> bookCollection = new HashMap <String,Book>();
		Book harry = new Book("Harry Potter",600,1234567);
		bookCollection.put("Harry Potter", harry);
		LibraryCatalogue lib = new LibraryCatalogue(bookCollection);
		lib.checkOutBook("Harry Potter");
		lib.nextDay();
		lib.nextDay();
		lib.checkOutBook("Harry Potter");
		lib.setDay(20);
		lib.returnBook("Harry Potter");
		lib.checkOutBook("Harry Potter");
	}

}
