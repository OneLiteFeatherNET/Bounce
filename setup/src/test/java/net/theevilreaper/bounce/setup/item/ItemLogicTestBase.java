package net.theevilreaper.bounce.setup.item;

import net.minestom.testing.extension.MicrotusExtension;
import net.onelitefeather.guira.SetupDataService;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.file.GsonFileHandler;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.common.util.GsonUtil;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.inventory.MapDataTestBase;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(MicrotusExtension.class)
public abstract class ItemLogicTestBase extends MapDataTestBase {

    protected static FileHandler fileHandler;
    protected static SetupItems setupItems;
    protected static SetupDataService<BounceData> setupDataService;
    protected static PlayerConsumer nopFunction = player -> {
        // No operation function for player
    };

    @BeforeAll
    static void setUp() {
        fileHandler = new GsonFileHandler(GsonUtil.GSON);
        setupItems = new SetupItems();
        setupDataService = SetupDataService.create();
    }
}
