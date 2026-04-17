package system;
import system.entity.Character;
/**
 * [2.2 Interface]
 * Saveable defines the contract for any class that persists Character data.
 * FileManager implements this interface, meaning we could swap to a different
 * persistence strategy (e.g., JSON, database) by just providing another Saveable.
 */
public interface Saveable {
    void save(Character c);
    Character load();
}