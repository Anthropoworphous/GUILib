package com.github.anthropoworphous.guilib.io;

import com.github.anthropoworphous.guilib.util.ID;
import com.github.anthropoworphous.guilib.window.pane.PaneItemCollection;
import com.github.anthropoworphous.guilib.window.pane.guiitem.base.GUIItem;
import com.google.gson.Gson;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PaneItemCollectionJsonConverter {
    public PaneItemCollectionJsonConverter(PaneItemCollection paneItemCollection) {
        pageNumber = paneItemCollection.currentPage();
        for (int i = paneItemCollection.size()-1; i > 0; i--) {
            paneItems.computeIfAbsent(i, k -> new HashMap<>());
            final int index = i;
            paneItemCollection.get(index).forEach((id, item) ->
                    paneItems.get(index).put(id.getID(), new Gson().toJson(item)));
        }
    }

    private final int pageNumber;
    private final Map<Integer, Map<Integer, String>> paneItems = new HashMap<>();

    public PaneItemCollection fromJson() {
        Optional<Integer> max = paneItems.keySet().stream().max(Comparator.naturalOrder());

        if (max.isEmpty()) { return null; }

        PaneItemCollection result = new PaneItemCollection(max.get());

        paneItems.forEach((page, map) ->
                map.forEach((id, item) ->
                        result.addItem(new Gson().fromJson(item, GUIItem.class), new ID(id), page)));

        result.jumpTo(pageNumber);

        return result;
    }
}
