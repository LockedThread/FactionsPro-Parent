package dev.lockedthread.factionspro.structure.enums;

/**
 * The return type to the Faction#claim method
 */
public enum ClaimResponse {

    NOT_ENOUGH_POWER,
    NO_PERMISSION,
    OUTSIDE_BORDER,
    UNABLE_TO_OVERCLAIM,
    SOMEONE_OWNS_THIS_LAND,
    SUCCESS

}
