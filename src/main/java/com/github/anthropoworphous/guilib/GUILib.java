package com.github.anthropoworphous.guilib;

import com.github.anthropoworphous.guilib.window.WindowListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class GUILib extends JavaPlugin {
    public GUILib() {
        super();
    }

    public GUILib(@NotNull JavaPluginLoader loader,
                  @NotNull PluginDescriptionFile description,
                  @NotNull File dataFolder,
                  @NotNull File file) {
        super(loader, description, dataFolder, file);
    }

    private static GUILib plugin;
    public static GUILib getPlugin() { return plugin; }

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getPluginManager().registerEvents(new WindowListener(), this);
        Bukkit.getLogger().info("GUILib loaded");
    }
}
