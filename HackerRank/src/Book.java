package libraryOne;

// Need to Practice this for basic Object Oriented Programming Refresh.

public class Book {

	// Properties, Fields, Global Variables

	String title;
	int pageCount;
	int ISBN;
	boolean isCheckedOut;
	int dayCheckout = -1;

	// Constructor 

	public Book(String bookTitle, int bookPageCount, int bookISBN ) {
		this.title = bookTitle;
		this.pageCount = bookPageCount;
		this.ISBN = bookISBN;
		this.isCheckedOut = false;
	}

	// Getters

	public String getTitle(){
		return this.title;

	}

	public int getPageCount(){
		return this.pageCount;
	}

	public int getISBN(){
		return this.ISBN;
	}

	public boolean getIsCheckedOut(){
		return this.isCheckedOut;
	}

	public int getDayCheckout(){
		return this.dayCheckout;
	}

	// Setters

	public void setIsCheckout(boolean newIsCheckout, int currentDayCheckedOut ) {
		this.isCheckedOut = newIsCheckout;
		setDayCheckOut(currentDayCheckedOut);
	}

	public void setDayCheckOut(int day) {
		this.dayCheckout = day;
	} 
}
