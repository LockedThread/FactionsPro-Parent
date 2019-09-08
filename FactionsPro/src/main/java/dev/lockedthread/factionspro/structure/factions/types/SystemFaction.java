package dev.lockedthread.factionspro.structure.factions.types;

import dev.lockedthread.factionspro.structure.enums.Relation;
import dev.lockedthread.factionspro.structure.factions.Faction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString(doNotUseGetters = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SystemFaction extends Faction {

    private final Relation globalRelation;
    private boolean defaultFaction;

    public SystemFaction(String name, Relation globalRelation, UUID uuid) {
        super(name, null);
        this.globalRelation = globalRelation;

    }
}
