package com.github.anthropoworphous.guilib.window;

import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.guiitem.IGUIItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public record WindowSlot(LinkedList<StackedItem> stack) {
    public WindowSlot() {
        this(new LinkedList<>());
    }

    public IGUIItem getGUIItem() {
        // getLast because we stack from the root (which is the bottom layer)
        return stack.getLast().item();
    }

    public void stack(@NotNull IGUIItem guiItem, Pane producer) throws OverlappingItemsException {
        if (!producer.isOrphan() &&
                stack.stream().anyMatch(i -> i.producer().ascent().equals(producer.ascent()))
        ) {
            throw new OverlappingItemsException(producer.name());
        }
        stack.add(new StackedItem(guiItem, producer));
    }
    public void stack(@NotNull WindowSlot slot) throws OverlappingItemsException {
        for (StackedItem item : slot.stack()) {
            if (!item.producer().isOrphan() &&
                    stack.stream().anyMatch(i -> i.producer().ascent().equals(item.producer().ascent()))
            ) {
                throw new OverlappingItemsException(item.producer().name());
            }
        }
        stack.addAll(slot.stack());
    }

    public Pane topPane() { return stack.getLast().producer; }

    public record StackedItem(IGUIItem item, Pane producer) {};

    public static class OverlappingItemsException extends IllegalStateException {
        public OverlappingItemsException(String parentPaneName) {
            super("item from 2 panes overlapped at the same level in pane: %s".formatted(parentPaneName));
        }
    }
}
