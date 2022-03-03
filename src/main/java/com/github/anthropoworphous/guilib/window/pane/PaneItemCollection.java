package com.github.anthropoworphous.guilib.window.pane;

import com.github.anthropoworphous.guilib.window.pane.guiitem.GUIItem;
import main.index.Index;
import main.structure.tree.IConnectable;

import java.util.*;

public class PaneItemCollection implements IConnectable {
    public PaneItemCollection() {}
    public PaneItemCollection(int pageLimit) {
        this.pageLimit = pageLimit;
    }

    private int pageNumber = 0;
    private int pageLimit = 1;
    private final Map<Integer, Map<Index, GUIItem>> content = new HashMap<>();

    //getter
    public List<Map<Index, GUIItem>> content() {
        int max = content.keySet().stream().max(Comparator.comparingInt(x -> x)).orElse(0);
        List<Map<Index, GUIItem>> result = new ArrayList<>(max);
        content.forEach(result::set);
        return result;
    }
    //end

    private void mkdir(int targetPage) {
        if (content.size() < pageLimit) {
            content.computeIfAbsent(targetPage, ignore -> new HashMap<>());
        }
    }

    public Map<Index, GUIItem> get(int pageNumber) {
        mkdir(pageNumber);
        return content.get(pageNumber);
    }
    public Map<Index, GUIItem> get() {
        return get(pageNumber);
    }

    /**
     * put item in pane
     * @param item item to put in pane, null for empty slot
     * @param position where to put the item
     */
    public void addItem(GUIItem item, Index position) {
        get().put(position, item);
    }
    /**
     * put item in pane
     * @param item item to put in pane, null for empty slot
     * @param position where to put the item
     * @param page which page the item is in
     */
    public void addItem(GUIItem item, Index position, int page) {
        get(page).put(position, item);
    }

    //page
    public void next() {
        if (pageNumber < content.size()-1) {
            pageNumber++;
        }
    }
    public void previous() {
        if (pageNumber > 0) {
            pageNumber--;
        }
    }
    public void jumpTo(int pageNumber) {
        if (pageNumber >= 0 && pageNumber < content.size()) {
            this.pageNumber = pageNumber;
        }
    }
    public int currentPage() { return pageNumber; }
    public int displayPage() { return pageNumber + 1; }
    //end

    @Override
    public String toString() {
        return super.toString();
    }
}
