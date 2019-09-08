package dev.lockedthread.factionspro.structure.enums;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.ChatColor;

import java.util.Arrays;

@ToString
@Getter
public enum Relation {

    ALLY(ChatColor.DARK_PURPLE, true, false),
    ENEMY(ChatColor.RED, true, false),
    MEMBER(ChatColor.GREEN, true, false),
    NEUTRAL(ChatColor.WHITE, true, true),
    TRUCE(ChatColor.AQUA, true, false),

    WILDERNESS(ChatColor.DARK_GREEN, true, false),
    SAFE_ZONE(ChatColor.GOLD, true, false),
    WAR_ZONE(ChatColor.DARK_RED, true, false);

    @Getter(lazy = true)
    private static final Relation defaultRelation = Arrays.stream(values()).filter(Relation::is_default).findFirst().orElse(MEMBER);

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

    public boolean isSystemRelation() {
        return this == WILDERNESS || this == SAFE_ZONE || this == WAR_ZONE;
    }
}
