package com.github.anthropoworphous.guilib.window.inventory;

import main.structure.tree.IConnectable;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public abstract class InventoryBuilder implements IConnectable {
    public InventoryBuilder() {
        inventory = build();
        if (Arrays.stream(SUPPORTED_INVENTORY_TYPES).noneMatch(t -> t == inventory.getType())) {
            throw new IllegalStateException("Unsupported inventory type");
        }
    }

    public static final InventoryType[] SUPPORTED_INVENTORY_TYPES = {
            InventoryType.CHEST,
            InventoryType.PLAYER,
            InventoryType.ENDER_CHEST,
            InventoryType.SHULKER_BOX,
            InventoryType.BARREL,
            InventoryType.HOPPER,
            InventoryType.DISPENSER,
            InventoryType.WORKBENCH,
            InventoryType.DROPPER,
            InventoryType.CRAFTING
    };

    private final Inventory inventory;

    public Inventory getInventory() {
        return inventory;
    }
    public int getWidth() {
        return switch (inventory.getType()) {
            case CHEST, PLAYER, ENDER_CHEST, SHULKER_BOX, BARREL -> 9;
            case HOPPER -> 5;
            case DISPENSER, WORKBENCH, DROPPER -> 3;
            case CRAFTING -> 2;
            default -> throw new IllegalStateException("Unsupported inventory type");
        };
    }
    public int getHeight() {
        return switch (inventory.getType()) {
            case CHEST -> inventory.getSize() / 9;
            case PLAYER, ENDER_CHEST, SHULKER_BOX, BARREL, DISPENSER, WORKBENCH, DROPPER -> 3;
            case CRAFTING -> 2;
            case HOPPER -> 1;
            default -> throw new IllegalStateException("Unsupported inventory type");
        };
    }

    public abstract Inventory build();
}
