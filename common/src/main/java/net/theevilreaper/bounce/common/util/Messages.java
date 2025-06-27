package net.theevilreaper.bounce.common.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class Messages {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Component PREFIX;
    private static final Component SEPARATOR;

    static {
        PREFIX = MINI_MESSAGE.deserialize("<gradient:#00ff33:#fffafe:0.2>Suicide<gradient:#fffafe:#ff0008>TNT</gradient></gradient>").append(Component.space());
        SEPARATOR = Component.space().append(Component.text(">>")).append(Component.space());
    }

    protected Messages() { }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Component withPrefix(@NotNull String component) {
        return PREFIX.append(MINI_MESSAGE.deserialize(component));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Component withPrefix(@NotNull Component component) {
        return PREFIX.append(Component.space()).append(component);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Component withMini(@NotNull String text) {
        return MINI_MESSAGE.deserialize(text);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Component withMini(@NotNull String text, @NotNull TagResolver... resolvers) {
        return MINI_MESSAGE.deserialize(text, resolvers);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Component withMiniPrefix(@NotNull String text) {
        return PREFIX.append(Component.space()).append(MINI_MESSAGE.deserialize(text));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Component withMiniPrefix(@NotNull String text, @NotNull TagResolver... resolvers) {
        return PREFIX.append(Component.space()).append(MINI_MESSAGE.deserialize(text, resolvers));
    }
}
