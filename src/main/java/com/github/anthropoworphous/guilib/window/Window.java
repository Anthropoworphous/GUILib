package com.github.anthropoworphous.guilib.window;

import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.guiitem.IGUIItem;
import main.index.Index;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class Window {
    public Window(GUI gui) {
        this.gui = gui;
        inv = gui.getInventoryBuilder().getInventory();
        itemReferences = gui.getMainPane().draw(this);
    }

    private final GUI gui;
    private final Inventory inv;
    private Map<Index, WindowSlot> itemReferences;

    //getter
    public GUI gui() { return gui; }
    public Inventory inv() { return inv; }
    public Map<Index, WindowSlot> itemReferences() { return itemReferences; }
    //end

    public void show(Player viewer) {
        GUI.getActiveWindows().put(inv, this);
        viewer.openInventory(inv);
    }

    public void reload() {
        inv.clear();
        itemReferences = gui.getMainPane().draw(this);
    }

    /**
     * Refresh GUIItem, if changes where made to the displayed item this will update it
     * @param targetIndex item to refresh
     */
    public void refresh(Index targetIndex) {
        Pane.draw(inv, targetIndex, itemReferences);
    }

    /**
     * Refresh GUIItem, if changes where made to the displayed item this will update it
     * Try to use index if possible, searching for item is quite inefficient
     * @param targetItem item to refresh
     */
    public void refresh(IGUIItem targetItem) {
        itemReferences.entrySet()
                .stream()
                .filter(set -> set.getValue().getGUIItem() == targetItem)
                .map(Map.Entry::getKey)
                .forEach(this::refresh);
    }
}