package com.github.anthropoworphous.guilib.window;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class WindowListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();

        Window window = Window.getActiveGUI().get(p.getUniqueId()).getKey();
        Inventory inv = Window.getActiveGUI().get(p.getUniqueId()).getValue();

        if (inv == event.getClickedInventory()) {
            event.setCancelled(true);

            WindowSlot slot = window.get(event.getSlot());

            if (slot != null) {
                slot.get().onClick(window, slot, slot.pane(), event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player p = (Player) event.getPlayer();

        if (Window.getActiveGUI().get(p.getUniqueId()).getValue() == event.getInventory()) {
            Window window = Window.getActiveGUI().get(p.getUniqueId()).getKey();

            window.getGuiItemReferences().forEach(
                    (doNotCare, item) -> item.get().onClose(window, item, item.pane(), event)
            );
        }
    }
}
