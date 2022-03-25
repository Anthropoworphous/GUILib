package com.github.anthropoworphous.guilib.window;

import main.index.ID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Window win = GUI.getActiveWindows().get(event.getInventory());
        if (win != null && win.inv() == event.getClickedInventory()) {
            event.setCancelled(true);
            WindowSlot slot = win.itemReferences().get(new ID(event.getSlot()));

            slot.getGUIItem().onClick(win, slot, slot.pane(), event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Window win = GUI.getActiveWindows().get(event.getInventory());

        if (win != null && win.inv() == event.getInventory()) {
            if (win.inv().getViewers().size() == 0) {
                GUI.getActiveWindows().remove(win.inv()); //Garbage window collector lol
            }
            win.itemReferences().values().forEach(item -> item.getGUIItem().onClose(win, item, item.pane(), event));
        }
    }
}
