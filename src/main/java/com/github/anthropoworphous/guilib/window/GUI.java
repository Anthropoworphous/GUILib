package com.github.anthropoworphous.guilib.window;

import com.github.anthropoworphous.guilib.window.inventory.InventoryBuilder;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import main.index.ID;
import main.structure.tree.Connected;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Hold the inventoryBuilder and mainPane
 * Doesn't need to recreate every time, create once and cache it
 * the show() method create new Window
 */
public class GUI implements Listener {
    public GUI(@NotNull InventoryBuilder builder, Pane mainPane, String name) {
        this.builder = builder;
        this.mainPane = new Pane(new ID(0), builder.getWidth(), builder.getHeight(), "ROOT_PANE");
        this.mainPane.adopt(mainPane);
        this.name = name;
    }
    public GUI(@NotNull InventoryBuilder builder, String name) {
        this.builder = builder;
        this.mainPane = new Pane(new ID(0), builder.getWidth(), builder.getHeight(), "ROOT_PANE");
        this.name = name;
    }

    private final InventoryBuilder builder;
    private final Pane mainPane;
    private final String name;

    private static final Map<Inventory, Window> activeWindow = new HashMap<>();

    @NotNull public String getName() { return name; }
    @NotNull public Pane getMainPane() { return mainPane; }
    @NotNull public InventoryBuilder getInventoryBuilder() { return builder; }

    /**
     * Switch or add a new Pane to MainPane
     * @param mainPane This isn't the actual main pane, the main pane is generated when the Pane is created
     * @return Old Pane if there is one, null otherwise
     */
    @Nullable public Pane setMainPane(@NotNull Pane mainPane) {
        Connected child = null;
        if (this.mainPane.getChild().size() == 1) { //actual mainPane should only have 1 or no child
            child = this.mainPane.getChild().get(0);
            this.mainPane.disown(0);
        }
        this.mainPane.adopt(mainPane);

        return (Pane)child;
    }

    @NotNull protected static Map<Inventory, Window> getActiveWindows() {
        return activeWindow;
    }

    public Window createWindow() {
        return new Window(this);
    }
}