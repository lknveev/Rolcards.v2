# GitHub Build Setup

This project uses the original RolCards flat `src/` layout.

GitHub Actions now:
1. Uses Java 8.
2. Runs Maven.
3. Compiles the `src/` tree.
4. Packages `plugin.yml`, `config.yml`, `messages.yml`, and `cards.yml`.
5. Produces `target/RolCards.jar`.
6. Uploads `RolCards.jar` as the `RolCards-JAR` Actions artifact.

The source references Lib1711's API1711/AMaterials classes. Because Lib1711 is a server-side dependency, this repository includes compile-only compatibility stubs under `src/buildstubs/`. Maven excludes those two stub packages from the final JAR, so the real Lib1711 remains the server dependency.

Server dependencies declared by plugin.yml:
- Vault
- Lib1711
- MVdWPlaceholderAPI is optional

Target API: Spigot 1.12.2, Java 8.
