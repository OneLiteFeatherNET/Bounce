package net.theevilreaper.bounce.setup.dialog;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.theevilreaper.bounce.setup.dialog.type.AuthorInputDialog;
import net.theevilreaper.bounce.setup.dialog.type.AuthorRequestDialog;
import net.theevilreaper.bounce.setup.dialog.type.DeleteDialog;
import net.theevilreaper.bounce.setup.dialog.type.NameInputDialog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SetupDialogRegistry implements DialogRegistry {

    private final Map<Key, DialogTemplate> dialogMap;

    public SetupDialogRegistry() {
        this.dialogMap = new HashMap<>();

        Component saveButton = Component.text("Save");
        Component cancelButton = Component.text("Cancel");

        this.registerDialog(
                new NameInputDialog(
                        Component.text("Setup Map Name"),
                        saveButton,
                        cancelButton
                )
        );
        this.registerDialog(new AuthorInputDialog(
                Component.text("Setup Author(s)"),
                saveButton,
                cancelButton
                // Default to 1 author
        ));
        this.registerDialog(new DeleteDialog());

        for (Key key : dialogMap.keySet()) {
            System.out.println("Registered dialog: " + key.asString());
        }
    }

    private void registerDialog(@NotNull DialogTemplate<?> dialog) {
        dialogMap.put(dialog.key(), dialog);
    }

    @Override
    public @Nullable DialogTemplate get(@Nullable Key key) {
        return this.dialogMap.getOrDefault(key, null);
    }

    @Override
    public boolean contains(@NotNull Key key) {
        return false;
    }
}
