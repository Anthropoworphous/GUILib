package com.github.anthropoworphous;

import com.github.anthropoworphous.guilib.util.ID;
import com.github.anthropoworphous.guilib.window.WindowSlot;
import com.github.anthropoworphous.guilib.window.pane.Pane;
import com.github.anthropoworphous.guilib.window.pane.guiitem.base.GUIItem;
import com.github.anthropoworphous.guilib.window.pane.pattern.Pattern;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Tester {
    //@Test
    public void TestPaneDrawing() {
        Pane parentPane = new Pane(new ID(0), 9, 6, "parentPane");
        Pane testPane = new Pane(new ID(11), 5, 5, "testingPane").pattern(
                new Pattern().define(0, null).define(1, new GUIItem(new ItemStack(Material.STICK))).shaping()
                        .next(1).next(0).next(1).next(1).next(1).nextLine()
                        .next(1).next(0).next(1).next(0).next(0).nextLine()
                        .next(1).next(1).next(1).next(1).next(1).nextLine()
                        .next(0).next(0).next(1).next(0).next(1).nextLine()
                        .next(1).next(1).next(1).next(0).next(1).build()
        );

        parentPane.adopt(testPane);

        Map<ID, WindowSlot> result = parentPane.map();

        result.forEach((id, slot) -> System.out.println(id.getID() + " || " + slot.get()));
    }
}
