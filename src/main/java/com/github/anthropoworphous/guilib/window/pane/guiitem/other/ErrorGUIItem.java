package com.github.anthropoworphous.guilib.window.pane.guiitem.other;

import com.github.anthropoworphous.guilib.window.Window;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.guiitem.GUIItem;
import com.github.anthropoworphous.guilib.window.pane.guiitem.util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ErrorGUIItem extends GUIItem {
    public ErrorGUIItem(String error, List<Component> debugInfo) {
        super(Util.Item.Create.create(
                Material.RED_STAINED_GLASS_PANE,
                1,
                Component.text().append(Component.text(error)).build(),
                debugInfo
        ));
    }

    @Override
    public void onClick(Window clickedWindow, WindowSlot slot, Pane pane, InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == null) { return; }
        event.getClickedInventory().close();

        p.sendMessage("StackTrace or something lmao have fun debugging:");

        pane.parentsWork(c -> p.sendMessage("    At: " + ((Pane) c).name() + " || Layer: " + c.getGeneration()));

        p.sendMessage("Environment:");
        p.sendMessage(Component.text("    GUI: " + clickedWindow.gui().getName()));
        p.sendMessage(Component.text("    Click: " + event.getClick()));
        p.sendMessage(Component.text("    Slot Index: " + event.getSlot()));
        p.sendMessage(Component.text("    Raw slot Index: " + event.getRawSlot()));
    }
}
