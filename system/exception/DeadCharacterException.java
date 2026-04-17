package system.exception;
/**
 * [2.3 Custom Exception]
 * All custom exceptions extend RuntimeException so they don't require checked 
handling,
 * but they carry meaningful messages specific to the game domain.
 * Using custom exceptions makes error handling expressive and readable.
 */
// ─── Thrown when a dead character tries to act ───────────────────────────────
public class DeadCharacterException extends RuntimeException {
    public DeadCharacterException(String characterName) {
        super(" " + characterName + " has fallen and cannot act!");
    }
}