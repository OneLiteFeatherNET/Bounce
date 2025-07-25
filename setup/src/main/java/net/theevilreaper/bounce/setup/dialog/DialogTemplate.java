package net.theevilreaper.bounce.setup.dialog;

import net.kyori.adventure.key.Key;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface DialogTemplate {

    void open(@NotNull Player player);

    @NotNull Key key();

}
