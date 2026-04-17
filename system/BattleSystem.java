package system;
import java.util.Scanner;
import system.entity.*;
import system.exception.*;
/**
 * [2.6 Parametric Polymorphism]
 * BattleSystem<T extends Character> works for ANY Character subtype.
 * Whether T is Warrior, Mage, or Archer, the battle logic is identical.
 * This avoids writing three separate BattleSystem classes.
 *
 * The bound <T extends Character> ensures we can call Character methods
 * (getHp, attack, useSkill, etc.) on the generic type safely.
 */
public class BattleSystem<T extends system.entity.Character> {
    private final T player;
    private final Scanner sc;
    public BattleSystem(T player, Scanner sc) {
        this.player = player;
        this.sc     = sc;
    }
    /**
     * Runs a full battle between player and monster.
     * Returns true if player wins, false if player dies or runs.
     *
     * [2.3 Exception] DeadCharacterException is caught here to end battle 
cleanly.
     */
    public boolean startBattle(Monster monster) {
        printLine();
        System.out.println("    BATTLE START!");
        System.out.println("   Enemy: " + monster.getName() + " — " + 
monster.getDescription());
        printLine();
        while (player.isAlive() && monster.isAlive()) {
            // Show battle status
            printBattleStatus(monster);
            // 
            // ─── Player turn ───────────────────────────────────────────────────────────
            System.out.println("\n  YOUR TURN:");
            System.out.println("  1. Attack");
            System.out.println("  2. Use Skill" + (player.getSkillCooldown() > 0
                    ? " (cooldown: " + player.getSkillCooldown() + ")" : ""));
            System.out.println("  3. Use Item" + (player.getInventory().isEmpty() ? " (empty)" : ""));
            System.out.println("  4. Run");
            System.out.print("  > ");
            int choice = readInt(1, 4);
            String actionResult = handlePlayerAction(choice, monster);
            System.out.println(actionResult);
            if (choice == 4) return false; // ran away
            if (!monster.isAlive()) break;
            // 
            // ─── Status effects on monster ─────────────────────────────────────────────────
            String monsterTick = monster.tickStatusEffects();
            if (!monsterTick.isEmpty()) System.out.print(monsterTick);
            if (!monster.isAlive()) break;
            // 
            // ─── Monster turn ──────────────────────────────────────────────────────────────
            System.out.println("\n   " + monster.getName() + "'s turn!");
            String monsterAction;
            // Use special move when raging (HP < 50%) with 40% chance
            if (monster.isRaging() && Math.random() < 0.4) {
                monsterAction = monster.performSpecialMove(player);
            } else {
                int dmg = monster.attack(player);
                monsterAction = String.format("    %s attacks for %d damage!", 
monster.getName(), dmg);
            }
            System.out.println(monsterAction);
            // 
            // ─── Status effects on player ──────────────────────────────────────────────────
            String playerTick = player.tickStatusEffects();
            if (!playerTick.isEmpty()) System.out.print(playerTick);
            // 
            // ─── Check player death ────────────────────────────────────────────────────────
            if (!player.isAlive()) {
                // [2.3 Exception] Throw when player HP reaches 0
                throw new DeadCharacterException(player.getName());
            }
            pause();
        }
        // 
        // ─── Victory ──────────────────────────────────────────────────────────────────────
        printLine();
        System.out.println("   VICTORY! " + monster.getName() + " defeated!");
        int exp  = monster.getExpReward();
        int gold = monster.getGoldReward();
        System.out.println("   +" + exp + " EXP    +" + gold + " Gold");
        String levelMsg = player.gainExp(exp);
        if (!levelMsg.isEmpty()) System.out.println(levelMsg);
        player.setGold(player.getGold() + gold);
        printLine();
        return true;
    }
    // 
    // ─── Handle Player Actions ────────────────────────────────────────────────
    private String handlePlayerAction(int choice, Monster monster) {
        return switch (choice) {
            case 1 -> {
                int dmg = player.attack(monster);
                if (dmg < 0) yield String.format("   CRITICAL HIT! %d damage!", -dmg);
                yield String.format("   You attack for %d damage!", dmg);
            }
            case 2 -> player.useSkill(monster);
            case 3 -> useItem();
            case 4 -> {
                double runChance = 0.5 + (player.getSpeed() - 15) * 0.02;
                runChance = Math.max(0.1, Math.min(0.9, runChance));
                if (Math.random() < runChance) {
                    yield "   You successfully escaped!";
                }
                yield "   Failed to escape! The enemy blocks your path!";
            }
            default -> "   Unknown action.";
        };
    }
    /**
     * [2.3 Exception] InvalidItemException thrown if inventory empty or bad 
index.
     * [2.4 Keyboard Input] Uses Scanner to read item choice.
     */
    private String useItem() {
        var inv = player.getInventory();
        if (inv.isEmpty()) {
            throw new InvalidItemException("Your inventory is empty!");
        }
        System.out.println("\n   INVENTORY:");
        for (int i = 0; i < inv.size(); i++) {
            System.out.println("    " + (i + 1) + ". " + inv.get(i).getName()
                    + " — " + inv.get(i).getDescription());
        }
        System.out.print("  Choose item (0 to cancel) > ");
        int idx = readInt(0, inv.size());
        if (idx == 0) return "    Cancelled.";
        Item item = inv.get(idx - 1);
        item.use(player);
        player.removeItem(item);
        return "   Used " + item.getName() + "!";
    }
    // 
    // ─── UI Helpers ────────────────────────────────────────────────────────────────
    private void printBattleStatus(Monster monster) {
        System.out.println();
        System.out.println("   " + player.getName() + " (" + 
player.getClassName() + " Lv." + player.getLevel() + ")");
        System.out.println("  " + player.getStatusBar());
        System.out.println();
        System.out.println("   " + monster.getName());
        System.out.println("  HP " + monster.getHpBar());
    }
    /**
     * [2.4 Keyboard Input] Safe integer reader with range validation.
     * [2.3 Exception] Uses try-catch for NumberFormatException.
     */
    private int readInt(int min, int max) {
        while (true) {
            try {
                String line = sc.nextLine().trim();
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) return val;
                System.out.print("  Please enter " + min + "-" + max + " > ");
            } catch (NumberFormatException e) {
                System.out.print("  Numbers only! > ");
            }
        }
    }
    private void printLine() {
        System.out.println("  " + "═".repeat(50));
    }
    private void pause() {
        System.out.println("\n  [Press ENTER to continue]");
        sc.nextLine();
    }
}