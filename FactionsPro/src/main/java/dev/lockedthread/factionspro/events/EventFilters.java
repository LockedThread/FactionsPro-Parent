package dev.lockedthread.factionspro.events;

import dev.lockedthread.factionspro.menus.Menu;
import dev.lockedthread.factionspro.utils.Utils;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

@SuppressWarnings({"unchecked", "unused"})
public class EventFilters {
    private static final Predicate<PlayerMoveEvent> IGNORE_SAME_BLOCK = event -> event.getFrom().getX() != event.getTo().getX() && event.getFrom().getZ() != event.getTo().getZ() && event.getFrom().getY() != event.getTo().getY();
    private static final Predicate<PlayerMoveEvent> IGNORE_SAME_CHUNK = event -> event.getFrom().getChunk().getX() != event.getTo().getChunk().getX() && event.getFrom().getChunk().getZ() != event.getTo().getChunk().getZ();
    private static final Predicate<? extends Cancellable> IGNORE_CANCELLED = cancellable -> !cancellable.isCancelled();
    private static final Predicate<? extends InventoryEvent> IGNORE_NON_MENUS = (Predicate<InventoryEvent>) event -> event instanceof InventoryClickEvent ? ((InventoryClickEvent) event).getClickedInventory() != null && ((InventoryClickEvent) event).getClickedInventory().getHolder() instanceof Menu : event.getInventory().getHolder() instanceof Menu;
    private static final Predicate<? extends Event> IGNORE_HAND_NULL = event -> {
        if (event instanceof PlayerBucketEmptyEvent) {
            ItemStack itemStack = ((PlayerBucketEmptyEvent) event).getItemStack();
            return itemStack != null && itemStack.getType() != Material.AIR;
        } else if (event instanceof PlayerInteractEvent) {
            ItemStack itemStack = ((PlayerInteractEvent) event).getItem();
            return itemStack != null && itemStack.getType() != Material.AIR;
        } else if (event instanceof PlayerEvent) {
            return ((PlayerEvent) event).getPlayer().getItemInHand().getType() != Material.AIR;
        } else if (event instanceof BlockBreakEvent) {
            return ((BlockBreakEvent) event).getPlayer().getItemInHand().getType() != Material.AIR;
        } else if (event instanceof BlockPlaceEvent) {
            return ((BlockPlaceEvent) event).getItemInHand().getType() != Material.AIR;
        }
        return false;
    };
    private static final Predicate<? extends Event> IGNORE_HAND_META_NULL = event -> {
        if (event instanceof PlayerBucketEmptyEvent) {
            return ((PlayerBucketEmptyEvent) event).getItemStack().hasItemMeta();
        } else if (event instanceof PlayerInteractEvent) {
            return ((PlayerInteractEvent) event).getItem().hasItemMeta();
        } else if (event instanceof PlayerEvent) {
            return ((PlayerEvent) event).getPlayer().getItemInHand().hasItemMeta();
        } else if (event instanceof BlockBreakEvent) {
            return ((BlockBreakEvent) event).getPlayer().getItemInHand().hasItemMeta();
        } else if (event instanceof BlockPlaceEvent) {
            return ((BlockPlaceEvent) event).getItemInHand().hasItemMeta();
        }
        return false;
    };
    private static final Predicate<? extends Event> IGNORE_HAND_DISPLAYNAME_NULL = event -> {
        if (event instanceof PlayerBucketEmptyEvent) {
            return ((PlayerBucketEmptyEvent) event).getItemStack().getItemMeta().hasDisplayName();
        } else if (event instanceof PlayerInteractEvent) {
            return ((PlayerInteractEvent) event).getItem().getItemMeta().hasDisplayName();
        } else if (event instanceof PlayerEvent) {
            return ((PlayerEvent) event).getPlayer().getItemInHand().getItemMeta().hasDisplayName();
        } else if (event instanceof BlockBreakEvent) {
            return ((BlockBreakEvent) event).getPlayer().getItemInHand().getItemMeta().hasDisplayName();
        } else if (event instanceof BlockPlaceEvent) {
            return ((BlockPlaceEvent) event).getItemInHand().getItemMeta().hasDisplayName();
        }
        return false;
    };
    private static final Predicate<? extends Event> IGNORE_OUTSIDE_BORDER = event -> {
        if (event instanceof PlayerTeleportEvent) {
            return !Utils.isOutsideBorder(((PlayerTeleportEvent) event).getTo());
        } else if (event instanceof BlockPlaceEvent) {
            return !Utils.isOutsideBorder(((BlockPlaceEvent) event).getBlockPlaced().getLocation());
        } else if (event instanceof BlockBreakEvent) {
            return !Utils.isOutsideBorder(((BlockBreakEvent) event).getBlock().getLocation());
        } else if (event instanceof PlayerBucketEmptyEvent) {
            return !Utils.isOutsideBorder(((PlayerBucketEmptyEvent) event).getBlockClicked().getRelative(((PlayerBucketEmptyEvent) event).getBlockFace()).getLocation());
        } else if (event instanceof BlockEvent) {
            return !Utils.isOutsideBorder(((BlockEvent) event).getBlock().getLocation());
        } else if (event instanceof PlayerEvent) {
            return !Utils.isOutsideBorder(((PlayerEvent) event).getPlayer().getLocation());
        }
        return false;
    };

    public static <T extends Event> Predicate<T> getIgnoreOutsideBorder() {
        return (Predicate<T>) IGNORE_OUTSIDE_BORDER;
    }

    public static <T extends Event> Predicate<T> getIgnoreHandDisplaynameNull() {
        return (Predicate<T>) IGNORE_HAND_DISPLAYNAME_NULL;
    }

    public static <T extends Event> Predicate<T> getIgnoreHandMetaNull() {
        return (Predicate<T>) IGNORE_HAND_META_NULL;
    }

    public static <T extends InventoryEvent> Predicate<T> getIgnoreNonMenus() {
        return (Predicate<T>) IGNORE_NON_MENUS;
    }

    public static <T extends Cancellable> Predicate<T> getIgnoreCancelled() {
        return (Predicate<T>) IGNORE_CANCELLED;
    }

    public static Predicate<PlayerMoveEvent> getIgnoreSameChunk() {
        return IGNORE_SAME_CHUNK;
    }

    public static Predicate<PlayerMoveEvent> getIgnoreSameBlock() {
        return IGNORE_SAME_BLOCK;
    }

    public static <T extends Event> Predicate<T> getIgnoreHandNull() {
        return (Predicate<T>) IGNORE_HAND_NULL;
    }

}
