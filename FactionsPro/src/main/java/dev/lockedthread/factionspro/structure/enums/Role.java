package dev.lockedthread.factionspro.structure.enums;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public enum Role {

    LEADER(3, "Leader", "**"), CO_LEADER(2, "Co-Leader", "*"), MODERATOR(1, "Moderator", "+"), MEMBER(0, "Member", "-");

    @Setter
    private int rank;
    @Setter
    private String name, icon;

    Role(int rank, String name, String icon) {
        this.rank = rank;
        this.name = name;
        this.icon = icon;
    }

}
