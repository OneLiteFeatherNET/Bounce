package net.theevilreaper.bounce.setup.dialog;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.dialog.DialogAction;
import net.minestom.server.dialog.DialogAfterAction;
import net.minestom.server.entity.Player;
import net.onelitefeather.pica.dialog.DialogTemplate;
import net.onelitefeather.pica.dialog.type.DialogType;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class MapDialogs extends DialogBase {

    public static final Key NAME_KEY = create("name_setup_dialog");
    public static final Key DELETE_KEY = create("delete_dialog");
    public static final Key SAVE_VALIDATION_KEY = create("save_validation_dialog");

    public static void openNameDialog(@NotNull Player player) {
        DialogTemplate dialogTemplate = DialogType.confirm(NAME_KEY)
                .meta(dialogMeta -> {
                    dialogMeta.closeWithEscape(false);
                    dialogMeta.pause(false);
                    dialogMeta.afterAction(DialogAfterAction.CLOSE);
                    dialogMeta.title(Component.text("Setup Map Name"));
                    dialogMeta.messageBody(template ->
                            template.contents(Component.text("How the name of the map should be?")));
                    dialogMeta.text("name", textInputTemplate ->
                            textInputTemplate.activeLabel(true).label(Component.text("Name")).maxLength(32).initial(""));
                })
                .yesButton(button -> button.width(100).label(Component.text("Save")).tooltip(Component.text("§7Click to submit"))
                        .action(new DialogAction.DynamicCustom(NAME_KEY, getEmptyPayload()))
                )
                .noButton(button -> button.width(101).label(Component.text("Cancel")).tooltip(Component.text("§7Click to cancel")))
                .build();
        dialogTemplate.open(player);
    }

    public static void openDeleteDialog(@NotNull Player player, @NotNull OverviewType overviewType) {
        DialogTemplate dialogTemplate = DialogType.confirm(DELETE_KEY)
                .meta(dialogMeta -> {
                    dialogMeta.closeWithEscape(false);
                    dialogMeta.pause(false);
                    dialogMeta.afterAction(DialogAfterAction.CLOSE);
                    dialogMeta.title(Component.text("Confirm data deletion"));
                    dialogMeta.messageBody(template ->
                            template.contents(Component.text("The following map data would be deleted:")).width(150));
                    dialogMeta.messageBody(template -> template.contents(Component.empty()).width(1));
                    dialogMeta.messageBody(template -> template.contents(Component.text(overviewType.getName())).width(100));
                })
                .yesButton(button -> button.width(100).label(Component.text("Yes")).tooltip(Component.text("Click to confirm", NamedTextColor.GREEN))
                        .action(new DialogAction.DynamicCustom(DELETE_KEY, getTypePayload(overviewType)))
                )
                .noButton(button -> button.width(101).label(NO_COMPONENT).tooltip(Component.text("Click to cancel", NamedTextColor.RED)))
                .build();
        dialogTemplate.open(player);
    }

    public static void openSaveValidationDialog(@NotNull Player player, @NotNull List<String> missingFields) {
        DialogTemplate dialogTemplate = DialogType.confirm(SAVE_VALIDATION_KEY)
                .meta(dialogMeta -> {
                    dialogMeta.closeWithEscape(false);
                    dialogMeta.pause(false);
                    dialogMeta.afterAction(DialogAfterAction.CLOSE);
                    dialogMeta.title(Component.text("This map isn't ready yet"));
                    dialogMeta.messageBody(template ->
                            template.contents(Component.text("The following data is still missing:")).width(200));
                    dialogMeta.messageBody(template -> template.contents(Component.empty()).width(1));

                    if (missingFields.isEmpty()) {
                        dialogMeta.messageBody(template ->
                                template.contents(Component.text("Unknown", NamedTextColor.RED)).width(200));
                    } else {
                        for (String missingField : missingFields) {
                            dialogMeta.messageBody(template ->
                                    template.contents(Component.text("- " + missingField, NamedTextColor.RED)).width(200));
                        }
                    }

                    dialogMeta.messageBody(template -> template.contents(Component.empty()).width(1));
                    dialogMeta.messageBody(template ->
                            template.contents(Component.text("Go back and fill them in, or delete this setup.")).width(200));
                })
                .yesButton(button -> button.width(155).label(Component.text("Delete data")).tooltip(Component.text("Click to confirm", NamedTextColor.GREEN))
                        .action(new DialogAction.DynamicCustom(SAVE_VALIDATION_KEY, getEmptyPayload()))
                )
                .noButton(button -> button.width(155).label(Component.text("Back")).tooltip(Component.text("Click to cancel", NamedTextColor.RED)))
                .build();
        dialogTemplate.open(player);
    }

    private MapDialogs() {
        // Nothing to do here
    }

    @Contract(pure = true)
    private static CompoundBinaryTag getTypePayload(@NotNull OverviewType overviewType) {
        return CompoundBinaryTag.builder().putInt("type", overviewType.ordinal()).build();
    }
}
