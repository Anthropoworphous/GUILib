package com.github.anthropoworphous.guilib.window.pane;

import com.github.anthropoworphous.guilib.window.pane.guiitem.GUIItem;

import java.util.ArrayList;
import java.util.List;

public class PaneItemCluster {
    public PaneItemCluster(int size) { this.size = size; }
    public PaneItemCluster(int size, int pageLimit) {
        this.size = size;
        this.pageLimit = pageLimit;
    }

    //field
    private final int size;
    private int pageNumber = 0;
    private int pageLimit = 1;
    private final List<GUIItem[]> content = new ArrayList<>(); //page<slots>
    //end

    //getter
    public int pageNumber() {
        return pageNumber;
    }
    public int pageLimit() {
        return pageLimit;
    }
    //end

    private void populatePages(int targetPage) {

    }

    public GUIItem[] get(int pageNumber) {
        populatePages(pageNumber);
        return content.get(pageNumber);
    }
    public GUIItem[] get() {
        return get(pageNumber);
    }

    /**
     * put item in pane
     * @param item item to put in pane, null for empty slot
     * @param id where to put the item
     */
    public void setItem(GUIItem item, int id) { get()[id] = item; }
    /**
     * put item in pane
     * @param item item to put in pane, null for empty slot
     * @param id where to put the item
     * @param page which page the item is in
     */
    public void setItem(GUIItem item, int id, int page) {
        get(page)[id] = item;
    }

    //page
    public void next() {
        if (pageNumber < pageLimit-1) {
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
