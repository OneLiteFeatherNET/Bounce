package net.theevilreaper.bounce.setup.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.bounce.common.push.PushEntry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@ApiStatus.Internal
public final class LoreHelper {

    private static final Component DISPLAY_NAME = Component.text("Boost Value", NamedTextColor.GREEN);
    private static final Component CURRENT_VALUE = Component.text("Current:", NamedTextColor.GRAY).append(Component.space());
    private static final Component LEFT_CLICK = miniMessage().deserialize("<yellow>Left-click</yellow><gray>: <green>Increase</green> the value");

    public static @NotNull ItemStack getPushValue(@NotNull PushEntry pushEntry) {
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(CURRENT_VALUE.append(Component.text(pushEntry.getValue(), NamedTextColor.YELLOW)));
        lore.add(Component.empty());
        lore.add(LEFT_CLICK);
        lore.add(Component.empty());
        return ItemStack.builder(Material.FEATHER)
                .customName(DISPLAY_NAME)
                .lore(lore)
                .build();
    }


    private LoreHelper() {
        // Prevent instantiation
    }
}
