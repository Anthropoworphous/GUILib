package com.github.anthropoworphous.guilib.window.pane.guiitem.util;

public class XYIndex {
    private final int width, x, y;

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

    public int toIndex() {
        return (y-1) * width + x-1;
    }
}
