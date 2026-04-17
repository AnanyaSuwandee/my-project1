package system;

import java.util.Scanner;
import system.entity.*;
import system.entity.Character; // ระบุชัดเจนเพื่อไม่ให้สับสนกับ java.lang.Character
import system.exception.*;

/*╔══════════════════════════════════════════════════════════════════╗
    ║
               RPG BATTLE SYSTEM — Full Source Code                  
    ║
               Java OOP Project — All 6 Criteria Covered             
    ║
    ║
    ╠══════════════════════════════════════════════════════════════════╣
    ║  2.1 File I/O          
    → FileManager.java                       
    ║
    ║  2.2 Inheritance/Interface → Character, Monster, Item, Saveable ║
    ║  2.3 Custom Exception  → 4 custom exception classes             
    ║
    ║  2.4 Keyboard Input    
    → Scanner in Main, BattleSystem, Shop    
    ║  2.5 Collection+Generics → List<Item>, Inventory<T>             
    ║  2.6 Parametric Poly   
    → BattleSystem<T extends Character>      
    ║
    ║
    ║
    ╠══════════════════════════════════════════════════════════════════╣
    ║  HOW TO COMPILE & RUN:                                          
    ║
    ║  1. Create folders: entity/ system/ exception/                  
    ║  2. Place each file in its matching folder                      
    ║
    ║
    ║  3. javac -d out entity/*.java system/*.java exception/*.java Main.java ║
    ║  4. java -cp out Main                                           
    ║
    ╚══════════════════════════════════════════════════════════════════╝
    ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    FILE: Main.java
    ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    import entity.*;
    import exception.*;
    import system.*;
    import java.util.Scanner;
    /**
     * 
    ═══════════════════════════════════════════════════
     *   RPG BATTLE SYSTEM — Main Entry Point
     *   Demonstrates all 6 OOP criteria in one system.
     * 
    ═══════════════════════════════════════════════════
     *
     * Criteria checklist:
     *  2.1 File I/O           
    → FileManager (save.txt, leaderboard.txt)
     *  2.2 Inheritance/Interface → Character→Warrior/Mage/Archer, Monster→subclasses,
     *                              Item interface, Saveable interface
     *  2.3 Custom Exception   
    → DeadCharacterException, InvalidItemException,
     *                           InsufficientGoldException, InvalidInputException
     *  2.4 Keyboard Input     
    → Scanner throughout menus and battles
     *  2.5 Collection+Generics → List<Item>, List<Monster>, Inventory<T>
     *  2.6 Parametric Poly    
    → BattleSystem<T extends Character>, Inventory<T>
     */
    public class Main {
        static Scanner sc          = new Scanner(System.in);
        static FileManager fm      = new FileManager();
        static ShopSystem shop;
        public static void main(String[] args) {
            printBanner();
            mainMenu();
        }
        // 
    /*─── Main Menu ────────────────────────────────────────────────────────────*/
        static void mainMenu() {
            while (true) {
                System.out.println();
                if (fm.hasSaveFile()) {
                System.out.println("   [ Save file detected ]");
                }

                System.out.println("╔══════════════════════════════╗");  
                System.out.println("║         RPG BATTLE           ║"); 
                System.out.println("╠══════════════════════════════╣");    
                System.out.println("║  1. New Game                 ║");  
                System.out.println("║  2. Load Game                ║");
                System.out.println("║  3. Leaderboard              ║");
                System.out.println("║  4. Exit                     ║");
                System.out.println("╚══════════════════════════════╝");
                System.out.print("  > ");
                int choice = readInt(1, 4);
                switch (choice) {
                    case 1 -> startNewGame();
                    case 2 -> loadGame();
                    case 3 -> { fm.printLeaderboard(); pause(); }
                    case 4 -> { System.out.println("   Goodbye!"); 
                    System.exit(0); 
                }
                }
            }
        }
        // 
   /*  ─── New Game ─────────────────────────────────────────────────────────────*/
        static void startNewGame() {
            System.out.print("\n  Enter your hero's name > ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) name = "Hero";
            System.out.println();
            System.out.println("  Choose your class:");
            System.out.println("  1.  Warrior  (HP:150 ATK:30 DEF:25) — Shield Bash");
            System.out.println("  2.  Mage     (HP:80  ATK:55 DEF:8 ) — Fireball");
            System.out.println("  3.  Archer   (HP:110 ATK:42 DEF:15) — Triple Shot");
            System.out.print("  > ");

            int cls = readInt(1, 3);
            Character player = switch (cls) {
                case 1 -> new Warrior(name);
                case 2 -> new Mage(name);
                case 3 -> new Archer(name);
                default -> new Warrior(name);
            };
            player.addItem(new Potion()); // starting item
            System.out.println("\n  " + player.getClassName() + " " + name + " created!");
            System.out.println("  You receive a Potion to start your journey.");
            pause();
            runGame(player);
        }
    
        // 
   /*  ─── Load Game ────────────────────────────────────────────────────────────*/
        static void loadGame() {
            Character player = fm.load();
            if (player == null) {
                System.out.println("   No save file found!");
                pause();
                return;
            }
            runGame(player);
        }
        // 
    /*  ─── Town Hub ─────────────────────────────────────────────────────────────*/
        /**
         * [2.6 Parametric Polymorphism]
         * runGame accepts Character but internally creates BattleSystem<T>
         * which adapts to whatever subtype the player chose.
         */
        static void runGame(Character player) {
            shop = new ShopSystem(sc);
            while (true) {
                System.out.println();
                line();
                System.out.println("   TOWN — Stage " + player.getStage());
                line();
                System.out.println("  1.  Enter Battle (Stage " + player.getStage() 
    + ")");
                System.out.println("  2.  Shop");
                System.out.println("  3.  View Status");
                System.out.println("  4.  Save & Return to Menu");
                System.out.print("  > ");
                int choice = readInt(1, 4);
                switch (choice) {
                    case 1 -> enterBattle(player);
                    case 2 -> shop.openShop(player);
                    case 3 -> viewStatus(player);
                    case 4 -> {
                        fm.save(player);
                        return;
                    }
                }
            }
        }
        // 
    /*─── Battle ───────────────────────────────────────────────────────────────*/
        static void enterBattle(Character player) {
            int stage = player.getStage();
            System.out.println();
            System.out.println("\n   " + MonsterFactory.stageDescription(stage));
            pause();
            Monster monster = MonsterFactory.spawnForStage(stage);
            // [2.6 Parametric Polymorphism] — BattleSystem works for any Character subtype
            BattleSystem<Character> battle = new BattleSystem<>(player, sc);
            try {
                boolean won = battle.startBattle(monster);
                if (won) {
                    player.setStage(stage + 1);
                    System.out.println("   You advance to Stage " + 
                        player.getStage() + "!");
                    if (stage == 5) {
                        System.out.println();
                        System.out.println("   YOU DEFEATED THE DRAGON! GAME CLEAR!");
                        fm.saveLeaderboard(player.getName(), player.getClassName(),
                                player.getLevel(), stage);
                        fm.printLeaderboard();
                        pause();
                        return;
                    }
                }
            } catch (DeadCharacterException e) {
                // [2.3 Exception handling]
                System.out.println();
                System.out.println("   " + e.getMessage());
                System.out.println("    GAME OVER");
                fm.saveLeaderboard(player.getName(), player.getClassName(),
                        player.getLevel(), player.getStage() - 1);
                fm.printLeaderboard();
                pause();
                mainMenu();
            } catch (InvalidItemException e) {
                System.out.println("  " + e.getMessage());
            }
            pause();
        }
        // ─── Status ────────────────────────────────────────────────────────────
        static void viewStatus(Character player) {
            System.out.println();
            line();
            System.out.println(player.getFullStats());
            System.out.println();
            System.out.println("   INVENTORY (" + player.getInventory().size() + " items)");
            if (player.getInventory().isEmpty()) {
                System.out.println("  (empty)");
            } else {
                for (Item item : player.getInventory()) {
                    System.out.println("    • " + item.getName() + " — " + 
    item.getDescription());
                }
            }
            line();
            pause();
        }
        // ─── Helpers ───────────────────────────────────────────────────────────
        /**
         * [2.4 Keyboard Input] [2.3 Exception] Safe int reader.
         */
        static int readInt(int min, int max) {
            while (true) {
                try {
                    String line = sc.nextLine().trim();
                    int val = Integer.parseInt(line);
                    if (val >= min && val <= max) return val;
                    System.out.print("  Enter " + min + "-" + max + " > ");
                } catch (NumberFormatException e) {
                    System.out.print("  Numbers only! > ");
                }
            }
        }
        static void pause() {
            System.out.println("\n  [Press ENTER]");
            sc.nextLine();
        }
        static void line() {
            System.out.println("  " + "═".repeat(46));
        }
        static void printBanner() {
            System.out.println();
            System.out.println("   ██████╗ ██████╗  ██████╗ ");
            System.out.println("   ██╔══██╗██╔══██╗██╔════╝ ");
            System.out.println("   ██████╔╝██████╔╝██║  ███╗");
            System.out.println("   ██╔══██╗██╔═══╝ ██║   ██║");
            System.out.println("   ██║  ██║██║     ██║   ██║");
            System.out.println("   ╚═╝  ╚═╝╚═╝     ╚═╝   ╚═╝");
            System.out.println("   ╚██████╔╝");
            System.out.println("   ╚═════╝ ");
            System.out.println("       BATTLE SYSTEM v1.0");
            System.out.println();
        }
    }
   