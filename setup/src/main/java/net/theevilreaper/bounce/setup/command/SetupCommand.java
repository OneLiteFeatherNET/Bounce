package net.theevilreaper.bounce.setup.command;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;
import net.onelitefeather.guira.SetupDataService;
import net.theevilreaper.bounce.setup.command.part.SetupBuildersCommand;
import net.theevilreaper.bounce.setup.command.part.SetupNameCommand;
import net.theevilreaper.bounce.setup.command.part.SetupPositionCommand;
import net.theevilreaper.bounce.setup.data.BounceData;
import org.jetbrains.annotations.NotNull;

public class SetupCommand extends Command {

    public SetupCommand(@NotNull SetupDataService<BounceData> dataService) {
        super("setup");
        this.setCondition(Conditions::playerOnly);
        this.addSubcommand(new SetupNameCommand(dataService::get));
        this.addSubcommand(new SetupBuildersCommand(dataService::get));
        this.addSubcommand(new SetupPositionCommand(dataService::get));
    }
}
