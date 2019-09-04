package dev.lockedthread.factionspro.menus;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MenuItem extends ItemStack {

    @Getter
    private Consumer<InventoryClickEvent> clickConsumer;

    public MenuItem(@NotNull ItemStack itemStack) {
        super(itemStack);
    }

    public MenuItem(@NotNull Material material) {
        super(material);
    }

    public MenuItem(@NotNull Material material, int amount) {
        super(material, amount);
    }

    public MenuItem(@NotNull Material material, int amount, short damage) {
        super(material, amount, damage);
    }

    public MenuItem setClickConsumer(Consumer<InventoryClickEvent> clickConsumer) {
        this.clickConsumer = clickConsumer;
        return this;
    }
}
