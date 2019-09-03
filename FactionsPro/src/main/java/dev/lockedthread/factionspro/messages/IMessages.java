package dev.lockedthread.factionspro.messages;

public interface IMessages {

    String getConfigKey();

    String getMessage();

    String getUnformattedMessage();

    void setMessage(String message);
}
