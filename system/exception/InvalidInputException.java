package system.exception;
/** [2.3 Custom Exception] Thrown when user enters an out-of-range or non-numeric 
menu choice. */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String input) {
        super(" Invalid input: '" + input + "'. Please enter a valid number.");
    }
}