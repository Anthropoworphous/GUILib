package com.github.anthropoworphous.guilib.window.pane;

import com.github.anthropoworphous.guilib.window.Window;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.guiitem.GUIItem;
import com.github.anthropoworphous.guilib.window.pane.guiitem.IGUIItem;
import com.github.anthropoworphous.guilib.window.pane.guiitem.util.XYIndex;
import com.github.anthropoworphous.guilib.window.pane.pattern.Pattern;
import com.github.anthropoworphous.owotoolkit.index.Indexed;
import com.github.anthropoworphous.owotoolkit.structure.tree.Connected;
import org.bukkit.inventory.Inventory;

public class Pane extends Connected<PaneItemCluster> implements Indexed {
    public Pane(int index, int width, int height, String name) {
        super(new PaneItemCluster(width * height));
        this.name = name;
        this.width = width;
        this.height = height;
        this.index = index;
    }
    public Pane(int index, int width, int height, String name, int pages) {
        super(new PaneItemCluster(width * height));
        this.name = name;
        this.width = width;
        this.height = height;
        this.index = index;
    }



    //field - don't forget about the connected value
    private final String name;
    private final int width, height;
    private int index;
    //end



    //getter & setter
    public String name() { return name; }
    public int width() { return width; }
    public int height() { return height; }
    public int size() { return width * height; }

    @Override
    public int index() { return index; }
    @Override
    public void index(int i) { index = i; }
    //end



    //set up
    public void addItem(int id, GUIItem item) {
        value().setItem(item, id);
    }

    /**
     * @param page yes page number start at 0 not 1, display is handled, so you don't have to worry about "page 0"
     * @param id   slot
     * @param item item to add
     */
    public void addItem(int page, int id, GUIItem item) {
        value().setItem(item, id, page);
    }

    public Pane pattern(Pattern.DefinedPattern pattern) {
        return pattern(0, pattern);
    }
    /**
     * @param page yes page number start at 0 not 1, display is handled, so you don't have to worry about "page 0"
     * @param pattern pattern to add
     * @return itself, for chaining
     */
    public Pane pattern(int page, Pattern.DefinedPattern pattern) {
        if (pattern.width() != width || pattern.height() != height) {
            throw new InvalidSizeException(this, pattern);
        }

        GUIItem[] items = pattern.mappedItems();

        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) { continue; }
            addItem(page, i, items[i]);
        }
        return this;
    }
    //end



    //draw
    private void checkForBound(Pane parent) {
        XYIndex pos = new XYIndex(parent.width(), index());
        XYIndex dim = pos.move(new XYIndex(parent.width(), width(), height()));

        if (parent.width < dim.x() || parent.height < dim.y()) {
            throw new InvalidSizeException(this, dim, parent);
        }
    }

    //convert local index to inventory index
    public int restoreIndex(int index) {
        if (ascent().isEmpty()) {
            return index;
        } else {
            Pane p = (Pane) ascent().get();
            XYIndex out = new XYIndex(width(), index);
            out.width(p.width());
            XYIndex offset = new XYIndex(p.width(), index());
            return p.restoreIndex(out.move(offset).toIndex());
        }
    }

    private GUIItem[] indexResolvedContent(Window win) {
        GUIItem[] out = new GUIItem[win.inv().getSize()];
        GUIItem[] content = value().get();
        for(int i = 0; i < content.length; i++) {
            if (content[i] == null) { continue; }

            int actualIndex = restoreIndex(i);
            out[actualIndex] = content[i];
            content[i].onInitialised(win, this, actualIndex);
        }

        return out;
    }

    /**
     * This function takes a window and returns an array of WindowSlots, which are the contents of the window,
     * with the contents of the descendants of this pane stacked on top of the contents of this pane.
     *
     * @param win The window to map
     * @return A WindowSlot[]
     */
    private WindowSlot[] map(Window win) {
        WindowSlot[] result = new WindowSlot[win.inv().getSize()];

        GUIItem[] indexResolvedContent = indexResolvedContent(win);
        for (int i = 0; i < indexResolvedContent.length; i++) {
            GUIItem item = indexResolvedContent[i];
            if (item == null) { continue; }
            if (result[i] == null) { result[i] = new WindowSlot(); }
            result[i].stack(item, this);
        }

        // if descents exist, add their contents
        descent().ifPresent(descendants -> {
            for (Connected<PaneItemCluster> descendant : descendants) {
                Pane p = (Pane) descendant;

                WindowSlot[] descendantResult = p.map(win);
                for (int i = 0; i < descendantResult.length; i++) {
                    if (descendantResult[i] == null) { continue; }
                    if (result[i] == null) { result[i] = new WindowSlot(); }
                    result[i].stack(descendantResult[i]);
                }
            }
        });

        return result;
    }

    /**
     * Put all the predefined GUIItems in an inventory
     * @param win the target window
     * @return everything displayed in the inventory
     */
    public WindowSlot[] draw(Window win) {
        WindowSlot[] result = map(win);

        for (int i = 0; i < result.length; i++) {
            WindowSlot slot = result[i];
            if (slot == null) {
                continue;
            }
            IGUIItem displayingItem = slot.getGUIItem();
            if (displayingItem == null) {
                continue;
            }

            displayingItem.onDraw(win, this, slot, i);
            win.inv().setItem(i, displayingItem.getDisplayItem());
        }

        return result;
    }

    /**
     * Simple draw that ignore all limit and just put a slot on a certain index in the inventory
     * @param inv inventory to draw on
     * @param index slot index that's being drawn
     * @param itemReferences itemReferences of a window
     */
    static public void draw(Inventory inv, int index, WindowSlot[] itemReferences) {
        inv.setItem(index, itemReferences[index].getGUIItem().getDisplayItem());
    }
    //end

    //checks for out of bound Connection
    @Override
    public Pane adopted(Connected<PaneItemCluster> parent) {
        checkForBound((Pane) parent);
        return (Pane) super.adopted(parent);
    }

    public static class InvalidSizeException extends IllegalStateException {
        public InvalidSizeException(Pane descendant, XYIndex dim, Pane holder) {
            super("Pane \"" + descendant.name() + "\"" + " require " +
                    dim.x() + "x" + dim.y() +
                    " slots, but Pane's parent \"" + holder.name + "\"" +
                    " only have " + holder.height + "x" + holder.width + " slots" +
                    " \n\"No!~ It won't fit ahhHHHouyhnhnm~\""
            );
        }
        public InvalidSizeException(Pane pane, Pattern.DefinedPattern pattern) {
            super("Pattern size isn't the same as pane " + pane.name() + "! " +
                    pattern.width() + "x" + pattern.height() + " isn't " +
                    pane.width() + "x" + pane.height()
            );
        }
    }
}