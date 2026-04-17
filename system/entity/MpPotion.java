package system.entity;
// ── MP Potion ─────────────────────────────────────────────────────────────────
public class MpPotion extends HealingItem {
    public MpPotion() { super("MP Potion ", 30, 25); }
    @Override
    public String getDescription() { return "Restores 30 MP  (25 Gold)"; }
    @Override
    public void use(Character target) {
        target.restoreMp(30);
    }
}