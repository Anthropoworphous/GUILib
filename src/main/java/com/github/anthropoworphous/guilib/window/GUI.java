package com.github.anthropoworphous.guilib.window;

import com.github.anthropoworphous.guilib.window.inventory.InventoryBuilder;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Hold the inventoryBuilder and mainPane
 * Doesn't need to recreate every time, create once and cache it
 * the show() method create new Window
 */
public class GUI implements Listener {
    public GUI(@NotNull InventoryBuilder builder) {
        this(builder, (Component) null);
    }
    public GUI(@NotNull InventoryBuilder builder, Pane mainPane) {
        this(builder, mainPane, (Component) null);
    }
    public GUI(@NotNull InventoryBuilder builder, String name) {
        this(builder, Component.text(name));
    }
    public GUI(@NotNull InventoryBuilder builder, Pane mainPane, String name) {
        this(builder, name);
        this.rootPane.adopt(mainPane);
    }
    public GUI(@NotNull InventoryBuilder builder, Component name) {
        this.builder = builder;
        this.rootPane = new Pane(0, builder.getWidth(), builder.getHeight(), "ROOT_PANE");
        this.name = name;
    }
    public GUI(@NotNull InventoryBuilder builder, Pane mainPane, Component name) {
        this(builder, name);
        this.rootPane.adopt(mainPane);
    }

    private final InventoryBuilder builder;
    private final Pane rootPane;
    private final Component name;

    private static final Map<Inventory, Window> activeWindow = new HashMap<>();

    @NotNull public Component getName() { return name; }
    @NotNull public Pane getMainPane() { return rootPane; }
    @NotNull public InventoryBuilder getInventoryBuilder() { return builder; }

    /**
     * Switch or add a new Pane to MainPane
     * @param mainPane This isn't the actual main pane, the main pane is generated when the Pane is created
     */
    public void setMainPane(@NotNull Pane mainPane) {
        this.rootPane.descent().ifPresent(descendants -> descendants.forEach(this.rootPane::disown));
        this.rootPane.adopt(mainPane);
    }

    @NotNull protected static Map<Inventory, Window> getActiveWindows() {
        return activeWindow;
    }

    public Window createWindow() {
        return new Window(this);
    }
}