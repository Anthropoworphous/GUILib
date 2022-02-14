package com.github.anthropoworphous.guilib.window.pane.guiitem.base;

import com.github.anthropoworphous.guilib.window.Window;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ErrorGUIItem extends GUIItem {
    public ErrorGUIItem(String error, List<Component> debugInfo) {
        super(
                Material.RED_STAINED_GLASS_PANE,
                1,
                Component.text().append(Component.text(error)).build(),
                debugInfo
        );
    }

    @Override
    public void onClick(Window clickedWindow, WindowSlot slot, Pane pane, InventoryClickEvent event) {
        if (event.getClickedInventory() == null) { return; }
        event.getClickedInventory().close();

        TextComponent.Builder print = Component.text();

        print.append(Component.text("StackTrace or something lmao have fun debugging:"));

        pane.parentsWork(c -> print.append(
                Component.text("\tAt: " + ((Pane) c).getName() + " || Layer: " + c.getGeneration())));

        print.append(Component.text("Environment:"))
                .append(Component.text("\tWindow: " + clickedWindow.getName()))
                .append(Component.text("\tClick: " + event.getClick()))
                .append(Component.text("\tSlot Index: " + event.getSlot()))
                .append(Component.text("\tRaw slot Index: " + event.getRawSlot()));

        event.getWhoClicked().sendMessage(print.build());
    }
}
