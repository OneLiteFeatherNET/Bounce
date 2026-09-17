package net.theevilreaper.bounce.common.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Contract;

/**
 * Utility class for creating messages with the Bounce prefix.
 * <p>
 * Messages can either be created from an existing {@link Component} or from
 * MiniMessage formatted text.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public abstract class Messages {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static final Component SEPARATOR;

    private static final Component PREFIX;

    static {
        SEPARATOR = Component.space()
                .append(Component.text("»", NamedTextColor.GRAY))
                .append(Component.space());
        PREFIX = Component.text("Bounce", NamedTextColor.DARK_AQUA)
                .append(SEPARATOR);
    }

    protected Messages() {
    }

    /**
     * Adds the Bounce prefix to the given component.
     *
     * @param component to append
     * @return a new component containing the prefix and the given component
     */
    @Contract(value = "_ -> new", pure = true)
    public static Component withPrefix(Component component) {
        return PREFIX.append(component);
    }

    /**
     * Deserializes the given MiniMessage text and adds the Bounce prefix.
     *
     * @param text to append
     * @return a new component containing the prefix and the deserialized text
     */
    @Contract(value = "_ -> new", pure = true)
    public static Component withMiniPrefix(String text) {
        return PREFIX.append(MINI_MESSAGE.deserialize(text));
    }

    /**
     * Deserializes the given MiniMessage text with the provided tag resolvers
     * and adds the Bounce prefix.
     *
     * @param text      to append
     * @param resolvers the tag resolvers used during deserialization
     * @return a new component containing the prefix and the deserialized text
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static Component withMiniPrefix(String text, TagResolver... resolvers) {
        return PREFIX.append(MINI_MESSAGE.deserialize(text, resolvers));
    }
}