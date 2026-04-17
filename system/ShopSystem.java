package system;
import java.util.Scanner;
import system.entity.*;
import system.entity.Character;
import system.exception.*;
/**
 * ShopSystem handles buying items.
 * [2.3 Exception] InsufficientGoldException thrown if player can't afford.
 * [2.4 Keyboard Input] Scanner reads player's purchase choice.
 */
public class ShopSystem {
    private final Scanner sc;

    public ShopSystem(Scanner sc) {
        this.sc = sc;
    }
    public void openShop(Character player) {
        while (true) {
            System.out.println();
            System.out.println("╔══════════════════════════════════╗");  
            System.out.println("║            SHOP                  ║");  
            System.out.println("║  Your Gold: " + String.format("%-21s", player.getGold() + " ") + "║");
            System.out.println("╠══════════════════════════════════╣");
            ItemFactory.printShopMenu();
            System.out.println("  0. Leave Shop");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("Select an item > ");
    
            int choice = readInt(0, 7);
            if (choice == 0) break;
            Item item = ItemFactory.create(choice);
            if (item == null) continue;
            try {
                if (player.getGold() < item.getPrice()) {
                    // [2.3 Custom Exception]
                    throw new InsufficientGoldException(player.getGold(), 
item.getPrice());
                }
                player.setGold(player.getGold() - item.getPrice());
                player.addItem(item);
                System.out.println("   Purchased " + item.getName() + "!");
            } catch (InsufficientGoldException e) {
                System.out.println("  " + e.getMessage());
            }
        }
    }
    private int readInt(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.print("  Enter " + min + "-" + max + " > ");
            } catch (NumberFormatException e) {
                System.out.print("  Numbers only > ");
            }
        }
    }
}