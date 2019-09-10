package dev.lockedthread.factionspro.messages.iface;

public interface IMessages {

    String getConfigKey();

    String getMessage();

    void setMessage(String message);

    String[] getArrayMessage();

    String getUnformattedMessage();

    boolean isArrayMessage();

    void setArrayMessage(String[] arrayMessage);
}
