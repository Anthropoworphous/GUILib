package com.github.anthropoworphous.guilib.window;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Optional;

public class GUIListener implements Listener {
    /**
     * register this if you want to use the events
     */
    public GUIListener() {}

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Window win = GUI.getActiveWindows().get(event.getInventory());
        if (win != null && win.inv() == event.getClickedInventory()) {
            event.setCancelled(true);
            WindowSlot slot = win.content()[event.getSlot()];

            Optional.ofNullable(slot)
                    .map(WindowSlot::getGUIItem)
                    .ifPresent(s -> s.onClick(win, slot, slot.topPane(), event));
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Window win = GUI.getActiveWindows().get(event.getInventory());

        if (win != null && win.inv() == event.getInventory()) {
            if (win.inv().getViewers().size() == 0) {
                GUI.getActiveWindows().remove(win.inv()); //window garbage collector lol
            }
            for (int i = 0; i < win.content().length; i++) {
                WindowSlot slot = win.content()[i];

                Optional.ofNullable(slot)
                        .map(WindowSlot::getGUIItem)
                        .ifPresent(s -> s.onClose(win, slot, slot.topPane(), event));
            }
        }
    }
}