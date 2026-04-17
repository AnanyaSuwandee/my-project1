package system;
import java.util.ArrayList;
import java.util.List;
/**
 * [2.5 Collection with Generics]
 * [2.6 Parametric Polymorphism]
 * Inventory<T> is a generic container that can hold any type T.
 * In practice we use Inventory<Item>, but the class works for any object.
 * This demonstrates parametric polymorphism — one class, many possible types.
 *
 * Why generics here? Because an inventory system is naturally type-agnostic.
 * We want type safety at compile time (no casting needed) while reusing the same 
logic.
 */
public class Inventory<T> {
    private final List<T> items = new ArrayList<>();
    private final int maxSize;
    
    public Inventory(int maxSize) {
        this.maxSize = maxSize;
    }
    public boolean add(T item) {
        if (items.size() >= maxSize) return false;
        items.add(item);
        return true;
    }

    public boolean remove(T item) {
        return items.remove(item);
    }
        
    public T get(int index) {
        if (index < 0 || index >= items.size()) 
        {return null;}
        return items.get(index);
    }
    public List<T> getAll() {
        return new ArrayList<>(items);
    }
    public int size()      { return items.size(); }
    public boolean isEmpty() { return items.isEmpty(); }
    public boolean isFull()  { return items.size() >= maxSize; }
    public void clear() { items.clear(); }
}
