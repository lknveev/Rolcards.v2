package com.adri1711.rolcards.cards;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.adri1711.rolcards.RolCards;

/**
 * Loads user-created, data-driven cards from cards.yml.
 *
 * Supported classes: NORMAL, HUNTER, MAGE, WARRIOR, ASSASSIN, PALADIN, NECROMANCER, DRUID (or 0-7).
 * Supported effects: DAMAGE, HEAL, DRAW, SPAWN, DAMAGE_ALL, HEAL_ALL, MANA, STEAL_MANA, POISON, BURN, STUN, SHIELD, LIFESTEAL, DISCARD, RAGE, CRITICAL, RANDOM.
 */
public final class ConfigCardLoader {
    private ConfigCardLoader() {}

    public static int load(RolCards plugin) {
        File file = new File(plugin.getDataFolder(), "cards.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException ex) {
                plugin.getLogger().warning("Could not create cards.yml: " + ex.getMessage());
                return 0;
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection cards = config.getConfigurationSection("cards");
        if (cards == null) {
            plugin.getLogger().warning("cards.yml has no 'cards:' section. No configurable cards were loaded.");
            return 0;
        }

        int loaded = 0;
        for (String classKey : cards.getKeys(false)) {
            CardClass cardClass = parseClass(classKey);
            if (cardClass == null) {
                plugin.getLogger().warning("Unknown card class '" + classKey + "' in cards.yml.");
                continue;
            }

            ConfigurationSection classSection = cards.getConfigurationSection(classKey);
            if (classSection == null) continue;

            for (String cardKey : classSection.getKeys(false)) {
                ConfigurationSection section = classSection.getConfigurationSection(cardKey);
                if (section == null) continue;

                String name = color(section.getString("name", cardKey));
                int price = section.getInt("price", 0);
                int cost = section.getInt("cost", 0);
                String permission = section.getString("permission", defaultPermission(cardClass));

                List<String> description = section.getStringList("description");
                while (description.size() < 5) description.add("");
                if (description.size() > 5) description = new ArrayList<String>(description.subList(0, 5));
                for (int i = 0; i < description.size(); i++) {
                    description.set(i, color(description.get(i)));
                }

                CardEffect effect = parseEffect(section.getString("effect", "DAMAGE"));
                if (effect == null) {
                    plugin.getLogger().warning("Card '" + name + "' has an unknown effect. Skipped.");
                    continue;
                }

                int value = section.getInt("value", 0);

                try {
                    if (effect == CardEffect.SPAWN) {
                        String mob = section.getString("mob", "ZOMBIE");
                        double damage = section.getDouble("mob-damage", 1.0D);
                        double health = section.getDouble("mob-health", 1.0D);
                        addSpawnCard(plugin, cardClass, name, price, cost, permission, description,
                                effect, value, mob, damage, health);
                    } else {
                        addSimpleCard(plugin, cardClass, name, price, cost, permission, description, effect, value);
                    }
                    loaded++;
                } catch (RuntimeException ex) {
                    plugin.getLogger().warning("Could not load card '" + name + "': " + ex.getMessage());
                }
            }
        }

        plugin.getLogger().info("Loaded " + loaded + " configurable RolCards card(s) from cards.yml.");
        return loaded;
    }

    private static void addSimpleCard(RolCards plugin, CardClass clazz, String name, int price, int cost,
                                      String permission, List<String> d, CardEffect effect, int value) {
        new CreatedCard(clazz, name, price, cost, permission, d.get(0), d.get(1), d.get(2), d.get(3), d.get(4),
                effect, value, plugin);
    }

    private static void addSpawnCard(RolCards plugin, CardClass clazz, String name, int price, int cost,
                                     String permission, List<String> d, CardEffect effect, int value,
                                     String mob, double damage, double health) {
        new CreatedCard(clazz, name, price, cost, permission, d.get(0), d.get(1), d.get(2), d.get(3), d.get(4),
                effect, value, mob, damage, health, plugin);
    }

    private static String defaultPermission(CardClass clazz) {
        return "rolcards.class." + clazz.name().toLowerCase(Locale.ROOT);
    }

    private static CardClass parseClass(String value) {
        if (value == null) return null;
        String key = value.trim().toUpperCase(Locale.ROOT);
        if ("0".equals(key)) return CardClass.NORMAL;
        if ("1".equals(key)) return CardClass.HUNTER;
        if ("2".equals(key)) return CardClass.MAGE;
        if ("3".equals(key)) return CardClass.WARRIOR;
        if ("4".equals(key)) return CardClass.ASSASSIN;
        if ("5".equals(key)) return CardClass.PALADIN;
        if ("6".equals(key)) return CardClass.NECROMANCER;
        if ("7".equals(key)) return CardClass.DRUID;
        try {
            return CardClass.valueOf(key);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private static CardEffect parseEffect(String value) {
        if (value == null) return null;
        try {
            return CardEffect.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private static String color(String value) {
        return value == null ? "" : value.replace('&', '\u00a7');
    }
}
