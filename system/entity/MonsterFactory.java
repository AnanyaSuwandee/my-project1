package system.entity;
// ─────────────────────────────────────────────────────────────────────────────
// GOBLIN  (Stage 1-2)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * [2.2 Inheritance] Goblin extends Monster.
 * Special: Dirty Trick — applies Poison to the player.
 */
class Goblin extends Monster {
    public Goblin() {
        super("Goblin", 50, 15, 5, 30, 10);
    }
    @Override
    public String performSpecialMove(Character target) {
        target.poisonTurns = 3;
        int dmg = (int)(atk * 1.2);
        target.takeDamage(dmg);
        return String.format("    Goblin uses DIRTY TRICK! Deals %d damage and poisons %s for 3 turns!", dmg, target.getName());
    }
    @Override
    public String getDescription() { return "A sneaky goblin. Watch out for poison!"; }
}
// ─────────────────────────────────────────────────────────────────────────────
// SKELETON  (Stage 2-3)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * [2.2 Inheritance] Skeleton extends Monster.
 * Special: Bone Crush — heavy physical damage ignoring DEF.
 */
class Skeleton extends Monster {
    public Skeleton() {
        super("Skeleton", 70, 22, 10, 50, 20);
    }
    @Override
    public String performSpecialMove(Character target) {
        int dmg = (int)(atk * 1.6);
        target.takePureDamage(dmg); // ignores DEF
        return String.format("   Skeleton uses BONE CRUSH! Deals %d PURE damage (ignores DEF)!", dmg);
    }
    @Override
    public String getDescription() { return "An undead warrior. Its bone crush ignores your armor."; }
}
// ─────────────────────────────────────────────────────────────────────────────
// ORC  (Stage 3-4)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * [2.2 Inheritance] Orc extends Monster.
 * Special: Battle Roar — buffs own ATK and attacks.
 */
class Orc extends Monster {
    public Orc() {
        super("Orc", 120, 35, 20, 80, 35);
    }
    @Override
    public String performSpecialMove(Character target) {
        atk = (int)(atk * 1.3); // permanent ATK buff for this battle
        int dmg = atk;
        target.takeDamage(dmg);
        return String.format("   Orc uses BATTLE ROAR! ATK increased permanently and deals %d damage!", dmg);
    }
    @Override
    public String getDescription() { return "A brutal orc warrior. It grows stronger as it fights."; }
}
// ─────────────────────────────────────────────────────────────────────────────
// DRAGON  (Stage 5 — Boss)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * [2.2 Inheritance] Dragon extends Monster — final boss.
 * Special: randomly uses Fire Breath (burn) or Tail Swipe (massive damage).
 */
class Dragon extends Monster {
    public Dragon() {
        super("Dragon ", 300, 60, 30, 200, 100);
    }
    @Override
    public String performSpecialMove(Character target) {
        if (Math.random() < 0.5) {
            // Fire Breath
            int dmg = (int)(atk * 1.5);
            target.takePureDamage(dmg);
            target.burnTurns = 3;
        return String.format("Dragon uses FIRE BREATH! %d damage + Burn 3 turns!", dmg);
        } else {
            // Tail Swipe
            int dmg = (int)(atk * 2.0);
            target.takeDamage(dmg);
            return String.format("    Dragon uses TAIL SWIPE! Massive %d damage!", dmg);
        }
    }
    @Override
    public String getDescription() { return "The final boss. A fearsome dragon with devastating fire."; }
}
// ─────────────────────────────────────────────────────────────────────────────
// FACTORY — used by BattleSystem to spawn monsters per stage
// ─────────────────────────────────────────────────────────────────────────────
/**
 * MonsterFactory creates the correct monster for a given stage.
 * Centralizes monster creation so BattleSystem doesn't need to know subclass 
names.
 */
public class MonsterFactory {
    /**
     * [2.5 Collection with Generics] — returns List<Monster> for the stage
     */
    public static Monster spawnForStage(int stage) {
        return switch (stage) {
            case 1 -> new Goblin();
            case 2 -> Math.random() < 0.5 ? new Goblin() : new Skeleton();
            case 3 -> new Skeleton();
            case 4 -> new Orc();
            case 5 -> new Dragon();
            default -> new Orc(); // repeat after stage 5
        };
    }
    public static String stageDescription(int stage) {
        return switch (stage) {
            case 1 -> "The Dark Forest — Goblins lurk in the shadows";
            case 2 -> "Haunted Ruins — Undead roam freely";
            case 3 -> "Cursed Catacombs — Skeletons guard ancient treasures";
            case 4 -> "Orc Stronghold — Brutes patrol the halls";
            case 5 -> "Dragon's Lair — THE FINAL BOSS AWAITS";
            default -> "Endless Dungeon — Stage " + stage;
        };
    }
}