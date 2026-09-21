package com.adri1711.api;

import com.adri1711.util.enums.AMaterials;
import org.bukkit.Material;

/**
 * Compile-time compatibility shim for the Lib1711 API used by RolCards.
 * The generated plugin JAR excludes this package; the real Lib1711 remains
 * the server-side dependency declared in plugin.yml.
 */
public class API1711 {
    public API1711(String user, String pluginName) {
    }

    public Material getMaterial(AMaterials material) {
        return material.toMaterial();
    }
}
