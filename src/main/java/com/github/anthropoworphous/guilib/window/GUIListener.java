package com.github.anthropoworphous.guilib.window;

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
            WindowSlot slot = win.content()[event.getSlot()];

            if (slot.getGUIItem() != null) {
                slot.getGUIItem().onClick(win, slot, slot.topPane(), event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Window win = GUI.getActiveWindows().get(event.getInventory());

        if (win != null && win.inv() == event.getInventory()) {
            if (win.inv().getViewers().size() == 0) {
                GUI.getActiveWindows().remove(win.inv()); //Garbage window collector lol
            }
            for (int i = 0; i < win.content().length; i++) {
                WindowSlot slot = win.content()[i];
                if (slot.getGUIItem() != null) {
                    slot.getGUIItem().onClose(win, slot, slot.topPane(), event);
                }
            }
        }
    }
}
