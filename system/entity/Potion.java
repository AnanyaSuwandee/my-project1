package system.entity;
// ── Potion ────────────────────────────────────────────────────────────────────
public class Potion extends HealingItem {
    public Potion() { super("Potion ", 50, 20); }
    @Override
    public String getDescription() { return "Restores 50 HP  (20 Gold)"; }
    @Override
    public void use(Character target) {
        target.heal(healAmount);
    }
}

