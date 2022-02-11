package com.github.anthropoworphous.guilib;

import com.github.anthropoworphous.guilib.window.WindowListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class GUILib extends JavaPlugin {
    private static GUILib plugin;
    public static GUILib getPlugin() { return plugin; }

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getPluginManager().registerEvents(new WindowListener(), this);
        Bukkit.getLogger().info("GUILib loaded");
    }
}
