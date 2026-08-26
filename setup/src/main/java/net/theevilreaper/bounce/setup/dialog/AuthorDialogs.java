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
import org.jetbrains.annotations.NotNull;

public final class AuthorDialogs extends DialogBase {

    public static final Key AUTHOR_AMOUNT_KEY = create("author_amount_dialog");
    public static final Key AUTHOR_INPUT_KEY = create("bounce_author_setup");

    public static void openAuthorAmountDialog(@NotNull Player player) {
        DialogTemplate dialogTemplate = DialogType.confirm(AUTHOR_AMOUNT_KEY)
                .meta(dialogMeta -> {
                    dialogMeta.closeWithEscape(false);
                    dialogMeta.pause(false);
                    dialogMeta.afterAction(DialogAfterAction.CLOSE);
                    dialogMeta.title(Component.text("Request Author(s)"));
                    dialogMeta.messageBody(template ->
                            template.contents(Component.text("How many builders should the map have?")));
                    dialogMeta.range("amount", range -> range
                            .label(Component.text("Amount"))
                            .start(1)
                            .initial(1)
                            .end(10)
                            .step(1));
                })
                .yesButton(button -> button.width(100).label(Component.text("Save")).tooltip(Component.text("Click to confirm", NamedTextColor.GREEN))
                        .action(new DialogAction.DynamicCustom(AUTHOR_AMOUNT_KEY, getEmptyPayload()))
                )
                .noButton(button -> button.width(101).label(Component.text("Cancel")).tooltip(Component.text("Click to cancel", NamedTextColor.RED)))
                .build();
        dialogTemplate.open(player);
    }

    public static void openAuthorInputDialog(@NotNull Player player, int amount) {
        DialogTemplate dialogTemplate = DialogType.confirm(AUTHOR_INPUT_KEY)
                .meta(dialogMeta -> {
                    dialogMeta.closeWithEscape(false);
                    dialogMeta.pause(false);
                    dialogMeta.afterAction(DialogAfterAction.CLOSE);
                    dialogMeta.title(Component.text("Setup Author(s)"));
                    dialogMeta.messageBody(template ->
                            template.contents(Component.text("Please enter the builder(s)")));

                    for (int i = 0; i < amount; i++) {
                        String key = "author" + i;
                        Component label = Component.text("Author " + i);
                        dialogMeta.text(key, textInputTemplate ->
                                textInputTemplate.activeLabel(false).label(label).maxLength(32).initial(""));
                    }
                })
                .yesButton(button -> button.width(100).label(Component.text("Save")).tooltip(Component.text("Click to confirm", NamedTextColor.GREEN))
                        .action(new DialogAction.DynamicCustom(AUTHOR_INPUT_KEY, getAmountPayload(amount)))
                )
                .noButton(button -> button.width(101).label(Component.text("Cancel")).tooltip(Component.text("Click to cancel", NamedTextColor.RED)))
                .build();
        dialogTemplate.open(player);
    }

    private AuthorDialogs() {
        // Nothing to do here
    }

    private static CompoundBinaryTag getAmountPayload(int amount) {
        return CompoundBinaryTag.builder().putInt("amount", amount).build();
    }
}
