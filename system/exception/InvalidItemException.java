package system.exception;
/** [2.3 Custom Exception] Thrown when player tries to use an item not in 
inventory. */
public class InvalidItemException extends RuntimeException {
    public InvalidItemException(String msg) {
        super(" Invalid item: " + msg);
    }
}