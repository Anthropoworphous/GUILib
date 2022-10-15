package com.github.anthropoworphous.guilib.window;

import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.guiitem.IGUIItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Window {
    public Window(GUI gui) {
        this.gui = gui;
        inv = gui.getInventoryBuilder().getInventory();
        content = gui.getMainPane().draw(this);
    }

    private final GUI gui;
    private final Inventory inv;
    private WindowSlot[] content;

    //getter
    public GUI gui() { return gui; }
    public Inventory inv() { return inv; }
    public WindowSlot[] content() { return content; }
    //end

    public void show(Player viewer) {
        GUI.getActiveWindows().put(inv, this);
        viewer.openInventory(inv);
    }

    public void reload() {
        inv.clear();
        content = gui.getMainPane().draw(this);
    }

    /**
     * Refresh GUIItem, if changes where made to the displayed item this will update it
     * @param targetIndex item to refresh
     */
    public void refresh(int targetIndex) {
        Pane.draw(inv, targetIndex, content);
    }

    /**
     * Refresh GUIItem, if changes where made to the displayed item this will update it
     * Try to use index if possible, searching for item is quite inefficient
     * @param targetItem item to refresh
     */
    public void refresh(IGUIItem targetItem) {
        for (int i = 0; i < content.length; i++) {
            if (content[i].getGUIItem() == targetItem) {
                refresh(i);
            }
        }
    }
}