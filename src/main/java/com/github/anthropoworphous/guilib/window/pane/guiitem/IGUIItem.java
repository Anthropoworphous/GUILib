package com.github.anthropoworphous.guilib.window.pane.guiitem;

import com.github.anthropoworphous.guilib.window.Window;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import main.index.Index;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface IGUIItem {
    ItemStack getDisplayItem();

    /**
     * Called whenever the window containing this item tried to show
     * @param inventory the inventory that's connected to the window
     * @param window the window that's calling for this item
     * @param pane the pane this item is in
     */
    void onInitialised(Inventory inventory, Window window, Pane pane);
    /**
     * Called whenever this item is actually displayed
     * @param inventory the inventory that's connected to the window
     * @param window the window that's calling for this item
     * @param slot the slot it's put in
     * @param pane the pane this item is in
     */
    void onDrew(Inventory inventory, Window window, Pane pane, WindowSlot slot, Index index);
    void onClick(Window clickedWindow, WindowSlot slot, Pane pane, InventoryClickEvent event);
    void onClose(Window closedWindow, WindowSlot slot, Pane pane, InventoryCloseEvent event);
    IGUIItem copy();
}
