package system.entity;

// ── Magic Staff ───────────────────────────────────────────────────────────────
public class MagicStaff extends EquipmentItem {
    public MagicStaff() { super("Magic Staff ", 15, 120); }
    @Override
    public String getDescription() { return "ATK +15  (120 Gold)"; }
    @Override
    public void use(Character target) {
        target.setAtk(target.getAtk() + statBonus);
    }
}