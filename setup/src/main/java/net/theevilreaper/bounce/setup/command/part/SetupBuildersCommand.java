package net.theevilreaper.bounce.setup.command.part;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.event.EventDispatcher;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.AbstractStateNotifyEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static net.theevilreaper.bounce.setup.BounceSetup.SETUP_TAG;
import static net.theevilreaper.bounce.setup.event.AbstractStateNotifyEvent.*;
import static net.theevilreaper.bounce.setup.util.SetupMessages.SELECT_MAP_FIRST;

/**
 * The command allows the set the creators of a map to a {@link net.theevilreaper.bounce.common.map.GameMap} reference.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public final class SetupBuildersCommand extends Command {

    private final Function<UUID, Optional<BounceData>> setupDataFunction;

    public SetupBuildersCommand(@NotNull Function<UUID, Optional<BounceData>> setupDataFunction) {
        super("builders");
        this.setupDataFunction = setupDataFunction;
        this.setCondition(Conditions::playerOnly);

        ArgumentStringArray buildersArray = ArgumentType.StringArray("builders");
        this.addSyntax(this::handleBuilderSetup, buildersArray);
    }

    private void handleBuilderSetup(@NotNull CommandSender sender, @NotNull CommandContext context) {
        if (!sender.hasTag(SETUP_TAG)) {
            sender.sendMessage(SELECT_MAP_FIRST);
            return;
        }

        String[] builders = context.get("builders");

        if (builders.length == 0) {
            sender.sendMessage(Component.text("A map needs at least one builder", NamedTextColor.RED));
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

        setupData.getMapBuilder().addAuthors(builders);
        Component buildersAsComponent = Component.join(JoinConfiguration.arrayLike(), transformBuilders(builders));
        sender.sendMessage(Component.text("The creators of the map are: ").append(buildersAsComponent));
        GameMapBuilderState state = new GameMapBuilderState(setupData, GameMapBuilderState.StateChange.BUILDERS);
        EventDispatcher.call(new GameMapBuilderStateNotifyEvent(state));
    }

    private @NotNull List<TextComponent> transformBuilders(@NotNull String... builders) {
        return Arrays.stream(builders).map(Component::text).toList();
    }
}
