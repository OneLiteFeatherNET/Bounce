package net.theevilreaper.bounce.setup.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.theevilreaper.tamias.common.util.Tags;
import org.jetbrains.annotations.NotNull;

/**
 * The class holds all item reference which are required in the setup process of a map.
 * Each item uses a {@link Tag<Byte>} to identify which functionality the item has.
 * This behaviour is easier to use because it doesn't require additional references in the event class to check the item.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("java:S3252")
public final class SetupItems {

    public static int OVERVIEW_FLAG = 0x02;

    public static final ItemStack DECORATION = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
            .customName(Component.empty())
            .build();
    
    public static Tag<Integer> ITEM_TAG = Tag.Integer("item_tag");

    private final ItemStack overview;
    private final ItemStack save;
    private final ItemStack viewItem;

    public SetupItems() {
        this.overview = ItemStack.builder(Material.CHEST)
                .customName(Component.text("Maps", NamedTextColor.GREEN))
                .set(ITEM_TAG, 0x00)
                .build();
        this.save = ItemStack.builder(Material.BELL)
                .customName(Component.text("Save map", NamedTextColor.RED))
                .set(ITEM_TAG, 0x01)
                .build();
        this.viewItem = ItemStack.builder(Material.COMPASS)
                .customName(Component.text("View data", NamedTextColor.AQUA))
                .set(ITEM_TAG, OVERVIEW_FLAG)
                .build();
    }

    /**
     * Sets the overview item for the given player.
     *
     * @param player the player to set the item
     */
    public void setOverViewItem(@NotNull Player player) {
        player.getInventory().clear();
        player.getInventory().setItemStack(0x00, this.overview);
        player.setHeldItemSlot((byte) 0);
    }

    /**
     * Sets the save item for the given player.
     *
     * @param player the player to set the item
     */
    public void setSaveItem(@NotNull Player player) {
        player.getInventory().clear();
        player.getInventory().setItemStack(0x06, this.save);
        player.getInventory().setItemStack(0x02, this.viewItem);
        player.setHeldItemSlot((byte) 0);
    }
}
