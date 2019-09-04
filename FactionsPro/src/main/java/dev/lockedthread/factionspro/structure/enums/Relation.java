package dev.lockedthread.factionspro.structure.enums;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter
public enum Relation {

    ALLY(ChatColor.DARK_PURPLE),
    ENEMY(ChatColor.RED),
    MEMBER(ChatColor.GREEN),
    NEUTRAL(ChatColor.WHITE),
    TRUCE(ChatColor.AQUA);

    private static Relation defaultRelation;

    @Setter
    private ChatColor chatColor;
    @Setter
    private boolean enabled;
    @Setter
    private boolean _default;

    Relation(ChatColor chatColor) {
        this.chatColor = chatColor;
        this.enabled = true;
        this._default = false;
    }

    @NotNull
    public static Relation getDefaultRelation() {
        if (defaultRelation == null) {
            defaultRelation = Arrays.stream(values()).filter(Relation::is_default).findFirst().orElse(MEMBER);
        }
        return defaultRelation;
    }


}
