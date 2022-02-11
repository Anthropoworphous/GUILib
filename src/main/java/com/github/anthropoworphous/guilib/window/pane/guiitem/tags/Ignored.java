package com.github.anthropoworphous.guilib.window.pane.guiitem.tags;


import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public enum Ignored {
    IGNORE_AMOUNT {
        @Override
        public void modify(ItemStack item) {
            item.setAmount(1);
        }
    },
    IGNORE_DURABILITY {
        @Override
        public void modify(ItemStack item) {
            ItemMeta data = item.getItemMeta();
            if (data instanceof Damageable) {
                ((Damageable) data).setDamage(0);
            }
            item.setItemMeta(data);
        }
    },
    IGNORE_ENCHANTMENT {
        @Override
        public void modify(ItemStack item) {
            item.getEnchantments().forEach((enchant, doNotCare) -> item.removeEnchantment(enchant));
        }
    },
    IGNORE_CUSTOM_MODEL_DATA {
        @Override
        public void modify(ItemStack item) {
            ItemMeta data = item.getItemMeta();
            data.setCustomModelData(null);
            item.setItemMeta(data);
        }
    },
    IGNORE_CUSTOM_NAME {
        @Override
        public void modify(ItemStack item) {
            ItemMeta data = item.getItemMeta();
            data.displayName(null);
            item.setItemMeta(data);
        }
    },
    IGNORE_LORE {
        @Override
        public void modify(ItemStack item) {
            ItemMeta data = item.getItemMeta();
            data.lore(null);
            item.setItemMeta(data);
        }
    },
    IGNORE_ATTRIBUTES {
        @Override
        public void modify(ItemStack item) {
            ItemMeta data = item.getItemMeta();
            if (data.hasAttributeModifiers()) {
                Objects.requireNonNull(data.getAttributeModifiers()).forEach((attribute, doNotCare) -> data.removeAttributeModifier(attribute));
            }
            item.setItemMeta(data);
        }
    },
    IGNORE_FLAGS {
        @Override
        public void modify(ItemStack item) {
            item.getItemFlags().forEach(item::removeItemFlags);
        }
    };

    public abstract void modify(ItemStack item);
}
