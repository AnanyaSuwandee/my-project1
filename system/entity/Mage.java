package system.entity;
/**
 * [2.2 Inheritance]
 * Mage extends Character and overrides useSkill() with Fireball:
 * highest burst damage + applies Burn status (5 damage/turn for 3 turns).
 * Fragile but devastating — shows how subclasses can have totally different 
playstyles.
 */
public class Mage extends Character {
    public Mage(String name) {
        super(name,
            80,   // HP  — lowest HP
            80,   // MP  — highest MP
            55,   // ATK — highest damage
            8,    // DEF — almost no defense
            15,   // SPEED
            10    // CRIT%
        );
    }
    @Override
    public String getClassName() { return "Mage"; }
    /**
     * Fireball: massive damage + Burn (5 dmg/turn for 3 turns).
     * MP cost: 25 | Cooldown: 3 turns
     */
    @Override
    public String useSkill(Monster target) {
        if (skillCooldown > 0)
            return "   Fireball is on cooldown! (" + skillCooldown + " turns left)";
        if (mp < 25)
            return "  Not enough MP! (need 25)";
        mp -= 25;
        skillCooldown = 3;
        int damage = (int)(atk * 1.8);
        if (damage < 1) damage = 1;
        target.takePureDamage(damage);
        target.burnTurns = 3;
        return String.format(
            "   FIREBALL! Deals %d damage and sets %s on fire for 3 turns!",
            damage, target.getName()
        );
    }
    public String getSkillInfo() {
        return "Fireball — 1.8x damage + Burn 3 turns (MP:25, CD:3)";
    }
}