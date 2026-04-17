package system.entity;

import java.util.ArrayList;
import java.util.List;
/**
 * [2.2 Inheritance - Abstract Class]
 * Character is abstract because every playable character shares base attributes
 * (hp, atk, def, speed, level, exp, gold) and common behaviors (takeDamage, heal, 
levelUp).
 * However, useSkill() MUST differ per subclass, so it is declared abstract here
 * and overridden in Warrior, Mage, and Archer.
 *
 * [2.5 Collection with Generics]
 * inventory uses List<Item> so we can store any Item subtype with type safety.
 */
public abstract class Character {
    protected String name;
    protected int maxHp, hp;
    protected int maxMp, mp;
    protected int atk, def, speed;
    protected int level, exp, gold;
    protected int critChance; // percentage 0-100
    protected int stage;
    // [2.5 Collection with Generics] - type-safe inventory
    protected List<Item> inventory = new ArrayList<>();
    // Status effects
    protected int burnTurns    = 0;
    protected int poisonTurns  = 0;
    protected int defDownTurns = 0;
    // Skill cooldown
    protected int skillCooldown = 0;
    public Character(String name, int hp, int mp, int atk, int def, int speed, int 
critChance) {
        this.name       = name;
        this.maxHp      = hp;
        this.hp         = hp;
        this.maxMp      = mp;
        this.mp         = mp;
        this.atk        = atk;
        this.def        = def;
        this.speed      = speed;
        this.critChance = critChance;
        this.level      = 1;
        this.exp        = 0;
        this.gold       = 50;
        this.stage      = 1;
    }
    // 
/*─── Abstract method ───────────────────────────────────────────────────────*/
    /**
     * [2.2 Inheritance - Abstract Method]
     * Each class has a unique skill: Warrior→ShieldBash, Mage→Fireball, 
Archer→TripleShot.
     * Declaring abstract forces every subclass to provide its own implementation.
     */
    public abstract String useSkill(Monster target);
    public abstract String getClassName();
    // 
/*─── Combat ────────────────────────────────────────────────────────────────*/
    public int attack(Monster target) {
        int rawDamage = this.atk - target.getDef();
        if (rawDamage < 1) rawDamage = 1;
        // Random variance ±10%
        double variance = 0.9 + Math.random() * 0.2;
        int damage = (int)(rawDamage * variance);
        // Critical hit
        if (Math.random() * 100 < critChance) {
            damage = (int)(damage * 1.8);
            target.takeDamage(damage);
            return -damage; // negative = crit signal
        }
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
    public void heal(int amount) {
        hp += amount;
        if (hp > maxHp) hp = maxHp;
    }
    public void restoreMp(int amount) {
        mp += amount;
        if (mp > maxMp) mp = maxMp;
    }
    // /*─── Status effects tick ───────────────────────────────────────────────────*  
    public String tickStatusEffects() {
        StringBuilder sb = new StringBuilder();
        if (burnTurns > 0) {
            takePureDamage(5);
            burnTurns--;
            sb.append("   ").append(name).append(" takes 5 burn damage! (").append(burnTurns).append(" turns left)\n");
        }
        if (poisonTurns > 0) {
            takePureDamage(3);
            poisonTurns--;
            sb.append("  ").append(name).append(" takes 3 poison damage! (").append(poisonTurns).append(" turns left)\n");
        }
        if (defDownTurns > 0) {
            defDownTurns--;
            sb.append("  ").append(name).append(" DEF is reduced! (").append(defDownTurns).append(" turns left)\n");
        }
        if (skillCooldown > 0) skillCooldown--;
        return sb.toString();
    }
    // 
/*─── Level Up ──────────────────────────────────────────────────────────────*/
    public String gainExp(int amount) {
        exp += amount;
        int required = getExpRequired();
        if (exp >= required) {
            exp -= required;
            level++;
            maxHp  += 20 + level * 3;
            hp      = maxHp;
            maxMp  += 10;
            mp      = maxMp;
            atk    += 5 + level;
            def    += 3;
            speed  += 1;
            return "   LEVEL UP! Now Level " + level + "! All stats increased!\n";
        }
        return "";
    }
    public int getExpRequired() {
        return 100 + (level - 1) * 75;
    }
    // 
/*─── Inventory ─────────────────────────────────────────────────────────────*/ 
    public void addItem(Item item) {
        inventory.add(item);
    }
    public boolean removeItem(Item item) {
        return inventory.remove(item);
    }
    public List<Item> getInventory() {
        return inventory;
    }
    // 
/*─── Getters / Setters ─────────────────────────────────────────────────────*/
    public String getName()    { return name; }
    public int getHp()         { return hp; }
    public int getMaxHp()      { return maxHp; }
    public int getMp()         { return mp; }
    public int getMaxMp()      { return maxMp; }
    public int getAtk()        { return atk; }
    public int getDef()        { return def; }
    public int getSpeed()      { return speed; }
    public int getLevel()      { return level; }
    public int getExp()        { return exp; }
    public int getGold()       { return gold; }
    public int getStage()      { return stage; }
    public int getSkillCooldown() { return skillCooldown; }
    public boolean isAlive()   { return hp > 0; }
    public void setGold(int gold)   { this.gold = gold; }
    public void setStage(int stage) { this.stage = stage; }
    public void setHp(int hp)       { this.hp = hp; }
    public void setMaxHp(int v)     { this.maxHp = v; }
    public void setMp(int mp)       { this.mp = mp; }
    public void setMaxMp(int v)     { this.maxMp = v; }
    public void setAtk(int atk)     { this.atk = atk; }
    public void setDef(int def)     { this.def = def; }
    public void setSpeed(int v)     { this.speed = v; }
    public void setLevel(int v)     { this.level = v; }
    public void setExp(int v)       { this.exp = v; }
    public void setCritChance(int v){ this.critChance = v; }
    public String getStatusBar() {
        String effects = "";
        if (burnTurns > 0)    effects += " ";
        if (poisonTurns > 0)  effects += " ";
        if (defDownTurns > 0) effects += " ↓";
        return String.format("HP:%d/%d  MP:%d/%d  %s", hp, maxHp, mp, maxMp, 
effects);
    }
    public String getFullStats() {
        return String.format(
            "  Name   : %s (%s)\n" +
            "  Level  : %d  (EXP: %d / %d)\n" +
            "  HP     : %d / %d\n" +
            "  MP     : %d / %d\n" +
            "  ATK    : %d\n" +
            "  DEF    : %d\n" +
            "  SPEED  : %d\n" +
            "  CRIT   : %d%%\n" +
            "  GOLD   : %d\n" +
            "  STAGE  : %d",
            name, getClassName(), level, exp, getExpRequired(),
            hp, maxHp, mp, maxMp, atk, def, speed, critChance, gold, stage
        );
    }
}
