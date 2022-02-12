package com.github.anthropoworphous.guilib.io;

import com.github.anthropoworphous.guilib.util.ID;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.PaneItemCollection;

public class PaneJsonConverter {
    public PaneJsonConverter(Pane p) {
        name = p.getName();
        location = p.getLocation();
        height = p.getHeight();
        width = p.getWidth();

        paneItems = new PaneItemCollectionJsonConverter(p.getValue(PaneItemCollection.class));
    }

    private final ID location;
    private final int width;
    private final int height;
    private final String name;

    private final PaneItemCollectionJsonConverter paneItems;

    public Pane fromJson() {
        Pane result = new Pane(location, width, height, name);
        result.setValue(paneItems.fromJson());
        return result;
    }
}
