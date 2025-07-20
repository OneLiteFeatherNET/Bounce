package net.theevilreaper.bounce.listener.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.event.BounceGameFinishEvent;
import net.theevilreaper.bounce.profile.BounceProfile;
import net.theevilreaper.bounce.profile.ProfileService;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


public class GameFinishListener implements Consumer<BounceGameFinishEvent> {

    private final ProfileService profileService;

    public GameFinishListener(@NotNull ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public void accept(@NotNull BounceGameFinishEvent event) {
        if (event.getReason() == BounceGameFinishEvent.Reason.PLAYER_LEFT) return;
        BounceProfile winnerProfile = profileService.getWinner();

        if (winnerProfile == null) {
            profileService.clear(bounceProfile -> bounceProfile.getJumpRunnable().cancel());
            return;
        }

        profileService.clear(profile -> {
            profile.getJumpRunnable().cancel();
            Player player = winnerProfile.getPlayer();
            boolean isWinner = profile.equals(winnerProfile);
            profile.sendStats(isWinner);
            var title = Title.title(player.getDisplayName(), Component.text("wons the game", NamedTextColor.GRAY), Title.DEFAULT_TIMES);
            player.sendTitlePart(TitlePart.TITLE, title.title());
            player.sendTitlePart(TitlePart.SUBTITLE, title.subtitle());
            player.sendTitlePart(TitlePart.TIMES, title.times());
        });
    }
}
