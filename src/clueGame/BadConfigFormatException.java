package clueGame;

public class BadConfigFormatException extends Exception{
	
	public BadConfigFormatException() {
		super("Error: Bad configuration format");
	}
	
	public BadConfigFormatException(String message) {
		super(message);
	}
}
