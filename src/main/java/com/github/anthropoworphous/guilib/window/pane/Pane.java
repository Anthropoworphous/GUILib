package com.github.anthropoworphous.guilib.window.pane;

import com.github.anthropoworphous.guilib.window.Window;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.guiitem.GUIItem;
import com.github.anthropoworphous.guilib.window.pane.guiitem.IGUIItem;
import com.github.anthropoworphous.guilib.window.pane.guiitem.other.ErrorGUIItem;
import com.github.anthropoworphous.guilib.window.pane.pattern.Pattern;
import main.index.ID;
import main.index.Index;
import main.index.XY;
import main.structure.tree.Connected;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Pane extends Connected {
    public Pane(Index location, int width, int height, String name, int pages) {
        super(new PaneItemCollection(pages));
        this.location = location;
        this.width = width;
        this.height = height;
        this.name = name;
    }
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

    @Override
    public @NotNull PaneItemCollection getValue() {
        return super.getValue(PaneItemCollection.class);
    }
    //end

    //set up
    public Pane addItem(Index id, GUIItem item) {
        getValue().addItem(item, id);
        return this;
    }
    public Pane pattern(Pattern.DefinedPattern pattern) {
        if (pattern.width() > width || pattern.height() > height) {
            throw new IllegalArgumentException("Pattern is too big for pane " + name + "! " +
                    pattern.width() + "x" + pattern.height() + " won't fit in " +
                    width + "x" + height
            );
        }
        pattern.mappedItems().forEach((y, map) -> map.forEach((x, item) -> addItem(new ID(y * width + x), item)));
        return this;
    }
    /**
     * @param page yes page number start at 0 not 1, display is handled, so you don't have to worry about "page 0"
     * @param id slot
     * @param item item to add
     * @return self
     */
    public Pane addItem(int page, Index id, GUIItem item) {
        getValue().addItem(item, id, page);
        return this;
    }
    /**
     * @param page yes page number start at 0 not 1, display is handled, so you don't have to worry about "page 0"
     * @param pattern pattern to add
     * @return self
     */
    public Pane pattern(int page, Pattern.DefinedPattern pattern) {
        if (pattern.width() > width || pattern.height() > height) {
            throw new IllegalArgumentException("Pattern is too big for pane " + name + "! " +
                    pattern.width() + "x" + pattern.height() + " won't fit in " +
                    width + "x" + height
            );
        }
        pattern.mappedItems().forEach((y, map) -> map.forEach((x, item) -> addItem(page, new ID(y * width + x), item)));
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

    public Index unoffset(Index id) {
        XY out = new XY(id.toIndex(), width);
        if (getParent().isEmpty()) {
            return out;
        } else {
            Pane p = (Pane) getParent().get();
            out.width(p.width);
            out.move(location);
            return p.unoffset(out);
        }
    }

    private Map<Index, GUIItem> unoffsettedContent(Window win) {
        Map<Index, GUIItem> out = new HashMap<>();
        getValue().get().forEach(
                (id, item) -> {
                    Index unoffseted = unoffset(id);
                    out.put(unoffseted, item);
                    item.onInitialised(win, this, unoffseted);
                }
        );
        return out;
    }

    private Map<Index, WindowSlot> map(Window win) {
        Map<Index, WindowSlot> result = new HashMap<>();

        for (Set<Connected> layer : toLayer()) {
            for (Connected pane : layer) {
                ((Pane) pane).unoffsettedContent(win).forEach((id, item) -> {
                    result.computeIfAbsent(id, i -> new WindowSlot((Pane) pane, new HashMap<>()));
                    WindowSlot slot = result.get(id);

                    if (slot.items().put(id.toIndex(), item) != null) {
                        slot.items().put(id.toIndex(), new ErrorGUIItem("GUIItem Collision", List.of(
                                Component.text().append(Component.text("click for more info")).build()
                        )));
                    }
                    result.put(new ID(id.toIndex()), slot);
                });
            }
        }

        return result;
    }

    /**
     * Put all the predefined GUIItems in an inventory
     * @param win the target window
     * @return everything displayed in the inventory
     */
    public Map<Index, WindowSlot> draw(Window win) {
        Map<Index, WindowSlot> result = map(win);

        result.forEach((index, slot) -> {
            IGUIItem displayingItem = slot.getGUIItem();
            displayingItem.onDraw(win, this, slot, index);
            win.inv().setItem(index.toIndex(), displayingItem.getDisplayItem());
        });

        return result;
    }

    /**
     * Simple draw that ignore all limit and just put a slot on a certain index in the inventory
     * @param inv inventory to draw on
     * @param index slot index that's being drawn
     * @param itemReferences itemReferences of a window
     */
    static public void draw(Inventory inv, Index index, Map<Index, WindowSlot> itemReferences) {
        inv.setItem(index.toIndex(), itemReferences.get(index).getGUIItem().getDisplayItem());
    }
    //end

    //checks for out of bound Connection
    @Override
    public Connected adopted(Connected parent) {
        checkForBound(parent);
        return super.adopted(parent);
    }
}