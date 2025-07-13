package net.theevilreaper.bounce.setup.inventory.overview;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

/**
 * The {@link OverviewType} enum represents different types of overview items
 * that can be displayed in the map overview inventory.
 *
 * @author Joltra
 * @version 1.0.0
 * @since 0.1.0
 */
public enum OverviewType {

    NAME("Map Name", Material.OAK_SIGN, NamedTextColor.YELLOW),
    BUILDER("Builder", Material.OAK_HANGING_SIGN, NamedTextColor.AQUA),
    SPAWN("Spawn Point", Material.COMPASS, NamedTextColor.RED),
    GAME_SPAWN("Game Spawn Point", Material.RECOVERY_COMPASS, NamedTextColor.RED)

    ;

    private final String name;
    private final Material material;
    private final TextColor color;

    private static final Map<OverviewType, ItemStack> itemCache = new EnumMap<>(OverviewType.class);
    private static final OverviewType[] VALUES = values();

    /**
     * Constructs a new OverviewType with the specified name, material, and color.
     *
     * @param name     the name of the overview type
     * @param material the material associated with this overview type
     * @param color    the text color for this overview type
     */
    OverviewType(@NotNull String name, @NotNull Material material, @NotNull TextColor color) {
        this.name = name;
        this.material = material;
        this.color = color;
    }

    /**
     * Gets the name of this overview type.
     *
     * @return the name of
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Gets the material associated with this overview type.
     *
     * @return the material
     */
    public @NotNull Material getMaterial() {
        return material;
    }

    /**
     * Gets the text color associated with this overview type.
     *
     * @return the text color
     */
    public @NotNull TextColor getColor() {
        return color;
    }

    /**
     * Gets the ItemStack representation of this overview type.
     *
     * @return the ItemStack for this overview type
     */
    public @NotNull ItemStack getItem() {
        return itemCache.computeIfAbsent(this, type -> ItemStack.builder(type.getMaterial())
                .customName(Component.text(type.getName(), type.getColor()))
                .build());
    }

    /**
     * Gets all available overview types.
     *
     * @return an array of all OverviewType values
     */
    public static @NotNull OverviewType[] getValues() {
        return VALUES;
    }
}
