package com.github.anthropoworphous.guilib.window.pane.guiitem.base;

import com.github.anthropoworphous.guilib.window.Window;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUIItem {
    public GUIItem(Material material, int amount, Component name, List<Component> lore) {
        backGroundItem = buildItemStack(material, amount, name, lore);
    }
    public GUIItem(ItemStack item) {
        backGroundItem = item;
    }
    public GUIItem() {}

    protected ItemStack buildItemStack(Material material, int amount, Component name, List<Component> lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(name);
        meta.lore(lore);

        item.setItemMeta(meta);

        return item;
    }

    private ItemStack foreGroundItem, backGroundItem = null;

    protected void foreGroundItem(ItemStack item) { foreGroundItem = item; }
    protected ItemStack foreGroundItem() { return foreGroundItem; }
    protected void backGroundItem(ItemStack item) { backGroundItem = item; }
    protected ItemStack backGroundItem() { return backGroundItem; }

    public ItemStack getDisplayItem() {
        return (foreGroundItem == null || foreGroundItem.getType().isAir()) ? backGroundItem : foreGroundItem;
    }

    @Override
    public String toString() {
        return getDisplayItem().getType().toString();
    }

    public void onClick(Window clickedWindow, WindowSlot slot, Pane pane, InventoryClickEvent event) {}
    public void onClose(Window closedWindow, WindowSlot slot, Pane pane, InventoryCloseEvent event) {}
}