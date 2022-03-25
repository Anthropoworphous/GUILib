package com.github.anthropoworphous.guilib.window.pane.guiitem.other;

import com.github.anthropoworphous.guilib.window.Window;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.PaneItemCollection;
import com.github.anthropoworphous.guilib.window.pane.guiitem.GUIItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.function.Supplier;

public class PageFlipingGUIItem extends GUIItem {
    public PageFlipingGUIItem(ItemStack item, Pane pageHolder, FlipMode flipMode) {
        super(item);
        this.pageHolder = pageHolder;
        this.flipMode = flipMode;
        pageNumberGetter = null;
    }
    public PageFlipingGUIItem(ItemStack item, Pane pageHolder, Supplier<Integer> pageNumberGetter) {
        super(item);
        this.pageHolder = pageHolder;
        this.pageNumberGetter = pageNumberGetter;
        flipMode = null;
    }

    private final Pane pageHolder;
    private final FlipMode flipMode;
    private final Supplier<Integer> pageNumberGetter;

    @Override
    public final void onClick(Window clickedWindow, WindowSlot slot, Pane pane, InventoryClickEvent event) {
        super.onClick(clickedWindow, slot, pane, event);
        PaneItemCollection holder = pageHolder.getValue(PaneItemCollection.class);
        if (flipMode == null) {
            holder.jumpTo(Optional.ofNullable(pageNumberGetter).orElse(() -> 0).get()); //should never be null but just to be safe u know?
        } else {
            switch (flipMode) {
                case NEXT -> holder.next();
                case PREVIOUS -> holder.previous();
                case NEXT_WITH_CYCLE -> { if (holder.currentPage() >= holder.getPageLimit()-1) {
                    holder.jumpTo(0);
                } else {
                    holder.next();
                }}
                case PREVIOUS_WITH_CYCLE -> { if (holder.currentPage() <= 0) {
                    holder.jumpTo(holder.getPageLimit()-1);
                } else {
                    holder.previous();
                }}
                case TO_THE_END -> holder.jumpTo(holder.getPageLimit()-1);
                case TO_THE_START -> holder.jumpTo(0);
                case RANDOM -> holder.jumpTo((int) (Math.random() * (holder.getPageLimit()-1)));
            }
        }
        clickedWindow.reload();
    }

    public enum FlipMode {
        NEXT,
        PREVIOUS,
        NEXT_WITH_CYCLE,
        PREVIOUS_WITH_CYCLE,
        TO_THE_START,
        TO_THE_END,
        RANDOM
    }
}
