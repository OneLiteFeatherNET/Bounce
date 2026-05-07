package net.theevilreaper.bounce.common.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Contract;

public abstract class Messages {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Component PREFIX;
    public static final Component SEPARATOR;

    static {
        SEPARATOR = Component.space().append(Component.text("»", NamedTextColor.GRAY)).append(Component.space());
        PREFIX = Component.text("Bounce", NamedTextColor.DARK_AQUA).append(SEPARATOR);
    }

    protected Messages() { }

    @Contract(value = "_ -> new", pure = true)
    public static Component withPrefix(String component) {
        return PREFIX.append(MINI_MESSAGE.deserialize(component));
    }

    @Contract(value = "_ -> new", pure = true)
    public static Component withPrefix(Component component) {
        return PREFIX.append(component);
    }

    @Contract(value = "_ -> new", pure = true)
    public static Component withMini(String text) {
        return MINI_MESSAGE.deserialize(text);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static Component withMini(String text, TagResolver... resolvers) {
        return MINI_MESSAGE.deserialize(text, resolvers);
    }

    @Contract(value = "_ -> new", pure = true)
    public static Component withMiniPrefix(String text) {
        return PREFIX.append(MINI_MESSAGE.deserialize(text));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static Component withMiniPrefix(String text, TagResolver... resolvers) {
        return PREFIX.append(MINI_MESSAGE.deserialize(text, resolvers));
    }
}
