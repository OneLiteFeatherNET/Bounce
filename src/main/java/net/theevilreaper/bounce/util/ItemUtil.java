package net.theevilreaper.bounce.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link ItemUtil} is a small utility class which handles the creation and management of the used {@link ItemStack} in the game.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ItemUtil {

    private final ItemStack feather;

    public ItemUtil() {
        this.feather = ItemStack.builder(Material.FEATHER)
                .customName(Component.text("Recoil-pushing recoil pusher", NamedTextColor.LIGHT_PURPLE))
                .set(DataComponents.ENCHANTMENTS, new EnchantmentList(Enchantment.KNOCKBACK, 3))
                .build();
    }

    /**
     * Set's the {@link ItemStack} which is required for the game into the {@link net.minestom.server.inventory.Inventory} of a player
     *
     * @param paramPlayer the player who should get the {@link ItemStack}
     */
    public void setItem(@NotNull Player paramPlayer) {
        paramPlayer.getInventory().addItemStack(feather);
    }
}
