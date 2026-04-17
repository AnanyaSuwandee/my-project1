package system.entity;
/**
 * [2.2 Interface]
 * Item is defined as an interface because different item types (healing, weapon, 
armor)
 * share common behavior (use, getName, getPrice) but have completely different 
implementations.
 * Using an interface enforces a contract without forcing unwanted inheritance.
 */
public interface Item {
    String getName();
    String getDescription();
    int getPrice();
    void use(Character target);
}