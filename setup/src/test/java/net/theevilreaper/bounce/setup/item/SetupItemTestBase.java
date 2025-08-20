package net.theevilreaper.bounce.setup.item;

import net.minestom.testing.extension.MicrotusExtension;
import net.onelitefeather.guira.SetupDataService;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.file.GsonFileHandler;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.common.util.GsonUtil;
import net.theevilreaper.bounce.setup.inventory.MapDataTestBase;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The {@link SetupItemTestBase} is an abstract base class to write test for the existing items in the setup.
 * It provides some data that is required for the tests.
 *
 * @version 1.0.0
 * @since 0.1.0
 * @author theEvilReaper
 */
@ExtendWith(MicrotusExtension.class)
public abstract class SetupItemTestBase extends MapDataTestBase {

    protected static FileHandler fileHandler;
    protected static SetupItems setupItems;
    protected static SetupDataService setupDataService;
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
