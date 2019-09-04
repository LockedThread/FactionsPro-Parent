package dev.lockedthread.factionspro.messages.iface;

public interface IMessages {

    String getConfigKey();

    String getMessage();

    String getUnformattedMessage();

    void setMessage(String message);
}
