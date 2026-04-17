package system.exception;
/** [2.3 Custom Exception] Thrown when player doesn't have enough gold. */
public class InsufficientGoldException extends RuntimeException {
    public InsufficientGoldException(int have, int need) {
        super(" Not enough gold! You have " + have + " but need " + need + ".");
    }
}
