package dev.lockedthread.factionspro.structure.enums;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

@Getter
public enum Relation {

    ALLY(ChatColor.DARK_PURPLE),
    ENEMY(ChatColor.RED),
    MEMBER(ChatColor.GREEN),
    NEUTRAL(ChatColor.WHITE),
    TRUCE(ChatColor.AQUA);

    @Setter
    private ChatColor chatColor;
    @Setter
    private boolean enabled;

    Relation(ChatColor chatColor) {
        this.chatColor = chatColor;
        this.enabled = true;
    }
}
