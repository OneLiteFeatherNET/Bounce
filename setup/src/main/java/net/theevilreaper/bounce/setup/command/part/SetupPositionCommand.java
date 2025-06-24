package net.theevilreaper.bounce.setup.command.part;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.theevilreaper.aves.util.Components;
import net.theevilreaper.bounce.common.util.Messages;
import net.theevilreaper.bounce.setup.data.BounceData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static net.theevilreaper.bounce.setup.BounceSetup.SETUP_TAG;
import static net.theevilreaper.bounce.setup.util.SetupMessages.SELECT_MAP_FIRST;

public final class SetupPositionCommand extends Command {

    private final Function<UUID, Optional<BounceData>> setupDataFunction;

    public SetupPositionCommand(@NotNull Function<UUID, Optional<BounceData>> setupDataFunction) {
        super("position");
        this.setCondition(Conditions::playerOnly);
        this.setupDataFunction = setupDataFunction;
        ArgumentWord spawnType = ArgumentType.Word("spawnType").from("spawn", "game");
        this.addSyntax(this::handleSpawnSet, spawnType);
    }

    private void handleSpawnSet(@NotNull CommandSender sender, @NotNull CommandContext context) {
        if (!sender.hasTag(SETUP_TAG)) {
            sender.sendMessage(SELECT_MAP_FIRST);
            return;
        }

        String type = context.get("spawnType");

        Optional<BounceData> fetchedData = setupDataFunction.apply(sender.identity().uuid());
        if (fetchedData.isEmpty()) {
            sender.sendMessage(SELECT_MAP_FIRST);
            return;
        }

        Player player = (Player) sender;
        BounceData setupData = fetchedData.get();

        if (setupData.getMapBuilder() == null) {
            sender.sendMessage("No map is currently selected. Please select a map first.");
            return;
        }

        switch (type) {
            case "spawn":
                setupData.getMapBuilder().setSpawn(player.getPosition());
                break;
            case "game":
                setupData.getMapBuilder().setGameSpawn(player.getPosition());
                break;
            default:
                sender.sendMessage(Component.text("Invalid spawn type! Use 'spawn' or 'game'.", NamedTextColor.RED));
                return;
        }
        Component posAsComponent = Components.convertPoint(Pos.fromPoint(player.getPosition()));
        Component argComponent = Component.text(type, NamedTextColor.GREEN);
        Component message = Messages.withPrefix(Component.text("The ", NamedTextColor.GRAY)
                .append(argComponent)
                .append(Component.space())
                .append(Component.text("position of the map is now located at: ", NamedTextColor.GRAY))
                .append(posAsComponent)
        );
        sender.sendMessage(message);
        setupData.triggerUpdate();
    }
}
