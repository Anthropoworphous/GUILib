package com.github.anthropoworphous.guilib.window.pane;

import com.github.anthropoworphous.guilib.interfaces.Localised;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.guiitem.base.ErrorGUIItem;
import com.github.anthropoworphous.guilib.window.pane.guiitem.base.GUIItem;
import com.github.anthropoworphous.guilib.window.pane.pattern.Pattern;
import main.index.ID;
import main.index.Index;
import main.index.XY;
import main.structure.tree.Connected;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pane extends Connected implements Localised {
    public Pane(Index location, int width, int height, String name) {
        super(new PaneItemCollection());
        this.location = location;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    //field - don't forget about the connected value
    private int width, height;
    private final Index location;
    private final String name;
    //end

    //getter
    public String getName() {
        return name;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public Index getLocation() {
        return location;
    }
    //end

    //set up
    public Pane addItem(Index index, GUIItem item) {
        getValue(PaneItemCollection.class).get().put(index, item);
        return this;
    }
    public Pane pattern(Pattern.DefinedPattern pattern) {
        if (pattern.width() > width) {
            throw new IllegalArgumentException("Pattern is too wide for pane " + name + "! " + width + "<" + pattern.width());
        }
        pattern.mappedItems().forEach((y, map) -> map.forEach((x, item) -> addItem(new ID(y * width + x), item)));
        return this;
    }
    //end

    //draw
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        this.getParent().ifPresent(this::checkForBound);
    }

    private void checkForBound(Connected parent) {
        Pane p = (Pane) parent;
        XY xyPos = new XY(location, p.width);
        XY xyDimension = xyPos.offset(width-1, height-1);

        if (p.width < xyDimension.x() || p.height < xyDimension.y()) {
            throw new IllegalStateException(
                    "Pane \"" + name + "\" in layer " + getGeneration() +
                            " require " + xyDimension.x() + "x" + xyDimension.y() +
                            " slots, but Pane's parent \"" + p.name + "\"  in layer " + p.getGeneration() +
                            " only have " + p.height + "x" + p.width + " slots" +
                            " \n\t\t\"No!~ It won't fit ahhHHHouyhnhnm~\""
            );
        }
    }

    public Index unoffset(Index index) {
        XY out = new XY(index, width);
        if (getParent().isEmpty()) {
            return out;
        } else {
            Pane p = (Pane) getParent().get();
            out.width(p.width);
            out.move(location);
            return p.unoffset(out);
        }
    }

    private Map<Index, GUIItem> unoffsettedContent() {
        Map<Index, GUIItem> out = new HashMap<>();
        getValue(PaneItemCollection.class).get().forEach(
                (id, item) -> out.put(unoffset(id), item)
        );
        return out;
    }

    private Map<Index, WindowSlot> map() {
        Map<Index, WindowSlot> result = new HashMap<>();

        this.toLayer().forEach(l -> l.forEach(p -> ((Pane) p).unoffsettedContent().forEach((index, item) -> {
            WindowSlot slot = new WindowSlot((Pane) p, new HashMap<>());

            if (slot.items().put(index.toIndex(), item) != null) {
                slot.items().put(index.toIndex(), new ErrorGUIItem("GUIItem Collision", List.of(
                        Component.text().append(Component.text("At depth: " + this.getGeneration())).build(),
                        Component.text().append(Component.text("At slot: " + index)).build()
                )));
            }

            result.put(index, slot);
        })));

        return result;
    }
    //end

    /**
     * Put all the predefined GUIItems in an inventory
     * @param inv the target inventory
     * @return everything displayed in the inventory
     */
    public Map<Index, WindowSlot> draw(Inventory inv) {
        Map<Index, WindowSlot> result = map();

        result.forEach((index, slot) -> inv.setItem(index.toIndex(), slot.getGUIItem().getDisplayItem()));

        return result;
    }

    //Localise
    @Override public Index location() { return location; }
    @Override public Index move(int offset) { return location.move(offset); }
    //end

    //checks for out of bound Connection
    @Override
    public Connected adopted(Connected parent) {
        checkForBound(parent);
        return super.adopted(parent);
    }
}