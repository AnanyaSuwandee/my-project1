package system.entity;
/**
 * [2.2 Inheritance - Abstract Class]
 * Monster is abstract for the same reason as Character:
 * all monsters share hp/atk/def/exp/gold but each has a unique special move.
 * performSpecialMove() is abstract so Goblin, Skeleton, Orc, Dragon each 
implement it.
 */
public abstract class Monster {
    protected String name;
    protected int hp, maxHp;
    protected int atk, def;
    protected int expReward, goldReward;
    // Status effects (can be applied by player skills)
    public int burnTurns   = 0;
    public int poisonTurns = 0;
    public int defDownTurns = 0;
    public Monster(String name, int hp, int atk, int def, int exp, int gold) {
        this.name       = name;
        this.maxHp      = hp;
        this.hp         = hp;
        this.atk        = atk;
        this.def        = def;
        this.expReward  = exp;
        this.goldReward = gold;
    }
    // /*─── Abstract ──────────────────────────────────────────────────────────────
    /**
     * [2.2 Inheritance - Abstract Method]
     * Each monster type has a unique special attack used when HP < 50%.
     */
    public abstract String performSpecialMove(Character target);
    public abstract String getDescription();
    // /*─── Combat ────────────────────────────────────────────────────────────────
    public int attack(Character target) {
        int rawDmg = this.atk;
        // Rage mode: if HP < 50%, deal 50% more damage
        if (hp < maxHp / 2) rawDmg = (int)(rawDmg * 1.5);
        double variance = 0.9 + Math.random() * 0.2;
        int damage = (int)(rawDmg * variance);
        if (damage < 1) damage = 1;
        target.takeDamage(damage);
        return damage;
    }
    public void takeDamage(int dmg) {
        int actualDef = defDownTurns > 0 ? def / 2 : def;
        int net = dmg - actualDef;
        if (net < 1) net = 1;
        hp -= net;
        if (hp < 0) hp = 0;
    }
    public void takePureDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }
    public String tickStatusEffects() {
        StringBuilder sb = new StringBuilder();
        if (burnTurns > 0) {
            takePureDamage(5);
            burnTurns--;
            sb.append("   ").append(name).append(" burns for 5 damage! (").append(burnTurns).append(" turns left)\n");
        }
        if (poisonTurns > 0) {
            takePureDamage(3);
            poisonTurns--;
            sb.append("   ").append(name).append(" is poisoned for 3 damage! (").append(poisonTurns).append(" turns left)\n");
        }
        if (defDownTurns > 0) {
            defDownTurns--;
        }
        return sb.toString();
    }
    public boolean isAlive()      { return hp > 0; }
    public boolean isRaging()     { return hp < maxHp / 2; }
    public String getName()       { return name; }
    public int getHp()            { return hp; }
    public int getMaxHp()         { return maxHp; }
    public int getDef()           { return defDownTurns > 0 ? def / 2 : def; }
    public int getExpReward()     { return expReward; }
    public int getGoldReward()    { return goldReward; }
    public String getHpBar() {
        int bars = (int)((double) hp / maxHp * 20);
        String bar = "█".repeat(Math.max(0, bars)) + "░".repeat(20 - Math.max(0, 
bars));
        String rage = isRaging() ? "  RAGE!" : "";
        return String.format("[%s] %d/%d%s", bar, hp, maxHp, rage);
    }
}
