package net.theevilreaper.bounce.setup.command.part;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.event.EventDispatcher;
import net.onelitefeather.guira.data.SetupData;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.common.util.Messages;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.AbstractStateNotifyEvent.GameMapBuilderStateNotifyEvent;
import net.theevilreaper.bounce.setup.util.SetupMessages;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static net.theevilreaper.bounce.setup.event.AbstractStateNotifyEvent.*;
import static net.theevilreaper.bounce.setup.util.SetupMessages.SELECT_MAP_FIRST;
import static net.theevilreaper.bounce.setup.util.SetupTags.SETUP_TAG;

public final class SetupNameCommand extends Command {

    private final OptionalSetupDataGetter setupDataFunction;

    public SetupNameCommand(@NotNull OptionalSetupDataGetter setupDataFunction) {
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

        Optional<SetupData> fetchedData = this.setupDataFunction.get(sender.identity().uuid());
        if (fetchedData.isEmpty()) {
            sender.sendMessage(SELECT_MAP_FIRST);
            return;
        }

        BounceData setupData = ((BounceData) fetchedData.get());

        if (setupData.getMapBuilder() == null) {
            sender.sendMessage("No map is currently selected. Please select a map first.");
            return;
        }


        setupData.getMapBuilder().name(name);
        Component message = Messages.withPrefix(Component.text("The name of the map now is: ", NamedTextColor.GRAY))
                .append(Component.text(name, NamedTextColor.AQUA));
        sender.sendMessage(message);
        GameMapBuilderState state = new GameMapBuilderState(setupData, GameMapBuilderState.StateChange.NAME);
        EventDispatcher.call(new GameMapBuilderStateNotifyEvent(state));
    }
}
