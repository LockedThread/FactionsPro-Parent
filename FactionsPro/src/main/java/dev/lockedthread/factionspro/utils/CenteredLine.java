package dev.lockedthread.factionspro.utils;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class CenteredLine {

    private static final Pattern PARENTHESIS_PATTERN = Pattern.compile("\\((.*?)\\)");
    private static final Pattern DATA_PATTERN = Pattern.compile(">(.*?)<");
    private static final Pattern STRIP_COLOR_CODE_PATTERN = Pattern.compile("(?i)&[0-9A-FK-OR]");
    private static final int MINECRAFT_CHAT_CHAR_LENGTH = 54;

    private final String data;
    private final String code;

    public CenteredLine(String unformattedLine) {
        Matcher parenthesisMatcher = PARENTHESIS_PATTERN.matcher(unformattedLine);
        parenthesisMatcher.find();
        if ((this.code = parenthesisMatcher.group(1)) == null) {
            throw new RuntimeException("Unable to find the code in between parenthesis in the string, \"" + unformattedLine + "\"");
        }
        Matcher dataMatcher = DATA_PATTERN.matcher(unformattedLine);
        dataMatcher.find();
        if ((this.data = dataMatcher.group(1)) == null) {
            throw new RuntimeException("Unable to find any data in between the greater and less than signs in the string, \"" + unformattedLine + "\"");
        }
    }

    public String getFormattedCenteredLine() {
        String strippedCode = STRIP_COLOR_CODE_PATTERN.matcher(code).replaceAll("");
        String strippedData = STRIP_COLOR_CODE_PATTERN.matcher(data).replaceAll("");

        int fixesLength = MINECRAFT_CHAT_CHAR_LENGTH - strippedData.length() / 2;

        String fixes = code + StringUtils.repeat(strippedCode, fixesLength - 1);
        return fixes + data + fixes;
    }

}
