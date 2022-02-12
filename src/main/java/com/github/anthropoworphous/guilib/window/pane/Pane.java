package com.github.anthropoworphous.guilib.window.pane;

import com.github.anthropoworphous.guilib.interfaces.Localised;
import com.github.anthropoworphous.guilib.util.ID;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.guiitem.base.ErrorGUIItem;
import com.github.anthropoworphous.guilib.window.pane.guiitem.base.GUIItem;
import com.github.anthropoworphous.guilib.window.pane.pattern.Pattern;
import main.structure.tree.Connected;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pane extends Connected implements Localised {
    public Pane(ID location, int width, int height, String name) {
        super(new PaneItemCollection());
        this.location = location;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    //field - don't forget about the connected value
    private int width, height;
    private final ID location;
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
    public ID getLocation() {
        return location;
    }
    //end

    //set up
    public Pane addItem(ID id, GUIItem item) {
        getValue(PaneItemCollection.class).get().put(id, item);
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
        checkForBound(this.getParent());
    }

    private void checkForBound(Connected parent) {
        int shiftedWidth = width + location.getID() % ((Pane) parent).width;
        int shiftedHeight = width + height + location.getID() % ((Pane) parent).height;

        if (((Pane) parent).width < shiftedWidth && ((Pane) parent).height < shiftedHeight) {
            throw new IllegalStateException(
                    "Pane out of bound, Pane in layer " + this.getGeneration() +
                            " require at least " + shiftedWidth + "x" + shiftedHeight +
                            " slot, but Pane's parent in layer " + parent.getGeneration() +
                            " is only " + ((Pane) parent).width + "wide" +
                            "\"No!~ It won't fit ahhHHHunnmmhhhaaaa~"
            );
        }
    }

    private ID unoffset(ID id) {
        this.parentsWork(p -> {
            Pane parent = (Pane) p;
            id.move(parent.location.getID() + ((id.getID() / width) * (parent.width - width)));
        });
        return id;
    }

    private Map<ID, GUIItem> unoffsettedContent() {
        Map<ID, GUIItem> out = new HashMap<>();
        getValue(PaneItemCollection.class).get().forEach(
                (id, item) -> out.put(unoffset(id), item)
        );
        return out;
    }

    private Map<ID, WindowSlot> map() {
        Map<ID, WindowSlot> result = new HashMap<>();

        this.toLayer().forEach(l -> l.forEach(p -> ((Pane) p).unoffsettedContent().forEach((id, item) -> {
            WindowSlot slot = new WindowSlot((Pane) p, new HashMap<>());

            if (slot.items().put(id.getID(), item) != null) {
                slot.items().put(id.getID(), new ErrorGUIItem("GUIItem Collision", List.of(
                        Component.text().append(Component.text("At depth: " + this.getGeneration())).build(),
                        Component.text().append(Component.text("At slot: " + id)).build()
                )));
            }

            result.put(id, slot);
        })));

        return result;
    }
    //end

    /**
     * Put all the predefined GUIItems in an inventory
     * @param inv the target inventory
     * @return everything displayed in the inventory
     */
    public Map<ID, WindowSlot> draw(Inventory inv) {
        Map<ID, WindowSlot> result = map();

        result.forEach((id, slot) -> inv.setItem(id.getID(), slot.get().getDisplayItem()));

        return result;
    }

    //Localise
    @Override public ID location() { return location; }
    @Override public ID move(int offset) { return location.move(offset); }
    //end

    //checks for out of bound Connection
    @Override
    public Connected adopted(Connected parent) {
        checkForBound(parent);
        return super.adopted(parent);
    }
}