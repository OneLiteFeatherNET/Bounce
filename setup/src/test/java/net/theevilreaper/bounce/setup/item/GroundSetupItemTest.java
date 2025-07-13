package net.theevilreaper.bounce.setup.item;

import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.listener.UseItemListener;
import net.minestom.server.network.packet.client.play.ClientUseItemPacket;
import net.minestom.testing.Env;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.listener.PlayerItemListener;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static net.theevilreaper.bounce.setup.util.SetupTags.SETUP_TAG;
import static org.junit.jupiter.api.Assertions.*;

public class GroundSetupItemTest extends SetupItemTestBase {

    @Test
    void testGroundItemLogic(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        setupDataService.add(player.getUuid(), new BounceData(player.getUuid(), testMapEntry, fileHandler));

        PlayerItemListener playerItemListener = new PlayerItemListener(nopFunction, setupDataService::get);

        env.process().eventHandler().addListener(PlayerUseItemEvent.class, playerItemListener);

        setupItems.setSaveItem(player);
        player.setTag(SETUP_TAG, 1);
        ItemStack stack = player.getInventory().getItemStack(0x04);
        assertNotEquals(ItemStack.AIR, stack);
        assertEquals(Material.CARTOGRAPHY_TABLE, stack.material());

        player.setHeldItemSlot((byte) 0x04);

        // Simulate using the item
        ClientUseItemPacket packet = new ClientUseItemPacket(PlayerHand.MAIN, 42, 0f, 0f);
        UseItemListener.useItemListener(packet, player);

        env.tick();

        Optional<BounceData> dataOptional = setupDataService.get(player.getUuid());
        assertTrue(dataOptional.isPresent(), "BounceData should be present for the player");

        assertNotNull(player.getOpenInventory());

        player.closeInventory();

        assertNull(player.getOpenInventory(), "The player should not have an open inventory after closing it");

        setupDataService.get(player.getUuid()).ifPresent(BounceData::reset);
        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty(), "Instance should be empty after destruction");
    }
}
