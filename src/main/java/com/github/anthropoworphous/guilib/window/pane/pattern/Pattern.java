package com.github.anthropoworphous.guilib.window.pane.pattern;

import com.github.anthropoworphous.guilib.window.pane.guiitem.GUIItem;
import com.github.anthropoworphous.guilib.window.pane.guiitem.util.XYIndex;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//define items
public class Pattern {

    private final Map<Integer, GUIItem> defined = new HashMap<>();

    public Pattern define(int index, @Nullable GUIItem item) {
        defined.put(index, item);
        return this;
    }

    public PatternBuilder shaping() {
        return new PatternBuilder(defined);
    }

    //add items
    public static class PatternBuilder {
        public PatternBuilder(Map<Integer, GUIItem> defined) {
            this.defined = defined;
        }

        private final Map<Integer, GUIItem> defined;
        private final List<GUIItem> items = new ArrayList<>();
        private int width, tempWidth = 0;
        private int height = 1;

        public PatternBuilder layout(int... index) {
            for (int i : index) {
                items.add(defined.get(i).copy());
                tempWidth++;
            }
            return this;
        }
        public PatternBuilder nextLine() {
            width = Math.max(width, tempWidth);
            tempWidth = 0;
            height++;
            return this;
        }

        public DefinedPattern build() {
            width = Math.max(width, tempWidth);
            if (tempWidth == 0) { height--; }
            return new DefinedPattern(items.toArray(GUIItem[]::new), width, height);
        }
    }

    //defined, ready for use
    public static record DefinedPattern(GUIItem[] mappedItems, int width, int height) {}
}
