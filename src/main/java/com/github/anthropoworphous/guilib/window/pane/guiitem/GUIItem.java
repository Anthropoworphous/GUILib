package com.github.anthropoworphous.guilib.window.pane.guiitem;

import com.github.anthropoworphous.guilib.window.Window;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class GUIItem implements IGUIItem {
    public GUIItem(ItemStack item) {
        this.item = item;
    }

    private ItemStack item;

    /**
     * Will not show on the gui that is already opened until refreshed or showed again
     */
    public void item(ItemStack item) {
        this.item = item;
    }
    public ItemStack item() { return item; }

    @Override
    public ItemStack getDisplayItem() {
        return item;
    }

    @Override
    public String toString() {
        return getDisplayItem().getType().toString();
    }

    public GUIItem copy() {
        return new GUIItem(item == null ? null : item.clone());
    }

    @Override
    public void onInitialised(Window win, Pane pane, int itemIndex) {}
    @Override
    public void onDraw(Window win, Pane pane, WindowSlot slot, int index) {}
    @Override
    public void onClick(Window clickedWindow, WindowSlot slot, Pane pane, InventoryClickEvent event) {}
    @Override
    public void onClose(Window closedWindow, WindowSlot slot, Pane pane, InventoryCloseEvent event) {}
}