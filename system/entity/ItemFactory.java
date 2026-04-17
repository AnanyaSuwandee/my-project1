package system.entity;
// ─────────────────────────────────────────────────────────────────────────────
// HEALING ITEMS
// ─────────────────────────────────────────────────────────────────────────────
/**
 * [2.2 Interface Implementation]
 * HealingItem implements Item interface. Concrete items (Potion, Elixir, 
Antidote)
 * extend HealingItem to reuse healAmount logic while still satisfying the Item 
contract.
 * This is a good mix of interface + inheritance.
 */
abstract class HealingItem implements Item {
    protected String name;
    protected int healAmount;
    protected int price;
    public HealingItem(String name, int healAmount, int price) {
        this.name       = name;
        this.healAmount = healAmount;
        this.price      = price;
    }
    @Override public String getName()        { return name; }
    @Override public String getDescription() { return ""; }
    @Override public int    getPrice()       { return price; }
}


// ─────────────────────────────────────────────────────────────────────────────
// EQUIPMENT ITEMS
// ─────────────────────────────────────────────────────────────────────────────
/**
 * [2.2 Interface + Inheritance]
 * EquipmentItem also implements Item. When used, it permanently boosts a stat.
 * Weapon and Armor extend this to split ATK vs DEF bonuses.
 */
abstract class EquipmentItem implements Item {
    protected String name;
    protected int statBonus;
    protected int price;
    public EquipmentItem(String name, int statBonus, int price) {
        this.name      = name;
        this.statBonus = statBonus;
        this.price     = price;
    }
    @Override public String getName()  { return name; }
    @Override public int    getPrice() { return price; }
}



// ─────────────────────────────────────────────────────────────────────────────
// ITEM FACTORY
// ─────────────────────────────────────────────────────────────────────────────
public class ItemFactory {
    /**
     * [2.5 Collection with Generics]
     * Returns a new Item instance by shop index.
     */
    public static Item create(int index) {
        return switch (index) {
            case 1 -> new Potion();
            case 2 -> new Elixir();
            case 3 -> new Antidote();
            case 4 -> new MpPotion();
            case 5 -> new IronSword();
            case 6 -> new MagicStaff();
            case 7 -> new LeatherArmor();
            default -> null;
        };
    }
    public static void printShopMenu() {
        System.out.println("  1. Potion       — Restores 50 HP       (20 Gold)");
        System.out.println("  2. Elixir       — Restores 100 HP      (50 Gold)");
        System.out.println("  3. Antidote     — Cures Poison         (15 Gold)");
        System.out.println("  4. MP Potion    — Restores 30 MP       (25 Gold)");
        System.out.println("  5. Iron Sword   — ATK +10              (80 Gold)");
        System.out.println("  6. Magic Staff  — ATK +15             (120 Gold)");
        System.out.println("  7. Leather Armor— DEF +8              (100 Gold)");
    }
}
