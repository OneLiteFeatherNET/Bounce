package net.theevilreaper.bounce.setup.dialog;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.dialog.DialogAction;
import net.minestom.server.dialog.DialogAfterAction;
import net.minestom.server.entity.Player;
import net.onelitefeather.pica.dialog.DialogTemplate;
import net.onelitefeather.pica.dialog.type.DialogType;
import org.jetbrains.annotations.NotNull;

/**
 * Groups the simple "one range input, confirm or cancel" dialogs used to change a single numeric map value.
 */
public final class ValueDialogs extends DialogBase {

    public static final Key VALUE_KEY = create("value_setup_dialog");
    public static final Key WEIGHT_KEY = create("weight_setup_dialog");
    public static final Key SHUFFLE_INTERVAL_KEY = create("shuffle_interval_setup_dialog");
    public static final Key RESHUFFLE_PERCENTAGE_KEY = create("reshuffle_percentage_setup_dialog");

    public static void openBounceValue(@NotNull Player player) {
        open(player, VALUE_KEY, "Change block boost", "How much the block should bounce?",
                "bounce_amount", "Amount", "options.generic_value", 1, 10, 1, 1, 200);
    }

    public static void openWeight(@NotNull Player player) {
        open(player, WEIGHT_KEY, "Change spawn chance", "Spawn probability in percent (0 - 100%):",
                "weight_percentage", "Chance", "options.percent_value", 0, 100, 5, 0.1f, 320);
    }

    public static void openShuffleInterval(@NotNull Player player) {
        open(player, SHUFFLE_INTERVAL_KEY, "Change shuffle interval", "Reshuffle interval in ticks (20 ticks = 1 second):",
                "interval_ticks", "Interval", "options.generic_value", 20, 600, 100, 10, 320);
    }

    public static void openReshufflePercentage(@NotNull Player player) {
        open(player, RESHUFFLE_PERCENTAGE_KEY, "Change reshuffle percentage", "Percentage of the area re-rolled\non each reshuffle (0 - 100%):",
                "reshuffle_percentage", "Percentage", "options.percent_value", 0, 100, 10, 0.1f, 320);
    }

    private static void open(
            @NotNull Player player,
            @NotNull Key key,
            @NotNull String title,
            @NotNull String message,
            @NotNull String inputKey,
            @NotNull String inputLabel,
            @NotNull String labelFormat,
            float start,
            float end,
            float initial,
            float step,
            int width
    ) {
        DialogTemplate dialogTemplate = DialogType.confirm(key)
                .meta(dialogMeta -> {
                    dialogMeta.closeWithEscape(false);
                    dialogMeta.pause(false);
                    dialogMeta.afterAction(DialogAfterAction.CLOSE);
                    dialogMeta.title(Component.text(title));
                    dialogMeta.messageBody(template -> template.contents(Component.text(message)).width(width));
                    dialogMeta.range(inputKey, range -> range
                            .label(Component.text(inputLabel))
                            .labelFormat(labelFormat)
                            .width(width)
                            .start(start)
                            .end(end)
                            .initial(initial)
                            .step(step));
                })
                .yesButton(button -> button.width(155).label(Component.text("Click to confirm")).tooltip(Component.text("Click to confirm", NamedTextColor.GREEN))
                        .action(new DialogAction.DynamicCustom(key, getEmptyPayload()))
                )
                .noButton(button -> button.width(155).label(Component.text("Click to cancel")).tooltip(Component.text("Click to cancel", NamedTextColor.RED)))
                .build();
        dialogTemplate.open(player);
    }

    private ValueDialogs() {
        // Nothing to do here
    }
}
