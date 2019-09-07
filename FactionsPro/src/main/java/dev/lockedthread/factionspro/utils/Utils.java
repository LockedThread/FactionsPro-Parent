package dev.lockedthread.factionspro.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Utils {

    public static final Supplier<EnumSet<Material>> ENUM_SET_SUPPLIER = () -> EnumSet.noneOf(Material.class);
    public static final Function<String, Material> MATERIAL_PARSE_FUNCTION = s -> {
        Material material = Material.matchMaterial(s);
        if (material == null) {
            throw new RuntimeException("Unable to parse material \"" + s + "\". Please correct this material in your config!");
        }
        return material;
    };

    public static boolean isOutsideBorder(Location location) {
        WorldBorder worldBorder = location.getWorld().getWorldBorder();
        double size = worldBorder.getSize() / 2.0;
        double centerX = worldBorder.getCenter().getX();
        double centerZ = worldBorder.getCenter().getZ();
        return centerX - size > location.getX() || centerX + size <= location.getX() || centerZ - size > location.getZ() || centerZ + size <= location.getZ();
    }

    public static String toTitleCasing(String input) {
        input = input.toLowerCase();
        return !input.contains(" ") ? StringUtils.capitalize(input) : Arrays.stream(input.split(" ")).map(s -> StringUtils.capitalize(s) + " ").collect(Collectors.joining());
    }

    public static EnumSet<Material> parseStringListAsEnumSet(List<String> list) {
        return list.stream().map(MATERIAL_PARSE_FUNCTION).collect(Collectors.toCollection(ENUM_SET_SUPPLIER));
    }
}
