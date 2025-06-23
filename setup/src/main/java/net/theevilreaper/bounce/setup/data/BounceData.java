package net.theevilreaper.bounce.setup.data;

import net.onelitefeather.guira.data.BaseSetupData;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.bounce.common.map.GameMap;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class BounceData extends BaseSetupData<GameMap> {

    private final FileHandler fileHandler;

    public BounceData(@NotNull UUID owner, @NotNull MapEntry mapEntry, @NotNull FileHandler fileHandler) {
        super(owner, mapEntry);
        this.fileHandler = fileHandler;
    }

    @Override
    public void save() {
        this.fileHandler.save(mapEntry.getDirectoryRoot(), map);
    }

    @Override
    public void reset() {

    }

    @Override
    public void loadData() {

    }
}
