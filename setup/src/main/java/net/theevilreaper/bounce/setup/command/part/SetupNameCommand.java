package net.theevilreaper.bounce.setup.command.part;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.theevilreaper.bounce.common.util.Messages;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.util.SetupMessages;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static net.theevilreaper.bounce.setup.BounceSetup.SETUP_TAG;
import static net.theevilreaper.bounce.setup.util.SetupMessages.SELECT_MAP_FIRST;

public final class SetupNameCommand extends Command {

    private final Function<UUID, Optional<BounceData>> setupDataFunction;

    public SetupNameCommand(@NotNull Function<UUID, Optional<BounceData>> setupDataFunction) {
        super("name");
        this.setupDataFunction = setupDataFunction;
        this.setCondition(Conditions::playerOnly);
        ArgumentString mapName = ArgumentType.String("mapName");
        this.addSyntax(this::handleNameSet, mapName);
    }

    private void handleNameSet(@NotNull CommandSender sender, @NotNull CommandContext context) {
        if (!sender.hasTag(SETUP_TAG)) {
            sender.sendMessage(SELECT_MAP_FIRST);
            return;
        }

        String name = context.get("mapName");

        if (name == null || name.trim().isEmpty()) {
            sender.sendMessage(SetupMessages.INVALID_NAME);
            return;
        }

        Optional<BounceData> fetchedData = this.setupDataFunction.apply(sender.identity().uuid());
        if (fetchedData.isEmpty()) {
            sender.sendMessage(SELECT_MAP_FIRST);
            return;
        }

        BounceData setupData = fetchedData.get();

        if (setupData.getMapBuilder() == null) {
            sender.sendMessage("No map is currently selected. Please select a map first.");
            return;
        }

        setupData.getMapBuilder().setName(name);
        Component message = Messages.withPrefix(Component.text("The name of the map now is: ", NamedTextColor.GRAY))
                .append(Component.text(name, NamedTextColor.AQUA));
        sender.sendMessage(message);
        setupData.triggerUpdate();
    }
}
