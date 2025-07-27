package net.theevilreaper.bounce.setup.command;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.onelitefeather.guira.SetupDataService;
import net.theevilreaper.bounce.setup.command.part.SetupPositionCommand;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link SetupCommand} is the root command for the setup process in the game.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public class SetupCommand extends Command {

    /**
     * Creates the command to allow players to initialize different parts of the game.
     *
     * @param dataService the service to get the setup data
     */
    public SetupCommand(@NotNull SetupDataService dataService) {
        super("setup");
        this.setCondition(Conditions::playerOnly);
        this.addSubcommand(new SetupPositionCommand(dataService::get));
    }
}
