package system.entity;
/**
 * [2.2 Inheritance]
 * Archer extends Character and overrides useSkill() with Triple Shot:
 * fires 3 separate hits each with independent crit checks.
 * High speed means Archer almost always attacks first — unique tactical 
advantage.
 */
public class Archer extends Character {
    public Archer(String name) {
        super(name,
            110,  // HP  — middle ground
            55,   // MP
            42,   // ATK
            15,   // DEF
            25,   // SPEED — fastest class
            25    // CRIT% — highest crit chance
        );
    }
    @Override
    public String getClassName() { return "Archer"; }
    /**
     * Triple Shot: 3 individual hits, each with 25% crit chance.
     * MP cost: 20 | Cooldown: 2 turns
     */
    @Override
    public String useSkill(Monster target) {
        if (skillCooldown > 0)
            return "   Triple Shot is on cooldown! (" + this.skillCooldown + " turns left)";
        if (mp < 20)
            return "   Not enough MP! (need 20)";
        mp -= 20;
        skillCooldown = 2;
        StringBuilder sb = new StringBuilder("   TRIPLE SHOT!\n");
        int totalDamage = 0;
        for (int i = 1; i <= 3; i++) {
            int dmg = (int)(atk * 0.7);
            boolean isCrit = Math.random() * 100 < critChance;
            if (isCrit) dmg = (int)(dmg * 1.8);
            if (dmg < 1) dmg = 1;
            target.takePureDamage(dmg);
            totalDamage += dmg;
            sb.append(String.format("    Arrow %d: %d damage%s\n", i, dmg, isCrit 
? "  CRITICAL!" : ""));
        }
        sb.append(String.format("  Total: %d damage!", totalDamage));
        return sb.toString();
    }
    public String getSkillInfo() {
        return "Triple Shot — 3 hits x0.7 ATK each, 25% crit/hit (MP:20, CD:2)";
    }
}