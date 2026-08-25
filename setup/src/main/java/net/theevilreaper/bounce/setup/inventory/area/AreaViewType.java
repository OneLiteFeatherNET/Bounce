package net.theevilreaper.bounce.setup.inventory.area;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.bounce.setup.inventory.DataType;

import java.util.EnumMap;
import java.util.Map;

/**
 * The {@link AreaViewType} enum represents different types of items that can be displayed in the
 * {@link AreaViewInventory}.
 */
public enum AreaViewType implements DataType {

    LEFT_AREA_CORNER("Left Corner", Material.GREEN_WOOL, NamedTextColor.AQUA),
    RIGHT_AREA_CORNER("Right Corner", Material.RED_WOOL, NamedTextColor.AQUA),
    SHUFFLE_INTERVAL("Reshuffle Interval", Material.CLOCK, NamedTextColor.LIGHT_PURPLE),
    RESHUFFLE_PERCENTAGE("Reshuffle Percentage", Material.TARGET, NamedTextColor.LIGHT_PURPLE)

    ;

    private final String name;
    private final Material material;
    private final TextColor color;

    private static final Map<AreaViewType, ItemStack> itemCache = new EnumMap<>(AreaViewType.class);
    private static final AreaViewType[] VALUES = values();

    /**
     * Constructs a new AreaViewType with the specified name, material, and color.
     *
     * @param name     the name of the area view type
     * @param material the material associated with this area view type
     * @param color    the text color for this area view type
     */
    AreaViewType(String name, Material material, TextColor color) {
        this.name = name;
        this.material = material;
        this.color = color;
    }

    /**
     * Gets the name of this area view type.
     *
     * @return the name of
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the material associated with this area view type.
     *
     * @return the material
     */
    @Override
    public Material getMaterial() {
        return material;
    }

    /**
     * Gets the text color associated with this area view type.
     *
     * @return the text color
     */
    @Override
    public TextColor getColor() {
        return color;
    }

    /**
     * Gets the ItemStack representation of this area view type.
     *
     * @return the ItemStack for this area view type
     */
    @Override
    public ItemStack getItem() {
        return itemCache.computeIfAbsent(this, type -> ItemStack.builder(type.getMaterial())
                .customName(Component.text(type.getName(), type.getColor()))
                .build());
    }
}
