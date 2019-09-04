package dev.lockedthread.factionspro.menus;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@EqualsAndHashCode
@ToString
public abstract class Menu implements InventoryHolder {

    private static final Consumer<InventoryClickEvent> CANCELLED_CLICK = event -> event.setCancelled(true);

    private final Inventory inventory;

    public Menu(String title, int slots) {
        this.inventory = Bukkit.createInventory(this, slots, ChatColor.translateAlternateColorCodes('&', title));
    }

    public Menu(String title, InventoryType inventoryType) {
        this(title, inventoryType.getDefaultSize());
    }

    public abstract void create();

    public void setItem(int slot, MenuItem menuItem) {
        inventory.setItem(slot, menuItem);
    }

    public MenuItem getMenuItem(int slot) {
        if (slot >= getSize()) {
            throw new RuntimeException("Unable to get MenuItem that's at a slot that's out of bounds. slot=" + slot + ", size" + getSize());
        }
        ItemStack item = inventory.getItem(slot);
        return item instanceof MenuItem ? (MenuItem) item : null;
    }

    public int getSize() {
        return getInventory().getSize();
    }

    public void execute(InventoryClickEvent event) {
        MenuItem menuItem = getMenuItem(event.getRawSlot());
        if (menuItem != null && menuItem.getClickConsumer() != null) {
            menuItem.getClickConsumer().accept(event);
        } else {
            CANCELLED_CLICK.accept(event);
        }
    }

    @NotNull
    public InventoryType getType() {
        return getInventory().getType();
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
