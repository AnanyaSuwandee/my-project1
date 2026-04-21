package system;

import java.io.*;
import java.util.*;
import system.entity.*;
/**
 * [2.1 Read/Write from File]
 * FileManager handles all file I/O:
 *  - saveGame()  → writes character state to save.txt
 *  - loadGame()  → reads save.txt and restores state
 *  - saveLeaderboard() / loadLeaderboard() → leaderboard.txt
 *
 * Using text files (not binary) makes saves human-readable and easy to debug.
 *
 * [2.2 Interface — Saveable]
 * FileManager implements Saveable interface, enforcing that any persistence class
 * must provide save() and load() methods.
 */
public class FileManager implements Saveable {
    private static final String SAVE_FILE        = "save.txt";
    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    // 
/*─── Save ─────────────────────────────────────────────────────────────────*/
    @Override
    public void save(system.entity.Character c) { // เขียนแบบนี้เพื่อเจาะจงว่าเป็น Character ของเรา
        if (c==null) return;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            bw.write("name="      + c.getName());      bw.newLine();
            bw.write("class="     + c.getClassName()); bw.newLine();
            bw.write("level="     + c.getLevel());     bw.newLine();
            bw.write("exp="       + c.getExp());       bw.newLine();
            bw.write("hp="        + c.getHp());        bw.newLine();
            bw.write("maxHp="     + c.getMaxHp());     bw.newLine();
            bw.write("mp="        + c.getMp());        bw.newLine();
            bw.write("maxMp="     + c.getMaxMp());     bw.newLine();
            bw.write("atk="       + c.getAtk());       bw.newLine();
            bw.write("def="       + c.getDef());       bw.newLine();
            bw.write("speed="     + c.getSpeed());     bw.newLine();
            bw.write("gold="      + c.getGold());      bw.newLine();
            bw.write("stage="     + c.getStage());     bw.newLine();
            // Inventory: comma-separated item class names
            StringBuilder inv = new StringBuilder("inventory=");
            for (int i = 0; i < c.getInventory().size(); i++) {
                if (i > 0) inv.append(",");
                inv.append(c.getInventory().get(i).getClass().getSimpleName());
            }
            bw.write(inv.toString());
            bw.newLine();
            System.out.println("  Game saved successfully!");
        } catch (IOException e) {
            System.out.println("    Could not save game: " + e.getMessage());
        }
    }
    // 
/*─── Load ─────────────────────────────────────────────────────────────────*/
    @Override
    public system.entity.Character load() {
        File f = new File(SAVE_FILE);
        if (!f.exists()) return null;

        Map<String, String> data = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) 
                    data.put(parts[0].trim(), parts[1].trim());
            }
        } catch (IOException e) {
            System.out.println("    Could not load save: " + e.getMessage());
            return null;
        }
        // Reconstruct character from saved class name
        String name      = data.getOrDefault("name",  "Hero");
        String className = data.getOrDefault("class", "Warrior");
        
        system.entity.Character c = switch (className) {
            case "Mage"   -> new Mage(name);
            case "Archer" -> new Archer(name);
            default       -> new Warrior(name);
        };
        // Restore stats
        c.setLevel(parseInt(data, "level", 1));
        c.setExp  (parseInt(data, "exp",   0));
        c.setMaxHp(parseInt(data, "maxHp", c.getMaxHp()));
        c.setHp   (parseInt(data, "hp",    c.getMaxHp()));
        c.setMaxMp(parseInt(data, "maxMp", c.getMaxMp()));
        c.setMp   (parseInt(data, "mp",    c.getMaxMp()));
        c.setAtk  (parseInt(data, "atk",   c.getAtk()));
        c.setDef  (parseInt(data, "def",   c.getDef()));
        c.setSpeed(parseInt(data, "speed", c.getSpeed()));
        c.setGold (parseInt(data, "gold",  50));
        c.setStage(parseInt(data, "stage", 1));
        // Restore inventory
        String invStr = data.getOrDefault("inventory", "");
        if (!invStr.isEmpty()) {
            for (String itemName : invStr.split(",")) {
                Item item = createItemByName(itemName.trim());
                if (item != null) c.addItem(item);
            }
        }
        System.out.println("Save loaded! Welcome back, " + name + "!");
        return c;
    }
    // 
 
/*─── Leaderboard ──────────────────────────────────────────────────────────
    /**
     * [2.1 Read/Write File] + [2.5 Collection with Generics]
     * Leaderboard is stored as text, loaded as List<String> for display.
     */
    public void saveLeaderboard(String name, String className, int level, int 
stage) {
        List<String[]> entries = loadLeaderboardRaw();
        entries.add(new String[]{name, className, String.valueOf(level), 
String.valueOf(stage)});
        // Sort by stage desc, then level desc
        entries.sort((a, b) -> {
            int s = Integer.compare(Integer.parseInt(b[3]), 
Integer.parseInt(a[3]));
            if (s != 0) return s;
            return Integer.compare(Integer.parseInt(b[2]), 
Integer.parseInt(a[2]));
        });
        // Keep top 10
        if (entries.size() > 10) entries = entries.subList(0, 10);
        try (BufferedWriter bw = new BufferedWriter(new 
FileWriter(LEADERBOARD_FILE))) {
            for (String[] e : entries) {
                bw.write(String.join(",", e));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(" Could not save leaderboard.");  
        }
    }
    public void printLeaderboard() {
        List<String[]> entries = loadLeaderboardRaw();
        System.out.println("   ╔══════════════════════════════════════════════╗");  
        System.out.println("   ║                LEADERBOARD                   ║");  
        System.out.println("   ╠══════════════════════════════════════════════╣");  

        if (entries.isEmpty()) {
            System.out.println("   ║     No records yet. Be the first!           ║");
        } else {    
            for (int i = 0; i < entries.size(); i++) {
                String[] e = entries.get(i);
                System.out.printf("   ║  #%-2d  %-12s %-8s Lv.%-3s Stage %-3s ║%n", i + 1, e[0], e[1], e[2], e[3]);
            }
        }
        System.out.println("   ╚══════════════════════════════════════════════╝");
    }
    private List<String[]> loadLeaderboardRaw() {
        List<String[]> list = new ArrayList<>();
        File f = new File(LEADERBOARD_FILE);
        if (!f.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) list.add(parts);
            }
        } catch (IOException ignored) {}
        return list;
    }
    public boolean hasSaveFile() {
        return new File(SAVE_FILE).exists();
    }
    // 
/*─── Helpers ──────────────────────────────────────────────────────────────*/
    private int parseInt(Map<String, String> map, String key, int def) {
    try { 
        String value = map.get(key);
        if (value == null) return def;
        return Integer.parseInt(value); 
    } catch (NumberFormatException e) {
        return def; 
    }
}
    private Item createItemByName(String name) {
        return switch (name) {
            case "Potion"       -> new Potion();
            case "Elixir"       -> new Elixir();
            case "Antidote"     -> new Antidote();
            case "MpPotion"     -> new MpPotion();
            case "IronSword"    -> new IronSword();
            case "MagicStaff"   -> new MagicStaff();
            case "LeatherArmor" -> new LeatherArmor();
            default             -> null;
        };
    }
}