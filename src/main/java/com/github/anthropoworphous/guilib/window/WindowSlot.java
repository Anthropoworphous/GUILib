package com.github.anthropoworphous.guilib.window;

import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.guiitem.base.GUIItem;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public record WindowSlot(Pane pane, Map<Integer, GUIItem> items) {
    public GUIItem get() {
        Optional<Integer> maxKey = items.keySet().stream().max(Comparator.naturalOrder());
        return maxKey.map(items::get).orElse(null);
    }
}
