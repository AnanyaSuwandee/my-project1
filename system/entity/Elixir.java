package system.entity;
// ── Elixir ────────────────────────────────────────────────────────────────────
public class Elixir extends HealingItem {
    public Elixir() { super("Elixir ", 100, 50); }
    @Override
    public String getDescription() { return "Restores 100 HP  (50 Gold)"; }
    @Override
    public void use(Character target) {
        target.heal(healAmount);
    }
}