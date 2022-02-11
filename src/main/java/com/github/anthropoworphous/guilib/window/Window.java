package com.github.anthropoworphous.guilib.window;

import com.github.anthropoworphous.guilib.util.ID;
import com.github.anthropoworphous.guilib.window.inventory.InventoryBuilder;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Hold the actual inventory and main pane
 * Doesn't need to recreated every time, the show() method create new GUI, create once an cache it
 */
public class Window implements Listener {
    public Window(InventoryBuilder builder, Pane pane, String name) {
        this.builder = builder;
        mainPane = new Pane(new ID(0), builder.getWidth(), builder.getHeight(), "ROOT_PANE");
        mainPane.adopt(pane);
        this.name = name;
    }

    private final InventoryBuilder builder;
    private final Pane mainPane;
    private final String name;

    private static final Map<UUID, Map.Entry<Window, Inventory>> activeGUI = new HashMap<>();
    private Map<ID, WindowSlot> guiItemReferences;

    public String getName() {
        return name;
    }
    public Pane getMainPane() {
        return mainPane;
    }
    public InventoryBuilder getInventoryBuilder() { return builder; }
    protected static Map<UUID, Map.Entry<Window, Inventory>> getActiveGUI() {
        return activeGUI;
    }
    public WindowSlot get(int id) {
        return guiItemReferences.get(new ID(id));
    }
    protected Map<ID, WindowSlot> getGuiItemReferences() { return guiItemReferences; }

    /**
     * create an inventory and populate it using the mainPane
     *
     * @param p player to show the inventory to
     */
    public void show(Player p) {
        Inventory inv = builder.getInventory();

        guiItemReferences = mainPane.draw(inv);
        activeGUI.put(p.getUniqueId(), Map.entry(this, inv));

        p.openInventory(inv);
    }
}