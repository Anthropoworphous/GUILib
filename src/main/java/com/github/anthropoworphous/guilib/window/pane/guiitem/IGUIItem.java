package com.github.anthropoworphous.guilib.window.pane.guiitem;

import com.github.anthropoworphous.guilib.window.Window;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public interface IGUIItem {
    ItemStack getDisplayItem();

    /**
     * Called whenever the GUI containing this item tried to show
     * @param win the window the item was created for
     * @param pane the pane this item is in
     * @param itemIndex the inventory(not pane) index the item will be in
     */
    void onInitialised(Window win, Pane pane, int itemIndex);
    /**
     * Called whenever this item is actually displayed
     * @param win the window this item will be in
     * @param slot the slot it's put in
     * @param pane the pane this item is in
     * @param index the slot this item is in
     */
    void onDraw(Window win, Pane pane, WindowSlot slot, int index);
    void onClick(Window clickedWindow, WindowSlot slot, Pane pane, InventoryClickEvent event);
    void onClose(Window closedWindow, WindowSlot slot, Pane pane, InventoryCloseEvent event);
    IGUIItem copy();
}
