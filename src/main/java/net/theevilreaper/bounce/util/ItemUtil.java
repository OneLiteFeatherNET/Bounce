package net.theevilreaper.bounce.util;

import de.icevizion.api.item.ItemFactory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {
    
    private final ItemStack feather;

    public ItemUtil() {
        feather = new ItemFactory(Material.FEATHER).setDisplayName("§5Rückstoßstoßender Rückstoßstoßer").addUnsafeEnchantment(Enchantment.KNOCKBACK, 2).build();
    }

    public void setItem(Player paramPlayer) {
        paramPlayer.getInventory().addItem(feather);
    }
}
