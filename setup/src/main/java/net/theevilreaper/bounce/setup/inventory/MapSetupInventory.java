package net.theevilreaper.bounce.setup.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.aves.inventory.GlobalInventoryBuilder;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.bounce.setup.event.map.MapSetupSelectEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

import static net.theevilreaper.bounce.setup.util.SetupItems.DECORATION;

/**
 * The {@link MapSetupInventory} is only used during the setup of the maps for the game.
 * It allows the user to select a map and trigger the process to set up the map.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("java:S3252")
public class MapSetupInventory extends GlobalInventoryBuilder {

    private static final List<Component> LORE_COMPONENTS = List.of(
            Component.empty(),
            MiniMessage.miniMessage().deserialize("<yellow>Click to setup this <green>map</green>"),
            Component.empty()
    );

    private static final int[] MAP_SLOTS = LayoutCalculator
            .repeat(InventoryType.CHEST_1_ROW.getSize(), InventoryType.CHEST_3_ROW.getSize());

    public MapSetupInventory(@NotNull Supplier<List<MapEntry>> maps) {
        super(Component.text("Select map"), InventoryType.CHEST_4_ROW);

        InventoryLayout layout = InventoryLayout.fromType(getType());
        layout.setItems(LayoutCalculator.fillRow(InventoryType.CHEST_1_ROW), DECORATION);
        layout.setItems(LayoutCalculator.fillRow(InventoryType.CHEST_4_ROW), DECORATION);

        this.setLayout(layout);

        if (maps.get().isEmpty()) return;

        this.setDataLayoutFunction(dataLayout -> {
            dataLayout = dataLayout == null ? InventoryLayout.fromType(getType()) : dataLayout;

            dataLayout.blank(MAP_SLOTS);
            List<MapEntry> mapEntries = maps.get();
            for (int i = 0; i < mapEntries.size(); i++) {
                var currentMap = mapEntries.get(i);
                dataLayout.setItem(MAP_SLOTS[i], getMapItem(currentMap.getDirectoryRoot()), (player, slot, clickType, result) ->
                        this.handleClick(currentMap, player, slot, clickType, result));
            }
            return dataLayout;
        });
        this.invalidateDataLayout();
        this.register();
    }

    /**
     * Handles the click event for the map selection.
     *
     * @param currentMap the current map being clicked
     * @param player     the player who clicked
     * @param ignored    the slot clicked
     * @param clickType  the type of click
     * @param result     the result of the inventory condition
     */
    private void handleClick(@NotNull MapEntry currentMap, @NotNull Player player, int ignored, @NotNull ClickType clickType, @NotNull InventoryConditionResult result) {
        result.setCancel(true);
        if (clickType != ClickType.LEFT_CLICK && clickType != ClickType.RIGHT_CLICK) return;
        EventDispatcher.callCancellable(new MapSetupSelectEvent(player, currentMap), player::closeInventory);
    }

    /**
     * Creates a new {@link ItemStack} representing the map item.
     *
     * @param path the path of the map
     * @return the ItemStack representing the map item
     */
    @Contract(value = "_ -> new", pure = true)
    private @NotNull ItemStack getMapItem(@NotNull Path path) {
        return ItemStack.builder(Material.PAPER)
                .lore(LORE_COMPONENTS)
                .customName(Component.text(path.getFileName().toString()))
                .build();
    }
}
