package system.entity;
/**
 * [2.2 Inheritance]
 * Warrior extends Character and overrides useSkill() with Shield Bash:
 * deals bonus damage AND applies DEF Down to the monster for 2 turns.
 * This demonstrates polymorphism — same method name, completely different 
behavior.
 */
public class Warrior extends Character {
    public Warrior(String name) {
        super(name,
            150,  // HP  — tankiest class
            40,   // MP
            30,   // ATK
            25,   // DEF — highest defense
            10,   // SPEED — slowest
            5     // CRIT% — lowest
        );
    }
    @Override
    public String getClassName() { return "Warrior"; }
    /**
     * Shield Bash: moderate damage + reduces monster DEF for 2 turns.
     * MP cost: 15 | Cooldown: 3 turns
     */
    @Override
    public String useSkill(Monster target) {
        if (skillCooldown > 0)
            return "   Shield Bash is on cooldown! (" + skillCooldown + " turns left)";
        if (mp < 15)
            return "   Not enough MP! (need 15)";
        mp -= 15;
        skillCooldown = 3;
        int damage = (int)(atk * 1.4);
        if (damage < 1) damage = 1;
        target.takePureDamage(damage);
        target.defDownTurns = 2;
        return String.format(
            "    SHIELD BASH! Deals %d damage and reduces %s DEF for 2 turns!",
            damage, target.getName()
        );
    }
    public String getSkillInfo() {
        return "Shield Bash — 1.4x damage + DEF Down 2 turns (MP:15, CD:3)";
    }
}