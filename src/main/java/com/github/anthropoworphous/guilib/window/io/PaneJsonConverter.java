package com.github.anthropoworphous.guilib.window.io;

import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.PaneItemCollection;
import main.index.ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaneJsonConverter {
    public PaneJsonConverter(Pane p) {
        name = p.getName();
        location = new ID(p.getLocation());
        height = p.getHeight();
        width = p.getWidth();
        paneItems = p.getValue(PaneItemCollection.class);

        if (!p.isChildless()) {
            Objects.requireNonNull(p.getChild())
                    .forEach(c -> child.add(new PaneJsonConverter((Pane) c)));
        }
    }

    private final ID location;
    private final int width;
    private final int height;
    private final String name;
    private final List<PaneJsonConverter> child = new ArrayList<>();
    private final PaneItemCollection paneItems;

    public Pane fromJson() {
        Pane result = new Pane(location, width, height, name);
        result.setValue(paneItems);
        child.forEach(c -> result.adopt(c.fromJson()));
        return result;
    }
}
