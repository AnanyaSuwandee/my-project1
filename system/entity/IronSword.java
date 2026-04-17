package system.entity;

// ── Iron Sword ────────────────────────────────────────────────────────────────
public class IronSword extends EquipmentItem {
    public IronSword() { super("Iron Sword ", 10, 80); }
    @Override
    public String getDescription() { return "ATK +10  (80 Gold)"; }
    @Override
    public void use(Character target) {
        target.setAtk(target.getAtk() + statBonus);
    }
}