# GitHub testing guide for RolCards Stage 6-12

## 1. Replace the project files
Copy the contents of `RolCards-master/RolCards/` into the matching directory in your RolCards repository. Keep the original MIT `LICENSE`.

## 2. cards.yml
After installing the plugin, copy `cards.yml` from this repository into `plugins/RolCards/cards.yml`. The loader reads that file at runtime.

## 3. Server dependencies
The original plugin declares Vault and Lib1711 as required dependencies. Keep those installed on the server. The Spigot 1.12.2 API is also required to compile the source.

## 4. New commands
- `/rolcards class <0-7>`
- `/rolcards deck list`
- `/rolcards deck clear`
- `/rolcards deck add <card>`
- `/rolcards deck remove <card>`
- `/rolcards collection [class]`
- `/rolcards pack [price]`
- `/rolcards rank`
- `/rolcards history`
- `/rolcards card give <card> [player]` (admin)

## 5. Classes
0 Normal, 1 Hunter, 2 Mage, 3 Warrior, 4 Assassin, 5 Paladin, 6 Necromancer, 7 Druid.

## 6. Card effects
DAMAGE, HEAL, DRAW, SPAWN, DAMAGE_ALL, HEAL_ALL, MANA, STEAL_MANA, POISON, BURN, STUN, SHIELD, LIFESTEAL, DISCARD, RAGE, CRITICAL, RANDOM.

## 7. Build note
This source still uses the project's original external `Lib1711` API and Vault. This environment did not have those jars available, so a final JAR was not falsely claimed as compiled here.
