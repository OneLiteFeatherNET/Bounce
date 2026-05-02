package net.theevilreaper.bounce.setup.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

/**
 * The class holds all item references that are required in the setup process of a map.
 * Each item uses a {@link Tag<Byte>} to identify which functionality the item has.
 * This behaviour is easier to use because it doesn't require additional references in the event class to check the item.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("java:S3252")
public final class SetupItems {

    public static final int OVERVIEW_FLAG = 0x02;
    public static final Tag<Integer> ITEM_TAG = Tag.Integer("item_tag");
    public static final ItemStack DECORATION = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
            .customName(Component.empty())
            .build();

    private static final ItemStack OVERVIEW;
    private static final ItemStack SAVE;
    private static final ItemStack VIEW_ITEM;
    private static final ItemStack GROUND_ITEM;

    static  {
        OVERVIEW = ItemStack.builder(Material.CHEST)
                .customName(Component.text("Maps", NamedTextColor.GREEN))
                .set(ITEM_TAG, 0x00)
                .build();
        SAVE = ItemStack.builder(Material.BELL)
                .customName(Component.text("Save map", NamedTextColor.RED))
                .set(ITEM_TAG, 0x01)
                .build();
        VIEW_ITEM = ItemStack.builder(Material.COMPASS)
                .customName(Component.text("View data", NamedTextColor.AQUA))
                .set(ITEM_TAG, OVERVIEW_FLAG)
                .build();
        GROUND_ITEM = ItemStack.builder(Material.CARTOGRAPHY_TABLE)
                .customName(Component.text("Ground Layer", NamedTextColor.GREEN))
                .set(ITEM_TAG, 0x04)
                .build();
    }

    /**
     * Sets the overview item for the given player.
     *
     * @param player the player to set the item
     */
    public static void setOverViewItem(Player player) {
        player.getInventory().clear();
        player.getInventory().setItemStack(0x00, OVERVIEW);
        player.setHeldItemSlot((byte) 0);
    }

    /**
     * Sets the save item for the given player.
     *
     * @param player the player to set the item
     */
    public static void setSetupItems(Player player) {
        player.getInventory().clear();
        player.getInventory().setItemStack(0x06, SAVE);
        player.getInventory().setItemStack(0x04, GROUND_ITEM);
        player.getInventory().setItemStack(0x02, VIEW_ITEM);
        player.setHeldItemSlot((byte) 0);
    }

    private SetupItems() {
        // Nothing do to here
    }
}
