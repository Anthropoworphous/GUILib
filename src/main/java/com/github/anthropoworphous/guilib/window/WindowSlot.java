package com.github.anthropoworphous.guilib.window;

import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.guiitem.IGUIItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public record WindowSlot(LinkedList<StackedItem> stack) {
    public IGUIItem getGUIItem() {
        return stack.getLast().item();
    }

    public void stack(@NotNull IGUIItem guiItem, Pane producer) throws OverlappingItemsException {
        if (!producer.isOrphan() &&
                stack.stream().anyMatch(i -> i.producer().ascent().equals(producer.ascent()))
        ) {
            throw new OverlappingItemsException(producer.name());
        }
        // addFirst because we stack from the descendants
        stack.addFirst(new StackedItem(guiItem, producer));
    }

    public Pane topPane() { return stack.getLast().producer; }

    private record StackedItem(IGUIItem item, Pane producer) {};

    public static class OverlappingItemsException extends IllegalStateException {
        public OverlappingItemsException(String parentPaneName) {
            super("item from 2 panes overlapped at the same level in pane: %s".formatted(parentPaneName));
        }
    }
}
