package dev.lockedthread.factionspro;

import dev.lockedthread.factionspro.utils.CenteredLine;
import org.junit.Test;

public class CenteredLineTest {

    @Test
    public void testCenterLine() {
        CenteredLine centeredLine = new CenteredLine("<center-line(&7|-)> {relation-color}{faction} <center-line>");
        System.out.println("centeredLine.toString() = " + centeredLine.toString());
        System.out.println("centeredLine.getFormattedCenteredLine() = " + centeredLine.getFormattedCenteredLine());
    }
}
