package com.github.anthropoworphous.guilib.io;

import com.github.anthropoworphous.guilib.window.Window;
import com.github.anthropoworphous.guilib.window.inventory.InventoryBuilder;

//tbh I could have just use transient, but I think that look messy
public class WindowJsonConverter {
    public WindowJsonConverter(Window win) {
        name = win.getName();
        invBuilder = win.getInventoryBuilder();

        pane = new PaneJsonConverter(win.getMainPane());
    }

    private final String name;
    private final PaneJsonConverter pane;
    private final InventoryBuilder invBuilder;

    public Window fromJson() {
        return new Window(invBuilder, pane.fromJson(), name);
    }
}
