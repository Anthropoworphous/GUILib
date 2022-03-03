package com.github.anthropoworphous.guilib.window;

import com.github.anthropoworphous.guilib.window.inventory.InventoryBuilder;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.guiitem.IGUIItem;
import main.index.ID;
import main.index.Index;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Hold the actual inventory and main pane
 * Doesn't need to recreate every time, the show() method create new GUI, create once an cache it
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
    private Map<Index, WindowSlot> guiItemReferences;

    public String getName() { return name; }
    public Pane getMainPane() { return mainPane; }
    public InventoryBuilder getInventoryBuilder() { return builder; }

    protected static Map<UUID, Map.Entry<Window, Inventory>> getActiveGUI() {
        return activeGUI;
    }
    protected Map<Index, WindowSlot> getGuiItemReferences() { return guiItemReferences; }
    protected WindowSlot get(int index) {
        return guiItemReferences.get(new ID(index));
    }
    protected void clearGuiItemReferences() {
        guiItemReferences.clear();
    }

    /**
     * create an inventory and populate it using the mainPane
     *
     * @param p player to show the inventory to
     */
    public void show(Player p) {
        Inventory inv = builder.getInventory();
        guiItemReferences = mainPane.draw(inv, this);
        activeGUI.put(p.getUniqueId(), Map.entry(this, inv));
        p.openInventory(inv);
    }

    /**
     * Refresh GUIItem, if changes where made to the displayed item this will update it
     * @param targetInv inventory to refresh
     * @param targetIndex item to refresh
     */
    public void refresh(Inventory targetInv, Index targetIndex) {
        Pane.draw(targetInv, targetIndex, guiItemReferences.get(targetIndex));
    }
    /**
     * Refresh GUIItem, if changes where made to the displayed item this will update it
     * Try to use index if possible, searching for item is quite inefficient
     * @param targetInv inventory to refresh
     * @param targetItem item to refresh
     */
    public void refresh(Inventory targetInv, IGUIItem targetItem) {
        guiItemReferences.forEach((index, slot) -> {
            if (slot.items().containsValue(targetItem)) {
                Pane.draw(targetInv, index, slot);
            }
        });
    }
}