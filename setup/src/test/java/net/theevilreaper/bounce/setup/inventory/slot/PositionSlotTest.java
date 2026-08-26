package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.inventory.function.InventoryClick;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static net.theevilreaper.bounce.setup.util.SetupMessages.SET_CLICK;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class PositionSlotTest {

    private static final int SLOT_INDEX = 12;

    @Test
    void testUnsetPositionWithSetterShowsHint(@NotNull Env env) {
        PositionSlot<OverviewType> slot = new PositionSlot<>(OverviewType.SPAWN, null, player -> {});
        ItemStack item = slot.getItem();

        assertTrue(item.has(DataComponents.LORE));
        List<Component> lore = item.get(DataComponents.LORE);

        assertNotNull(lore);
        assertEquals(3, lore.size(), "Lore should contain 3 components");
        assertAll(
                "Unset position lore",
                () -> assertEquals(Component.empty(), lore.getFirst(), "First lore component should be empty"),
                () -> assertEquals(SET_CLICK, lore.get(1), "Middle lore component should hint at setting the position"),
                () -> assertEquals(Component.empty(), lore.getLast(), "Last lore component should be empty")
        );
    }

    @Test
    void testUnsetPositionWithoutSetterHasNoLore(@NotNull Env env) {
        PositionSlot<OverviewType> slot = new PositionSlot<>(OverviewType.SPAWN, null, null);
        ItemStack item = slot.getItem();

        assertEquals(OverviewType.SPAWN.getItem(), item, "Without a setter the raw overview item should be rendered");
    }

    @Test
    void testLeftClickOnUnsetPositionInvokesSetter(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        AtomicReference<Player> captured = new AtomicReference<>();

        PositionSlot<OverviewType> slot = new PositionSlot<>(OverviewType.SPAWN, null, captured::set);
        InventoryClick click = slot.getClick();
        click.onClick(player, SLOT_INDEX, new Click.Left(SLOT_INDEX), slot.getItem(), holder -> {});

        assertEquals(player, captured.get(), "Left-clicking an unset position should capture the clicking player");

        env.destroyInstance(instance, true);
    }

    @Test
    void testRightClickOnUnsetPositionDoesNotInvokeSetter(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        AtomicReference<Player> captured = new AtomicReference<>();

        PositionSlot<OverviewType> slot = new PositionSlot<>(OverviewType.SPAWN, null, captured::set);
        InventoryClick click = slot.getClick();
        click.onClick(player, SLOT_INDEX, new Click.Right(SLOT_INDEX), slot.getItem(), holder -> {});

        assertNull(captured.get(), "Right-clicking an unset position must not invoke the setter");

        env.destroyInstance(instance, true);
    }

    @Test
    void testLeftClickOnAlreadySetPositionDoesNotInvokeSetter(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        AtomicReference<Player> captured = new AtomicReference<>();
        Pos position = new Pos(1, 2, 3);

        PositionSlot<OverviewType> slot = new PositionSlot<>(OverviewType.SPAWN, position, captured::set);
        InventoryClick click = slot.getClick();
        click.onClick(player, SLOT_INDEX, new Click.Left(SLOT_INDEX), slot.getItem(), holder -> {});

        assertNull(captured.get(), "Clicking an already-set position must teleport instead of invoking the setter");

        env.destroyInstance(instance, true);
    }
}
