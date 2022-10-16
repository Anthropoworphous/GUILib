package com.github.anthropoworphous.guilib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class GUILib extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("GUILib loaded");
    }
}
