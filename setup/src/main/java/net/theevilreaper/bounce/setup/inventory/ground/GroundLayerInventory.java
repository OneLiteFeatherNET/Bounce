package net.theevilreaper.bounce.setup.inventory.ground;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.aves.inventory.GlobalInventoryBuilder;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.setup.inventory.slot.BackSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

import static net.theevilreaper.bounce.setup.util.SetupItems.DECORATION;

public class GroundLayerInventory extends GlobalInventoryBuilder {

    private final List<Material> allowedGroundBlocks = List.of(
            Material.GLASS,
            Material.RED_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS,
            Material.LIME_STAINED_GLASS,
            Material.GREEN_STAINED_GLASS,
            Material.CYAN_STAINED_GLASS,
            Material.LIGHT_BLUE_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS,
            Material.PINK_STAINED_GLASS,
            Material.WHITE_STAINED_GLASS,
            Material.BLACK_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS,
            Material.BROWN_STAINED_GLASS,
            Material.TERRACOTTA,
            Material.WHITE_TERRACOTTA,
            Material.ORANGE_TERRACOTTA,
            Material.MAGENTA_TERRACOTTA,
            Material.LIGHT_BLUE_TERRACOTTA,
            Material.YELLOW_TERRACOTTA,
            Material.LIME_TERRACOTTA,
            Material.GREEN_TERRACOTTA,
            Material.CYAN_TERRACOTTA,
            Material.LIGHT_GRAY_TERRACOTTA,
            Material.GRAY_TERRACOTTA,
            Material.PINK_TERRACOTTA,
            Material.PURPLE_TERRACOTTA,
            Material.BLUE_TERRACOTTA,
            Material.BROWN_TERRACOTTA,
            Material.BLACK_TERRACOTTA,
            Material.ICE,
            Material.PACKED_ICE
    );

    public GroundLayerInventory(@NotNull PlayerConsumer backFunction) {
        super(Component.text("Select ground block"), InventoryType.CHEST_6_ROW);

        InventoryLayout layout = InventoryLayout.fromType(getType());

        layout.setItems(LayoutCalculator.fillRow(InventoryType.CHEST_1_ROW), DECORATION);
        layout.setItems(LayoutCalculator.fillRow(getType()), DECORATION);

        int[] slots = LayoutCalculator.quad(InventoryType.CHEST_1_ROW.getSize(), InventoryType.CHEST_5_ROW.getSize() - 1);

        Iterator<Material> iterator = allowedGroundBlocks.iterator();

        for (int i = 0; i < slots.length && iterator.hasNext(); i++) {
            Material currentMaterial = iterator.next();
            ItemStack stack = ItemStack.builder(currentMaterial)
                    .customName(Component.translatable(currentMaterial.registry().translationKey(), NamedTextColor.AQUA))
                    .build();
            layout.setItem(slots[i], stack, this::handleClick);
        }
        layout.setItem(getType().getSize(), new BackSlot(), (player1, i, clickType, result) -> backFunction.accept(player1));
        this.setLayout(layout);
    }

    private void handleClick(@NotNull Player player, int slot, @NotNull ClickType clickType, @NotNull InventoryConditionResult result) {
        result.setCancel(true);

       /* ItemStack clickedItem = player.getInventory().getItemStack(slot);
        if (clickedItem == null || !allowedGroundBlocks.contains(clickedItem.material())) {
            player.sendMessage(Component.text("Invalid ground block selected.", NamedTextColor.RED));
            return;
        }

        // Handle the selection of the ground block
        // This could involve updating the game map or sending a confirmation message to the player
        player.sendMessage(Component.text("Selected ground block: " + clickedItem.customName(), NamedTextColor.GREEN));
*/
    }
}
