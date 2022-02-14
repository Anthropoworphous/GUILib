package com.github.anthropoworphous.guilib.window.pane.pattern;

import com.github.anthropoworphous.guilib.window.pane.guiitem.base.GUIItem;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
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
        private final Map<Integer, Map<Integer, GUIItem>> items = new HashMap<>();
        private int pointerX, pointerY, width = 0;

        public PatternBuilder next(int index) {
            if (defined.get(index) != null) {
                items.computeIfAbsent(pointerY, k -> new HashMap<>());
                items.get(pointerY).put(pointerX, defined.get(index).copy());
            }
            pointerX++;
            return this;
        }
        public PatternBuilder nextLine() {
            width = Math.max(width, pointerX);
            pointerX = 0;
            pointerY++;
            return this;
        }

        public DefinedPattern build() {
            return new DefinedPattern(items, width);
        }
    }

    //defined, ready for use
    public static record DefinedPattern(Map<Integer, Map<Integer, GUIItem>> mappedItems, int width) {}
}
