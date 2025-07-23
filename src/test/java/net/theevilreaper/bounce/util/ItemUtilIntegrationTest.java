package net.theevilreaper.bounce.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class ItemUtilIntegrationTest {

    @Test
    void testItemStackSet(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);

        assertEquals(ItemStack.AIR, player.getInventory().getItemStack(0));

        ItemUtil itemUtil = new ItemUtil();

        // Test setting an item stack
        itemUtil.setItem(player);

        ItemStack itemStack = player.getInventory().getItemStack(0);

        // Verify that the item stack is not null and has the expected properties
        assertNotEquals(ItemStack.AIR, itemStack);
        assertEquals(Material.FEATHER, itemStack.material());

        assertTrue(itemStack.has(DataComponents.CUSTOM_NAME));

        assertTrue(itemStack.has(DataComponents.ENCHANTMENTS));

        Component customName = itemStack.get(DataComponents.CUSTOM_NAME);
        assertNotNull(customName);
        String displayName = PlainTextComponentSerializer.plainText().serialize(customName);

        assertNotNull(displayName);
        assertTrue(displayName.contains("Recoil-pushing recoil pusher"));

        EnchantmentList enchantmentList = itemStack.get(DataComponents.ENCHANTMENTS);

        assertNotNull(enchantmentList);
        assertEquals(1, enchantmentList.enchantments().size());
        assertTrue(enchantmentList.has(Enchantment.KNOCKBACK));

        env.destroyInstance(instance, true);
    }

}