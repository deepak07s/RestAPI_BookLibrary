package Library;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;
import io.restassured.RestAssured;


public class Book {
	
	String createURL = "/Library/Addbook.php";
    String getURL="/Library/GetBook.php?ID=%s";
    String getURLAuthorName="/Library/GetBook.php?AuthorName=%s";
    String deleteUrl = "/Library/DeleteBook.php";
    
    AddBookRequest myBook;
    AddBookResponse bookResponse;
	
    //Add the Book
	@Test(priority=1)
	public void AddBook() {
		RestAssured.baseURI = "http://216.10.245.166";

        myBook=new AddBookRequest();
        
        myBook.setName("Automation with Perl");
        myBook.setIsbn("5593");
        myBook.setAisle("2293");
        myBook.setAuthor("MarkBB");
        
        bookResponse=new AddBookResponse();
        
        //Post
        bookResponse= given().log().all().header("Content-Type", "application/json").body(myBook)
                .when().post(createURL)
                .then().log().all().extract().as(AddBookResponse.class);
        
	}
	
	//Get the Book using ID
	@Test(priority=2)
	public void getDetailsUsingID()
	{
		RestAssured.baseURI = "http://216.10.245.166";
		
		String ID=bookResponse.getID();
		System.out.println("ID is"+ID);
		String getURLID=String.format(getURL, ID);
		
		//GetBookIDResponse getBookIDResponse=new GetBookIDResponse();
		
		GetBookIDResponse[] getBookIDResponse=given().log().all().header("Content-Type", "application/json")
	            .when().get(getURLID)
	            .then().log().all().extract().as(GetBookIDResponse[].class);
		System.out.println("Result of GetBook by ID is");
		for(GetBookIDResponse itr : getBookIDResponse) {
			System.out.println("Book Name is : "+itr.getBook_name());
		}
	}
	
	//Get the Book using AuthornameName
	@Test(priority=3)
	public void getDetailsUsingAuthorname() {
		RestAssured.baseURI = "http://216.10.245.166";
		
		String authorName=myBook.getAuthor();
		
		String getURLAuthor=String.format(getURLAuthorName, authorName);
		String responseAuthor=given().log().all().header("Content-Type", "application/json")
	            .when().get(getURLAuthor)
	            .then().log().all().extract().body().asString();
	}
	
	//Delete the Book
	@Test(priority=4)
	public void deleteBook() {
		RestAssured.baseURI = "http://216.10.245.166";
		
		String deleteID=bookResponse.getID();
		
		DeleteBookRequest ID=new DeleteBookRequest();
		
		ID.setID(deleteID);
		
		String resDel=given().log().all().header("Content-Type", "application/json").body(ID)
	            .when().post(deleteUrl)
	            .then().log().all().extract().body().asString();
	}

	
}
