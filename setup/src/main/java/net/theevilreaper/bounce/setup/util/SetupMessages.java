package net.theevilreaper.bounce.setup.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.theevilreaper.bounce.common.util.Messages;

public class SetupMessages extends Messages {

    public static final Component SELECT_MAP_FIRST;
    public static final Component INVALID_NAME;
    public static final Component NO_SPACE_SEPARATOR;

    public static final Component TELEPORT_CLICK;
    public static final Component DELETE_CLICK;

    static {
        NO_SPACE_SEPARATOR = Component.text("»", NamedTextColor.GRAY);
        SELECT_MAP_FIRST = withPrefix(Component.text("Please select a map first!", NamedTextColor.RED));
        INVALID_NAME = withPrefix(Component.text("Invalid name for the map", NamedTextColor.RED));

        TELEPORT_CLICK = NO_SPACE_SEPARATOR
                .append(Component.space())
                .append(Component.text("Left", NamedTextColor.GREEN))
                .append(Component.space())
                .append(Component.text("click", NamedTextColor.GRAY))
                .append(Component.space())
                .append(Component.text("->", NamedTextColor.GRAY))
                .append(Component.space())
                .append(Component.text("teleport", NamedTextColor.GREEN));

        DELETE_CLICK = NO_SPACE_SEPARATOR
                .append(Component.space())
                .append(Component.text("Right", NamedTextColor.RED))
                .append(Component.space())
                .append(Component.text("click", NamedTextColor.GRAY))
                .append(Component.space())
                .append(Component.text("->", NamedTextColor.GRAY))
                .append(Component.space())
                .append(Component.text("delete", NamedTextColor.RED));
    }


    private SetupMessages() {

    }
}
