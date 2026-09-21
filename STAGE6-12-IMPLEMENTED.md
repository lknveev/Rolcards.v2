# RolCards Stage 6-12 implementation

Implemented in source:
- 8 classes (0-7)
- configurable card effects
- deck/collection/class commands
- card packs using Vault economy
- card grant command
- UUID-backed progression save
- LuckPerms/Vault permission nodes
- reload-safe extra class lists

Commands:
/rolcards class <0-7>
/rolcards deck [list|clear|add <card>|remove <card>]
/rolcards collection [class]
/rolcards pack [price]
/rolcards rank
/rolcards history
/rolcards card give <card> [player]

The legacy GUI remains in place. Advanced combat effects are declared in CardEffect and can be loaded from cards.yml; additional combat execution should be tested against the target Spigot 1.12.2 API/dependency set before production use.
