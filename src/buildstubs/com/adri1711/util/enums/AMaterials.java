package com.adri1711.util.enums;

import org.bukkit.Material;

public enum AMaterials {
    AIR,
    ARROW,
    BARRIER,
    BLAZE_POWDER,
    BLAZE_ROD,
    BOOK,
    BOW,
    CARROT,
    COAL,
    DIAMOND,
    EMERALD,
    EMERALD_BLOCK,
    ENDER_PEARL,
    EYE_OF_ENDER,
    GLOWSTONE_DUST,
    GOLDEN_CARROT,
    GOLD_BLOCK,
    GOLD_INGOT,
    IRON_BLOCK,
    IRON_INGOT,
    IRON_SWORD,
    LEATHER_BOOTS,
    LEATHER_CHESTPLATE,
    LEATHER_HELMET,
    LEATHER_LEGGINGS,
    PAPER,
    REDSTONE,
    REDSTONE_BLOCK,
    SAPLING,
    SKULL_ITEM,
    SNOW_BALL,
    STICK,
    WHEAT,
    WOOD_SWORD;

    public Material toMaterial() {
        return Material.valueOf(name());
    }

    public static AMaterials getAMaterial(String name) {
        if (name == null) {
            return AIR;
        }
        try {
            return valueOf(name.toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return AIR;
        }
    }
}
