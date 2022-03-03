package com.github.anthropoworphous.guilib.window.pane.guiitem.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class Util {
    public static class Item {
        public static class Create {
            public static ItemStack create(Material material, int amount, Component name, List<Component> lore) {
                ItemStack item = new ItemStack(material, amount);
                ItemMeta meta = item.getItemMeta();

                meta.displayName(name);
                meta.lore(lore);

                item.setItemMeta(meta);

                return item;
            }
        }

        public static class Remove {
            public static void removeAmount(ItemStack item) {
                item.setAmount(1);
            }
            public static void removeDamage(ItemStack item) {
                ItemMeta data = item.getItemMeta();
                if (data instanceof Damageable) {
                    ((Damageable) data).setDamage(0);
                }
                item.setItemMeta(data);
            }
            public static void removeEnchant(ItemStack item) {
                item.getEnchantments().keySet().forEach(item::removeEnchantment);
            }
            public static void removeCustomModelData(ItemStack item) {
                ItemMeta data = item.getItemMeta();
                data.setCustomModelData(null);
                item.setItemMeta(data);
            }
            public static void removeCustomName(ItemStack item) {
                ItemMeta data = item.getItemMeta();
                data.displayName(null);
                item.setItemMeta(data);
            }
            public static void removeLore(ItemStack item) {
                ItemMeta data = item.getItemMeta();
                data.lore(null);
                item.setItemMeta(data);
            }
            public static void removeAttributes(ItemStack item) {
                ItemMeta data = item.getItemMeta();
                if (data.hasAttributeModifiers()) {
                    Objects.requireNonNull(data.getAttributeModifiers()).forEach((attribute, doNotCare) -> data.removeAttributeModifier(attribute));
                }
                item.setItemMeta(data);
            }
            public static void removeFlags(ItemStack item) {
                item.getItemFlags().forEach(item::removeItemFlags);
            }
        }
    }
    public static class Color {
        public static TextComponent removeColor(TextComponent text) {
            return text.color(TextColor.color(255,255,255));
        }
        public static TextComponent colorWithCode(TextComponent text, String code) {
            ChatColor color = ChatColor.getByChar(code);
            if (color == null) { throw new IllegalArgumentException("Invalid color code"); }
            return text.color(TextColor.color(color.asBungee().getColor().getRGB()));
        }
    }
}