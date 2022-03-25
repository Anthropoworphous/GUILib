package com.github.anthropoworphous.guilib.window.pane.guiitem.other;

import com.github.anthropoworphous.guilib.GUILib;
import com.github.anthropoworphous.guilib.window.Window;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.guiitem.GUIItem;
import main.index.ID;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class TriggerGUIItem extends GUIItem {
    /**
     * Basically a button, but in gui
     * @param baseItem the item to show when this item is not clicked
     * @param reactItem what the item will change into when clicked
     * @param coolDown ticks for the button to be clickable again
     * @param execute thing to run
     */
    public TriggerGUIItem(ItemStack baseItem, ItemStack reactItem, int coolDown, Runnable execute) {
        super(baseItem);
        this.reactItem = reactItem;
        this.coolDown = coolDown;
        this.execute = execute;
    }

    private final int coolDown;
    private final ItemStack reactItem;
    private final Runnable execute;
    private boolean clicked = false;

    @Override
    public ItemStack getDisplayItem() {
        return clicked ? reactItem : super.getDisplayItem();
    }

    @Override
    public final void onClick(Window clickedWindow, WindowSlot slot, Pane pane, InventoryClickEvent event) {
        super.onClick(clickedWindow, slot, pane, event);

        if (clicked) { return; }
        execute.run();
        clicked = true;

        clickedWindow.refresh(new ID(event.getSlot()));
        new BukkitRunnable() {
            @Override
            public void run() {
                clicked = false;
                clickedWindow.refresh(new ID(event.getSlot()));
            }
        }.runTaskLater(GUILib.getPlugin(), coolDown);
    }
}
