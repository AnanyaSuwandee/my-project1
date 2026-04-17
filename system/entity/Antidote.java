package system.entity;

// ── Antidote ──────────────────────────────────────────────────────────────────
public class Antidote extends HealingItem {
    public Antidote() { super("Antidote ", 0, 15); }
    @Override
    public String getDescription() { return "Cures Poison status  (15 Gold)"; }
    @Override
    public void use(Character target) {
        target.poisonTurns = 0;
        target.heal(10);
    }
}