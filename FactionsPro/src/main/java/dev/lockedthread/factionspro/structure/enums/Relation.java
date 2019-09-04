package dev.lockedthread.factionspro.structure.enums;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter
public enum Relation {

    ALLY(ChatColor.DARK_PURPLE, true, false),
    ENEMY(ChatColor.RED, true, false),
    MEMBER(ChatColor.GREEN, true, false),
    NEUTRAL(ChatColor.WHITE, true, true),
    TRUCE(ChatColor.AQUA, true, false);

    private static Relation defaultRelation;

    @Setter
    private ChatColor chatColor;
    @Setter
    private boolean enabled;
    @Setter
    private boolean _default;

    Relation(ChatColor chatColor, boolean enabled, boolean _default) {
        this.chatColor = chatColor;
        this.enabled = enabled;
        this._default = _default;
    }

    @NotNull
    public static Relation getDefaultRelation() {
        if (defaultRelation == null) {
            defaultRelation = Arrays.stream(values()).filter(Relation::is_default).findFirst().orElse(MEMBER);
        }
        return defaultRelation;
    }


}
