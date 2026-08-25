package net.theevilreaper.bounce.setup.inventory;

import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public interface DataType {

    /**
     * Gets the name of this overview type.
     *
     * @return the name of
     */
    String getName();

    /**
     * Gets the material associated with this overview type.
     *
     * @return the material
     */
    Material getMaterial();

    /**
     * Gets the text color associated with this overview type.
     *
     * @return the text color
     */
    TextColor getColor();

    /**
     * Gets the ItemStack representation of this data type.
     *
     * @return the ItemStack for this data type
     */
    ItemStack getItem();
}
