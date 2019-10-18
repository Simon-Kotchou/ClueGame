package clueGame;

public class BadConfigFormatException extends Exception{
	
	public BadConfigFormatException() {								//default constructor
		super("Error: Bad configuration format");
	}
	
	public BadConfigFormatException(String message) {				//parameterized constructor allows for message to be sent
		super(message);
	}
}
