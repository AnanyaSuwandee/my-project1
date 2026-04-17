package system.entity;

// ── Leather Armor ─────────────────────────────────────────────────────────────
public class LeatherArmor extends EquipmentItem {
    public LeatherArmor() { super("Leather Armor ", 8, 100); }
    @Override
    public String getDescription() { return "DEF +8  (100 Gold)"; }
    @Override
    public void use(Character target) {
        target.setDef(target.getDef() + statBonus);
    }
}