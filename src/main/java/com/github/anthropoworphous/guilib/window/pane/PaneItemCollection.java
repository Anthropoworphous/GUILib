package com.github.anthropoworphous.guilib.window.pane;

import com.github.anthropoworphous.guilib.interfaces.Paginated;
import com.github.anthropoworphous.guilib.util.ID;
import com.github.anthropoworphous.guilib.window.pane.guiitem.base.GUIItem;
import main.structure.tree.IConnectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaneItemCollection extends ArrayList<Map<ID, GUIItem>> implements IConnectable, Paginated {
    public PaneItemCollection() {
        add(new HashMap<>());
    }
    public PaneItemCollection(int pageLimit) {
        for (int i = pageLimit; i > 0; i--) {
            add(new HashMap<>());
        }
    }

    private int pageNumber = 1;

    /**
     * put item in pane
     * @param item item to put in pane, null for empty slot
     * @param position where to put the item
     * @return previous value at the position if it's not null, return null otherwise
     */
    public GUIItem addItem(GUIItem item, ID position) {
        return get(pageNumber).put(position, item);
    }
    /**
     * put item in pane
     * @param item item to put in pane, null for empty slot
     * @param position where to put the item
     * @param page which page the item is in
     * @return previous value at the position if it's not null, return null otherwise
     */
    public GUIItem addItem(GUIItem item, ID position, int page) {
        return get(page).put(position, item);
    }

    @Override
    public Map<ID, GUIItem> get(int pageNumber) {
        return (pageNumber > size()) ? null : super.get(pageNumber - 1);
    }

    public Map<ID, GUIItem> get() {
        return super.get(pageNumber - 1);
    }

    //page
    @Override public void next() {
        if (pageNumber < size()) {
            pageNumber++;
        }
    }
    @Override public void previous() {
        if (pageNumber > 1) {
            pageNumber--;
        }
    }
    @Override public void jumpTo(int pageNumber) {
        if (pageNumber >= 1 && pageNumber <= size())
        this.pageNumber = pageNumber;
    }
    @Override public int currentPage() { return pageNumber; }
    //end

    @Override
    public String toString() {
        return super.toString();
    }
}
