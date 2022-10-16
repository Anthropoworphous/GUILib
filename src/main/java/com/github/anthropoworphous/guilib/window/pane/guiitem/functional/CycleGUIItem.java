package com.github.anthropoworphous.guilib.window.pane.guiitem.functional;

import com.github.anthropoworphous.guilib.window.Window;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.guiitem.GUIItem;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class CycleGUIItem extends GUIItem {
    public CycleGUIItem(List<ItemStack> items, int cycleSpeed, Plugin plugin) {
        this(items, cycleSpeed, 0, plugin);
    }
    public CycleGUIItem(List<ItemStack> items, int cycleSpeed, int cycleOffset, Plugin plugin) {
        super(null);
        this.items = items;
        this.cycleSpeed = cycleSpeed;
        this.cycleOffset = cycleOffset;
        this.plugin = plugin;
    }

    private final List<ItemStack> items;
    private int cycleIndex = 0;
    private final int cycleSpeed, cycleOffset;
    private final Plugin plugin;
    private BukkitTask cycle;


    @Override
    public ItemStack getDisplayItem() {
        return items.get(cycleIndex);
    }

    @Override
    public final void onDraw(Window win, Pane pane, WindowSlot slot, int index) {
        super.onDraw(win, pane, slot, index);
        cycle = new BukkitRunnable() {
            @Override
            public void run() {
                cycleIndex = (cycleIndex+1 >= items.size() ? 0 : cycleIndex+1);
                win.refresh(index);
            }
        }.runTaskTimer(plugin, cycleOffset, cycleSpeed);
    }

    @Override
    public final void onClose(Window closedWindow, WindowSlot slot, Pane pane, InventoryCloseEvent event) {
        super.onClose(closedWindow, slot, pane, event);
        if (cycle != null) {
            cycle.cancel();
        }
    }
}
