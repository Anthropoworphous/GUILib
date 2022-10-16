package com.github.anthropoworphous.guilib.window.pane.guiitem.util;

public class XYIndex {
    private int width, x, y;

    public XYIndex(int width, int x, int y) {
        this.width = width;
        this.x = x;
        this.y = y;
    }
    public XYIndex(int width, int id) {
        this.width = width;
        this.x = id % width + 1;
        this.y = id / width + 1;
    }

    public int width() { return width; }
    public int x() { return x; }
    public int y() { return y; }
    public void width(int width) { this.width = width; }
    public void x(int x) { this.x = x; }
    public void y(int y) { this.y = y; }

    public void resize(int width) {
        int index = this.toIndex();
        this.width = width;
        x = index % width;
        y = index / width;
    }

    public XYIndex move(XYIndex offset) {
        x += offset.x() - 1;
        y += offset.y() - 1;
        return this;
    }

    public int toIndex() {
        return (y - 1) * width + (x - 1);
    }
}
