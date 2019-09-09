package dev.lockedthread.factionspro.messages.iface;

public interface IMessages {

    String getConfigKey();

    String getMessage();

    String[] getArrayMessage();

    String getUnformattedMessage();

    void setMessage(String message);

    boolean isArrayMessage();

    void setArrayMessage(String[] arrayMessage);
}
