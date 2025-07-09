package net.theevilreaper.bounce.setup.inventory;

import net.minestom.server.item.Material;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.NonExtendable
@ApiStatus.Internal
public final class SetupBlocks {

    public static final List<Material> ALLOWED_GROUND_BLOCKS = List.of(
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

    public  static final List<Material> ALLOWED_PUSH_BLOCKS = List.of(
            Material.COAL_BLOCK,
            Material.GOLD_BLOCK,
            Material.IRON_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.EMERALD_BLOCK,
            Material.LAPIS_BLOCK,
            Material.COPPER_BLOCK,
            Material.NETHERITE_BLOCK
    );

    private SetupBlocks() {
        // Prevent instantiation
    }
}
