package com.github.anthropoworphous.guilib.window;

import org.bukkit.Bukkit;
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
        Bukkit.getLogger().info(p.getName() + " clicked in inv");

        Window window = Window.getActiveGUI().get(p.getUniqueId()).getKey();
        Inventory inv = Window.getActiveGUI().get(p.getUniqueId()).getValue();

        if (inv == event.getClickedInventory()) {
            Bukkit.getLogger().info("no prob");

            event.setCancelled(true);

            WindowSlot slot = window.get(event.getSlot());

            if (slot == null) {
                Bukkit.getLogger().info("empty slot");
                Bukkit.getLogger().info("clicked: " + event.getSlot());
                window.getGuiItemReferences().forEach((k,v) -> {
                    if (v == null || v.get() == null || v.get().getDisplayItem() == null) {
                        Bukkit.getLogger().info(k.getID() + " || null");
                    } else {
                        Bukkit.getLogger().info(k.getID() + " || " + v.get().getDisplayItem().getType());
                    }
                });
                return;
            }

            slot.get().onClick(window, slot, slot.pane(), event);
        } else {
            Bukkit.getLogger().info("yes prob");
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
