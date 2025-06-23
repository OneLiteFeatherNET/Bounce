package net.theevilreaper.bounce.setup.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.theevilreaper.bounce.common.util.Messages;

public class SetupMessages extends Messages {

    public static final Component SELECT_MAP_FIRST;
    public static final Component INVALID_NAME;

    static {
        SELECT_MAP_FIRST = withPrefix(Component.text("Please select a map first!", NamedTextColor.RED));
        INVALID_NAME = withPrefix(Component.text("Invalid name for the map", NamedTextColor.RED));
    }


    private SetupMessages() {

    }
}
